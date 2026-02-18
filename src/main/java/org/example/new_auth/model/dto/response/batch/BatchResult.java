package org.example.new_auth.model.dto.response.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BatchResult<T> {

    private List<T> success;
    private List<BatchError> errors;

    public BatchResult(List<T> success, List<BatchError> errors) {
        this.success = success == null ? new ArrayList<>() : new ArrayList<>(success);
        this.errors = errors == null ? new ArrayList<>() : new ArrayList<>(errors);
    }

    public List<T> getSuccess() {
        return Collections.unmodifiableList(success);
    }

    public void setSuccess(List<T> success) {
        this.success = success == null ? new ArrayList<>() : new ArrayList<>(success);
    }

    public List<BatchError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public void setErrors(List<BatchError> errors) {
        this.errors = errors == null ? new ArrayList<>() : new ArrayList<>(errors);
    }

    public void addSuccess(T success) {
        if (success == null) return;
        this.success.add(success);
    }

    public void addSuccess(List<T> success) {
        if (success == null || success.isEmpty()) return;
        success.forEach(this::addSuccess);
    }

    public void addError(BatchError error) {
        if (error == null) return;
        this.errors.add(error);
    }

    public void addErrors(List<BatchError> errors) {
        if (errors == null || errors.isEmpty()) return;
        errors.forEach(this::addError);
    }

}
