package guru.springframework.msscssm.services;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    public static final String PAYMENT_ID_HEADER = "payment_id";
    
    private final PaymentRepository repository;
    private final StateMachineFactory<PaymentState, PaymentEvent> factory;
    private final PaymentStateChangeInterceptor interceptor;

    @Transactional
    @Override
    public Payment newPayment(Payment payment) {
	payment.setState(PaymentState.NEW);
	return repository.save(payment);
    }

    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
	StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
	sendEvent(paymentId, sm, PaymentEvent.PRE_AUTH_APPROVED);
	return sm;
    }

    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) {
	StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
	sendEvent(paymentId, sm, PaymentEvent.AUTHORIZED);
	return sm;
    }

    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {
	StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
	sendEvent(paymentId, sm, PaymentEvent.AUTH_DECLINED);
	return sm;
    }

    private StateMachine<PaymentState, PaymentEvent> build(Long paymentId) {
	Payment payment = repository.getOne(paymentId);
	StateMachine<PaymentState, PaymentEvent> sm = factory.getStateMachine(Long.toString(payment.getId()));
	sm.stop();
	sm.getStateMachineAccessor().doWithAllRegions(sma -> {
	    sma.addStateMachineInterceptor(interceptor);
	    sma.resetStateMachine(
		    new DefaultStateMachineContext<PaymentState, PaymentEvent>(payment.getState(), null, null, null));
	});
	sm.start();
	return sm;
    }
    
    private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> sm, PaymentEvent event) {
	Message msg = MessageBuilder.withPayload(event).setHeader(PAYMENT_ID_HEADER, paymentId).build();
	sm.sendEvent(msg);
    }

}
