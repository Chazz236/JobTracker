package com.jobtracker.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class JobServiceException extends RuntimeException {
    public JobServiceException(String message) {
        super(message);
    }
}
