package com.db.flare.gcpbatchjob.job;

import com.db.flare.gcpbatchjob.common.FlareSkippableException;
import com.db.flare.gcpbatchjob.model.Payment;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertThrows;


class PaymentProcessorTest {

    private PaymentProcessor paymentProcessor = new PaymentProcessor();

    @Test
    public void process() throws Exception {
        final Payment inputPayment = Payment.builder().amount(123).build();
        final Payment process = paymentProcessor.process(inputPayment);
        Assert.isTrue(inputPayment.getAmount()==process.getAmount(),"Amount is not same");
    }

    @Test()
    public void exceptionTest() throws Exception {
        final Payment inputPayment = Payment.builder().amount(-12).build();
        assertThrows(FlareSkippableException.class, () -> {
            paymentProcessor.process(inputPayment);
        });
    }
}