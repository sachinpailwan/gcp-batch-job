package com.db.flare.gcpbatchjob.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Payment {

    private String transactionId;
    private String ordering;
    private String beneficiary;
    private String date;
    private double amount;
}
