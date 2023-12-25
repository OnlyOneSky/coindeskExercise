package com.chc.coindesk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Currency {
    @Id
    int id;
    String currencyCode;
}
