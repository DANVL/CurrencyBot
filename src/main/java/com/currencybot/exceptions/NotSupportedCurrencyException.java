package com.currencybot.exceptions;

public class NotSupportedCurrencyException extends Exception{

    public NotSupportedCurrencyException(String message) {
        super(message);
    }

    public NotSupportedCurrencyException() {
    }
}
