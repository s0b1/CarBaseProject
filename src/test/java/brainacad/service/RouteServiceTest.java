package brainacad.service;

import brainacad.model.*;
import brainacad.repository.RouteRepository;
import brainacad.repository.VehicleRepository;
import brainacad.repository.RepairRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RouteServiceTest
{

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private RepairRequestRepository repairRequestRepository;

    @InjectMocks
    private RouteService routeService;

    private Driver driver;
    private Vehicle vehicle;
    private CargoRequest cargo;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);

        driver = Driver.builder()
                .id(1L)
                .name("Test Driver")
                .experience(5)
                .available(true)
                .totalEarnings(java.math.BigDecimal.ZERO)
                .build();

        vehicle = Vehicle.builder()
                .id(1L)
                .type("Truck")
                .loadCapacity(1000)
                .condition("GOOD")
                .available(true)
                .build();

        cargo = CargoRequest.builder()
                .id(1L)
                .destination("San Andreas")
                .cargoType("food")
                .weight(300.0)
                .build();
    }

    @Test
    void testAssignRoute()
    {
        Route expectedRoute = Route.builder()
                .id(1L)
                .driver(driver)
                .vehicle(vehicle)
                .cargoRequest(cargo)
                .completed(false)
                .build();

        when(routeRepository.save(any(Route.class))).thenReturn(expectedRoute);

        Route saved = routeService.assignRoute(driver, vehicle, cargo);
        assertNotNull(saved);
        assertEquals(driver, saved.getDriver());
        assertEquals(vehicle, saved.getVehicle());
        assertEquals(cargo, saved.getCargoRequest());
    }

    @Test
    void testGetIncompleteRoutes()
    {
        Route r = Route.builder().completed(false).build();
        when(routeRepository.findByCompletedFalse()).thenReturn(List.of(r));

        List<Route> incomplete = routeService.getIncompleteRoutes();
        assertEquals(1, incomplete.size());
        assertFalse(incomplete.get(0).isCompleted());
    }

    @Test
    void testCompleteRoute_MarksAsCompleted()
    {
        Route route = Route.builder()
                .id(10L)
                .driver(driver)
                .vehicle(vehicle)
                .cargoRequest(cargo)
                .completed(false)
                .build();

        when(routeRepository.findById(10L)).thenReturn(Optional.of(route));
        when(routeRepository.save(any(Route.class))).thenReturn(route);

        routeService.completeRoute(10L);

        assertTrue(route.isCompleted());
        verify(routeRepository, times(1)).save(route);
    }
}
