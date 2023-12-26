package com.chc.coindesk.repository;

import com.chc.coindesk.dto.BpiDTO;
import com.chc.coindesk.model.Bpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface BpiRepository extends JpaRepository<Bpi, BigInteger> {
    Bpi findByCurrencyCode(String code);

    @Query("select new com.chc.coindesk.dto.BpiDTO(bpi.currencyCode, bpi.symbol, bpi.rate, translation.translation, bpi.rateFloat) " +
            "from Bpi bpi join Currency currency on bpi.currencyCode = currency.currencyCode " +
            "join Translation translation on translation.translationKey.textColumnId = bpi.description " +
            "and currency.id = translation.translationKey.translatingTableUid " +
            "and translation.translationKey.languageId = :languageId " +
            "and bpi.currencyCode = :currencyCode")
    BpiDTO findByCodeAndLanguageId(@Param("currencyCode") String currencyCode, @Param("languageId") int languageId);
}
