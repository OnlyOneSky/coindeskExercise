package com.chc.coindesk.repository;

import com.chc.coindesk.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    Currency findIdByCurrencyCode(String currencyCode);
    Currency findTopByOOrderByIdDesc();
}
