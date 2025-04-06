package com.wedding.backend.wedding_app.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Detail {
    private String field;
    private String reason;

    public static Detail create(String field, String reason) {
        return new Detail(field, reason);
    }
}
