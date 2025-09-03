package com.wedding.backend.wedding_app.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {

    VENMO("Venmo"),
    ZELLE("Zelle"),
    OTHER("Other");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

}