package guru.springframework.msscssm.services;

import org.springframework.statemachine.StateMachine;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.domain.PaymentEvent;

public interface PaymentService {
    
    Payment newPayment(Payment payment);
    
    StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);
    
    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);
    
    StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);
    
    

}
