package com.sample.tracking.config.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LogicException extends RuntimeException{
    private final String message;

    public LogicException(String message) {
        super(message);
        this.message = message;
    }
}
