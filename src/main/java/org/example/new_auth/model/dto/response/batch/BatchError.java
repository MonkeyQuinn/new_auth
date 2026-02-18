package org.example.new_auth.model.dto.response.batch;

import org.example.new_auth.exception.ApiException;
import org.example.new_auth.model.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;

public record BatchError(String item, String ref, ErrorResponse error) {

    public static <T> BatchError fromException(T t, Exception e, Function<T, String> itemExtractor, Function<T, String> refExtractor) {
        String item;
        try {
            item = itemExtractor.apply(t);
        } catch (Exception ex) {
            item = "<item-error>";
        }

        String ref;
        try {
            ref = refExtractor.apply(t);
        } catch (Exception ex) {
            ref = "<ref-error>";
        }

        int status = 0;
        String message = Objects.nonNull(e.getMessage()) ? e.getMessage() : e.getClass().getSimpleName();
        String uri = "";
        String path = "";

        if (e instanceof ApiException) {
            status = ((ApiException) e).getStatus().value();
            uri = ((ApiException) e).getUri();
            path = ((ApiException) e).getPath();

        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        ErrorResponse error = new ErrorResponse(status, message, uri, path, LocalDateTime.now());

        return new BatchError(item, ref, error);
    }

}
