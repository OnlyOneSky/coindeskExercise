package com.chc.coindesk.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class TranslationKey implements Serializable {
    private int languageId;
    private int textColumnId;
    private int translatingTableUid;
}
