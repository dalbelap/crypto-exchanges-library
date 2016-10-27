package com.coincelt.traderapi.domain.exceptions;

/**
 * Not found exchange exception
 */
public class ExchangeNotSupportedException extends Exception {
    public ExchangeNotSupportedException(String name) {
        super(String.format("Exchange %s not supported", name));
    }
}
