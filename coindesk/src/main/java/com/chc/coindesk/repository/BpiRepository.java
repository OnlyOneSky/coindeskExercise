package com.chc.coindesk.repository;

import com.chc.coindesk.model.Bpi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface BpiRepository extends JpaRepository<Bpi, BigInteger> {
    Bpi findByCurrencyCode(String code);
}
