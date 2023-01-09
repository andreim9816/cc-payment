package com.example.payment.service;

import com.example.payment.domain.Payment;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.request.PaymentReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(PaymentReqDto paymentDto) {
        log.info("payment received: " + paymentDto);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Payment newPayment = Payment.builder()
                .ibanTo(paymentDto.getIbanTo())
                .ibanFrom(paymentDto.getIbanFrom())
                .amount(paymentDto.getAmount())
                .currency(paymentDto.getCurrency())
                .timestamp(timestamp)
                .build();

        paymentRepository.save(newPayment);
    }

    public List<Payment> getPayments(String iban) {
        List<Payment> payments = paymentRepository.getPaymentByIbanFromOrIbanToOrderByTimestamp(iban, iban);
        payments.forEach(payment -> {
            if (payment.getIbanFrom().equals(iban))
                payment.setAmount(-payment.getAmount());
        });
        return payments;
    }
}
