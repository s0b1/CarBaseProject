package brainacad.service;

import brainacad.model.Driver;
import brainacad.model.Payment;
import brainacad.model.Route;
import brainacad.repository.PaymentRepository;
import brainacad.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService
{

    private final PaymentRepository paymentRepository;
    private final DriverRepository driverRepository;

    public Payment payDriver(Driver driver, Route route, BigDecimal amount)
    {
        if (driver.getTotalEarnings() == null)
        {
            driver.setTotalEarnings(BigDecimal.ZERO);
        }

        Payment payment = Payment.builder()
                .driver(driver)
                .route(route)
                .amount(amount)
                .build();

        driver.setTotalEarnings(driver.getTotalEarnings().add(amount));
        driverRepository.save(driver);

        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsForDriver(Driver driver)
    {
        return paymentRepository.findByDriver(driver);
    }

    public List<Payment> getAllPayments()
    {
        return paymentRepository.findAll();
    }
}
