package guru.springframework.msscssm.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class StateMachineConfigTest {
    
    @Autowired
    StateMachineFactory<PaymentState, PaymentEvent> factory;

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void test() {
	StateMachine<PaymentState, PaymentEvent> sm = factory.getStateMachine(UUID.randomUUID().toString());
	sm.start();
	log.info("Initial state at startup: " + sm.getState().getId());
	sm.sendEvent(PaymentEvent.PRE_AUTHORIZE);
	log.info("Sent Event " + PaymentEvent.PRE_AUTHORIZE + " caused a state of : " + sm.getState().getId());
	sm.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
	log.info("Sent Event " + PaymentEvent.PRE_AUTH_APPROVED + " caused a state of : " + sm.getState().getId());
    }

}
