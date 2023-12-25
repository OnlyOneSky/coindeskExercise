package com.chc.coindesk.repository;

import com.chc.coindesk.model.Translation;
import com.chc.coindesk.model.TranslationKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRepository extends JpaRepository<Translation, TranslationKey> {
}
