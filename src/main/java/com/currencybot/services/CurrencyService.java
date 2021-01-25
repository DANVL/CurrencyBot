package com.currencybot.services;

import com.currencybot.entities.Currency;
import com.currencybot.entities.Rate;
import com.currencybot.entities.Source;
import com.currencybot.exceptions.NoExchangersException;
import com.currencybot.exceptions.NotSupportedCurrencyException;

public interface CurrencyService {
    Rate getCurrencyRate(Currency currency, Source source) throws NotSupportedCurrencyException, NoExchangersException;
}
