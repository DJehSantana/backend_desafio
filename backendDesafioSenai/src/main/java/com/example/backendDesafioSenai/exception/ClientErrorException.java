package com.example.backendDesafioSenai.exception;

public class ClientErrorException extends RuntimeException {
    private final int statusCode;
    private final ErrorResponse errorResponse;

    public ClientErrorException(int statusCode, String message, ErrorResponse errorResponse) {
        super(message);
        this.statusCode = statusCode;
        this.errorResponse = errorResponse;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
