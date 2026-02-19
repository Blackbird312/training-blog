package com.novelis.blog.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
    public NotFoundException(String message, Throwable cause) { super(message, cause); }
    public static NotFoundException of(String message) { return new NotFoundException(message); }
}
