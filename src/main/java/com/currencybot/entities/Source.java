package com.currencybot.entities;

public enum Source {
    OSHAD(13),
    PRIVAT(9),
    MONEY24(25);

    private final int value;

    private Source(int i) {
        this.value = i;
    }

    public int getValue(){
        return value;
    }
}
