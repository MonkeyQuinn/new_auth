package org.example.new_auth.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record BatchResult<T>(List<T> success, List<BatchError> errors) {

    public BatchResult(List<T> success, List<BatchError> errors) {
        this.success = success == null ? new ArrayList<>() : new ArrayList<>(success);
        this.errors = errors == null ? new ArrayList<>() : new ArrayList<>(errors);
    }

    @Override
    public List<T> success() {
        return Collections.unmodifiableList(success);
    }

    @Override
    public List<BatchError> errors() {
        return Collections.unmodifiableList(errors);
    }

    public void addSuccess(T success) {
        if (success == null) return;
        this.success.add(success);
    }

    public void addSuccesses(List<T> success) {
        if (success == null || success.isEmpty()) return;
        success.forEach(this::addSuccess);
    }

    public void addError(BatchError error) {
        if (error == null) return;
        this.errors.add(error);
    }

    public void addErrors(List<BatchError> errors) {
        if (errors != null && !errors.isEmpty()) {
            errors.forEach(this::addError);
        }
    }

}
