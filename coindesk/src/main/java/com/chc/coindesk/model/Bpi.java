package com.chc.coindesk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Data
public class Bpi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    BigInteger id;
    int currency_code;
    String symbol;
    String rate;
    double rate_float;
    int description;
    LocalDateTime created;
    LocalDateTime updated;
}
