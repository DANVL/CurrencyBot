package com.currencybot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rate{
    private String buy;
    private String sel;

    @Override
    public String toString() {
        return String.format("%s\\%s",sel,buy);
    }
}
