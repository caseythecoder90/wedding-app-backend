package com.wedding.backend.wedding_app.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "error_codes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEntity {

    @Id
    @Column(name = "uniq_err_key")
    private String errorKey;

    @Column(name = "err_cd")
    private String errorCode;

    @Column(name = "err_reason")
    private String errorReason;

    @Column(name = "err_msg")
    private String errorMessage;

    @Column(name = "retryable")
    private boolean retryable;
}
