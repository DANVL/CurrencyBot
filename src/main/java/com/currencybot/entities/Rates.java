package com.currencybot.entities;

import com.currencybot.entities.Rate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rates{
    private Rate rur;
    private Rate usd;
    private Rate eur;
}
