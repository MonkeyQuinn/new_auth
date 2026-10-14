package org.example.new_auth.exception;

public class TarantoolOperationException extends RuntimeException {

    private final String operation;
    private final String space;

    public TarantoolOperationException(String message, String operation, String space, Throwable cause) {
        super(message, cause);
        this.operation = operation;
        this.space = space;
    }

    public String getOperation() {
        return operation;
    }

    public String getSpace() {
        return space;
    }

}
