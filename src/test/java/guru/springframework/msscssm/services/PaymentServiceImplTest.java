package guru.springframework.msscssm.services;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.repository.PaymentRepository;

@SpringBootTest
class PaymentServiceImplTest {
    
    @Autowired
    PaymentService paymentService;
    
    @Autowired
    PaymentRepository repository;
    
    Payment payment;

    @BeforeEach
    void setUp() throws Exception {
	payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Transactional
    @Test
    void testPreAuth() {
	Payment savedPayment = paymentService.newPayment(payment);
	paymentService.preAuth(payment.getId());
	Payment preAuthorizedPayment = repository.getOne(payment.getId());
	System.out.println(preAuthorizedPayment);
    }

}
