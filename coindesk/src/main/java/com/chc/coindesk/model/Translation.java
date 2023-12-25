package com.chc.coindesk.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;
@Entity
@Data
public class Translation {
    @EmbeddedId
    private TranslationKey translationKey;
    private String translation;
}
