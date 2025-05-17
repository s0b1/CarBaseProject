package brainacad.service;

import brainacad.model.Driver;
import brainacad.model.Payment;
import brainacad.model.Route;
import brainacad.repository.DriverRepository;
import brainacad.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest
{

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Driver driver;
    private Route route;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);

        driver = Driver.builder()
                .id(1L)
                .name("Max Speed")
                .experience(5)
                .available(true)
                .totalEarnings(BigDecimal.ZERO)
                .build();

        route = Route.builder()
                .id(1L)
                .driver(driver)
                .completed(true)
                .build();
    }

    @Test
    void testPayDriver_AddsToEarnings()
    {
        BigDecimal paymentAmount = new BigDecimal("300.00");

        Payment expectedPayment = Payment.builder()
                .driver(driver)
                .route(route)
                .amount(paymentAmount)
                .build();

        when(paymentRepository.save(any())).thenReturn(expectedPayment);

        Payment result = paymentService.payDriver(driver, route, paymentAmount);

        assertNotNull(result);
        assertEquals(paymentAmount, result.getAmount());
        assertEquals(new BigDecimal("300.00"), driver.getTotalEarnings());
        verify(driverRepository, times(1)).save(driver);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testGetAllPayments()
    {
        Payment p = Payment.builder().driver(driver).amount(BigDecimal.TEN).build();
        when(paymentRepository.findAll()).thenReturn(List.of(p));

        List<Payment> all = paymentService.getAllPayments();
        assertEquals(1, all.size());
    }

    @Test
    void testGetPaymentsForDriver()
    {
        Payment p = Payment.builder().driver(driver).amount(BigDecimal.ONE).build();
        when(paymentRepository.findByDriver(driver)).thenReturn(List.of(p));

        List<Payment> list = paymentService.getPaymentsForDriver(driver);
        assertEquals(1, list.size());
        assertEquals(driver, list.get(0).getDriver());
    }
}
