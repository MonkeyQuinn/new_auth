package org.example.new_auth.config;

import io.tarantool.driver.exceptions.TarantoolException;
import org.example.new_auth.exception.TarantoolOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class TarantoolErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(TarantoolErrorHandler.class);

    public <T> T execute(String operation, String space, Runnable action) {
        return execute(operation, space, () -> {
            action.run();
            return null;
        });
    }

    public <T> T execute(String operation, String space, Supplier<T> action) {
        try {
            return action.get();

        } catch (TarantoolException e) {
            log.error("Tarantool error [operation={}, space={}]: {}", operation, space, e.getMessage(), e);
            throw new TarantoolOperationException("Tarantool error: " + e.getMessage(), operation, space, e);

        } catch (Exception e) {
            if (isCauseByInterrupt(e)) {
                Thread.currentThread().interrupt();
            }

            log.error("Tarantool error [operation={}, space={}]: {}", operation, space, e.getMessage(), e);
            throw new TarantoolOperationException("Unhandled error: " + e.getMessage(), operation, space, e);
        }
    }

    private boolean isCauseByInterrupt(Throwable cause) {
        Throwable t = cause;

        while (t != null) {
            if (t instanceof InterruptedException) return true;
            t = t.getCause();
        }

        return false;
    }

}
