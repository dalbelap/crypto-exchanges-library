package com.coincelt.traderapi.domain.exceptions;

/**
 * Loan type not supported exception
 */
public class UnknownLoanTypeException extends Exception {
    public UnknownLoanTypeException(String name) {
        super(String.format("Unknown loan type %s", name));
    }
}
