package org.example.new_auth.context;

import jakarta.servlet.http.HttpServletRequest;
import org.example.new_auth.batch.BatchParams;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class BatchParamsContext {

    private final BatchParams params;

    public BatchParamsContext(HttpServletRequest request) {
        this.params = new BatchParams(
                getInt(request, "pack", BatchParams.DEFAULT_PACK),
                getInt(request, "interval", BatchParams.DEFAULT_INTERVAL)
        );
    }

    public BatchParams getParams() {
        return params;
    }

    private int getInt(HttpServletRequest request, String name, int defaultValue) {
        String value = request.getParameter(name);
        if (value == null || value.isBlank()) return defaultValue;

        try {
            return Integer.parseInt(value);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Parameter '%s' must be an integer".formatted(name));
        }
    }

}
