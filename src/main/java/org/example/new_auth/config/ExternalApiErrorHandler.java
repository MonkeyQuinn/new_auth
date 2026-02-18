package org.example.new_auth.config;

import org.example.new_auth.exception.ApiException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component
public class ExternalApiErrorHandler {

    private static final int MAX_ERROR_BODY = 16 * 1024;

    private final ObjectMapper mapper;

    public ExternalApiErrorHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void handleError(HttpRequest request, ClientHttpResponse response) {
        String externalUri = request.getURI().toString();
        String externalPath = request.getURI().getPath();

        try {
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());
            byte[] bodyBytes = readUpTo(response, MAX_ERROR_BODY);
            String bodyText = toSafeString(bodyBytes, response);

            ParsedError parsed = tryParse(bodyBytes, response);

            String message = buildMessage(parsed, status, bodyText);

            throw new ApiException(message, status, externalUri, externalPath);

        } catch (IOException e) {
            throw new ApiException(e.getMessage(), HttpStatus.BAD_GATEWAY, externalUri, externalPath);
        }
    }

    private String buildMessage(ParsedError parsed, HttpStatus status, String text) {
        if (parsed.message() != null && !parsed.message().isBlank()) {
            return parsed.message();
        }

        if (text != null && !text.isBlank()) {
            return "External API Error: (" + status.value() + "): " + shorten(text);
        }

        return "External API Error: (" + status.value() + ")";
    }

    private ParsedError tryParse(byte[] bytes, ClientHttpResponse response) {
        if (bytes == null || bytes.length == 0) return ParsedError.empty();

        String text = toSafeString(bytes, response).trim();
        if (!(text.startsWith("{") || text.startsWith("["))) return ParsedError.empty();

        try {
            JsonNode root = mapper.readTree(bytes);

            List<String> msgPointers = List.of();
            String message = firstText(root, msgPointers);

            return new ParsedError(message);

        } catch (Exception ignored) {
            return ParsedError.empty();
        }
    }

    private String firstText(JsonNode root, List<String> pointers) {
        for (String p : pointers) {
            JsonNode n = root.at(p);

            if (n != null && !n.isMissingNode() && n.isValueNode()) {
                String s = n.asString();
                if (s != null && !s.isBlank()) return s;
            }
        }

        return null;
    }

    private byte[] readUpTo(ClientHttpResponse response, int maxBytes) {
        try (InputStream is = response.getBody()) {
            if (is == null) return new byte[0];
            return is.readNBytes(maxBytes);

        } catch (IOException e) {
            return new byte[0];
        }
    }

    private String toSafeString(byte[] bytes, ClientHttpResponse response) {
        if (bytes == null || bytes.length == 0) return "";

        Charset charset = Optional.ofNullable(response.getHeaders().getContentType())
                .map(MediaType::getCharset)
                .orElse(StandardCharsets.UTF_8);

        return new String(bytes, charset);
    }

    private String shorten(String s) {
        if (s == null) return "";
        return s.length() <= 500 ? s : s.substring(0, 500) + "...";
    }

    private record ParsedError(String message) {
        static ParsedError empty() {
            return new ParsedError(null);
        }
    }

}
