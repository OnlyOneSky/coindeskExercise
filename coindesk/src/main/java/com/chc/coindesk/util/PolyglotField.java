package com.chc.coindesk.util;

public enum PolyglotField {
    CURRENCY_DESCRIPTION(1),
    TEST_NAME(2);

    private final int id;
    PolyglotField(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
