package com.currencybot.services;

import com.currencybot.entities.Currency;
import com.currencybot.entities.Source;

public interface CurrencyService {
    String getCurrencyRate(Currency currency, Source source);
}
