package com.franilones.medium_blog_scrapper.domain.exception;

/**
 * Generic exception for MediumScrapperErrors.
 */
public class MediumScrapperException extends RuntimeException {
    public MediumScrapperException(String message) {
        super(message);
    }

    public MediumScrapperException(String message, Throwable cause) {
        super(message, cause);
    }
}