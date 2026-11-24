package org.example.new_auth.util;

import org.example.new_auth.batch.BatchResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class BatchResponsesUtils {

    private BatchResponsesUtils() {
    }

    public static <T> ResponseEntity<BatchResult<T>> toResponse(BatchResult<T> batchResult) {
        return batchResult.hasFailures()
                ? ResponseEntity.status(HttpStatus.MULTI_STATUS).body(batchResult)
                : ResponseEntity.ok(batchResult);
    }

}
