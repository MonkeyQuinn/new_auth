package org.example.new_auth.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String uri;
    private final String path;

    public ApiException(String message, HttpStatus status, String uri, String path) {
        super(message);
        this.status = status;
        this.uri = uri;
        this.path = path;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getUri() {
        return uri;
    }

    public String getPath() {
        return path;
    }

}
