package com.chc.coindesk.repository;

import com.chc.coindesk.model.FetchInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface FetchInfoRepository extends JpaRepository<FetchInfo, BigInteger> {
    FetchInfo findFirstByOrderByIdDesc();
}
