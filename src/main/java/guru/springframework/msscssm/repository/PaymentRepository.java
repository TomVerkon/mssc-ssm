package guru.springframework.msscssm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import guru.springframework.msscssm.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
