package com.db.flare.gcpbatchjob.job;

import com.db.flare.gcpbatchjob.common.FlareSkippableException;
import com.db.flare.gcpbatchjob.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PaymentProcessor implements ItemProcessor<Payment,Payment> {
    @Override
    public Payment process(Payment payment) throws Exception {
        if(payment.getAmount()<=0){
            log.error("Skipping payment processing due to invalid amount : "+payment.toString());
            throw new FlareSkippableException();
        }
        return payment;
    }
}
