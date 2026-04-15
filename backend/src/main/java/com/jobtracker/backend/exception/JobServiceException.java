package com.jobtracker.backend.exception;

public class JobServiceException extends RuntimeException {
    public JobServiceException(String message) {
        super(message);
    }
}
