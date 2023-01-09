package com.example.payment.request;

import com.example.payment.domain.Currency;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReqDto {
    @NotBlank
    String ibanTo;
    @NotBlank
    String ibanFrom;
    @NotNull
    Currency currency;
    @NotNull
    Double amount;
    @NotNull
    Timestamp timestamp;
}
