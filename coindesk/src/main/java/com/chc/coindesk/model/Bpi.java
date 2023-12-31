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
    String currencyCode;
    String symbol;
    String rate;
    double rateFloat;
    int description;
    LocalDateTime created;
    LocalDateTime updated;
}
