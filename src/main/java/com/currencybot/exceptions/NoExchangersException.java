package com.currencybot.exceptions;

public class NoExchangersException extends Exception{
    public NoExchangersException() {
    }

    public NoExchangersException(String message) {
        super(message);
    }
}
