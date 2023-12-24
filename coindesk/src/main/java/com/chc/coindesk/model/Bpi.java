package com.chc.coindesk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalTime;

@Entity
@Data
public class Bpi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    BigInteger id;
    int currency_code;
    String symbol;
    String rate;
    float rate_float;
    int description;
    LocalTime created;
    LocalTime updated;
}
