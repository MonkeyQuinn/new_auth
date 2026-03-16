package org.example.new_auth.batch;

import org.example.new_auth.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Component
public class BatchProcessor {

    private static final Logger log = LoggerFactory.getLogger(BatchProcessor.class);

    public <T, R> BatchResult<R> batchMap(Collection<T> source,
                                          Function<T, R> mapper,
                                          Function<T, String> itemExtractor,
                                          Function<T, String> refExtractor) {

        List<R> success = new ArrayList<>();
        List<BatchError> errors = new ArrayList<>();

        for (T t : source) {
            try {
                success.add(mapper.apply(t));

            } catch (Exception e) {
                BatchError batchError = BatchError.fromException(t, e, itemExtractor, refExtractor);
                errors.add(batchError);
                handleError(e, batchError);
            }
        }

        log.debug("Batch processing: {} success, {} errors", success.size(), errors.size());
        return new BatchResult<>(success, errors);
    }

    private void handleError(Exception e, BatchError batchError) {
        if (e instanceof ApiException ae && ae.getStatus() == HttpStatus.NOT_FOUND) {
            log.debug("Batch error item='{}' ref='{}' status={} msg={}",
                    batchError.item(),
                    batchError.ref(),
                    batchError.error().status(),
                    batchError.error().message());

        } else {
            log.warn("Batch error item='{}' ref='{}' status={} msg={}",
                    batchError.item(),
                    batchError.ref(),
                    batchError.error().status(),
                    batchError.error().message(),
                    e);
        }
    }

}
