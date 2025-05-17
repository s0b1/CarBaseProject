package brainacad.repository;

import brainacad.model.Driver;
import brainacad.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>
{
    List<Payment> findByDriver(Driver driver);
}
