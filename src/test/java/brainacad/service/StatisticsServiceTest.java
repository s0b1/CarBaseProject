package brainacad.service;

import brainacad.model.*;
import brainacad.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StatisticsServiceTest
{

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private CargoRequestRepository cargoRequestRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    private Driver driver1;
    private Driver driver2;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);

        driver1 = Driver.builder().id(1L).name("Alex").totalEarnings(new BigDecimal("1000")).build();
        driver2 = Driver.builder().id(2L).name("Olga").totalEarnings(new BigDecimal("2000")).build();
    }

    @Test
    void testGetTopEarningDriver() {
        when(driverRepository.findAll()).thenReturn(List.of(driver1, driver2));

        Optional<Driver> result = statisticsService.getTopEarningDriver();
        assertTrue(result.isPresent());
        assertEquals("Olga", result.get().getName());
    }

    @Test
    void testGetCargoWeightPerDestination()
    {
        CargoRequest cr1 = CargoRequest.builder().destination("Auckland").weight(100).build();
        CargoRequest cr2 = CargoRequest.builder().destination("Auckland").weight(150).build();
        CargoRequest cr3 = CargoRequest.builder().destination("Wellington").weight(200).build();

        Route r1 = Route.builder().completed(true).cargoRequest(cr1).build();
        Route r2 = Route.builder().completed(true).cargoRequest(cr2).build();
        Route r3 = Route.builder().completed(true).cargoRequest(cr3).build();

        when(routeRepository.findAll()).thenReturn(List.of(r1, r2, r3));

        Map<String, Double> result = statisticsService.getCargoWeightPerDestination();
        assertEquals(2, result.size());
        assertEquals(250.0, result.get("Auckland"));
        assertEquals(200.0, result.get("Wellington"));
    }

    @Test
    void testGetCargoWeightPerDriver()
    {
        CargoRequest cr1 = CargoRequest.builder().weight(100).build();
        CargoRequest cr2 = CargoRequest.builder().weight(300).build();

        Route r1 = Route.builder().completed(true).driver(driver1).cargoRequest(cr1).build();
        Route r2 = Route.builder().completed(true).driver(driver1).cargoRequest(cr2).build();

        when(routeRepository.findAll()).thenReturn(List.of(r1, r2));

        Map<String, Double> result = statisticsService.getCargoWeightPerDriver();
        assertEquals(1, result.size());
        assertEquals(400.0, result.get("Alex"));
    }

    @Test
    void testGetEarningsPerDriver()
    {
        when(driverRepository.findAll()).thenReturn(List.of(driver1, driver2));

        Map<String, Double> earnings = statisticsService.getEarningsPerDriver();
        assertEquals(2, earnings.size());
        assertEquals(1000.0, earnings.get("Alex"));
        assertEquals(2000.0, earnings.get("Olga"));
    }
}
