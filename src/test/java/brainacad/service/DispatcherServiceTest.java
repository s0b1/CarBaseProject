package brainacad.service;

import brainacad.model.*;
import brainacad.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class DispatcherServiceTest
{

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private CargoRequestRepository cargoRequestRepository;

    @Mock
    private RouteService routeService;

    @InjectMocks
    private DispatcherService dispatcherService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignPendingCargoRequest_Success() {
        CargoRequest cargo = CargoRequest.builder()
                .id(1L)
                .cargoType("electronics")
                .weight(300)
                .destination("Liberty City")
                .build();

        Driver driver = Driver.builder()
                .id(1L)
                .name("Ivan")
                .experience(3)
                .available(true)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .type("Van")
                .loadCapacity(500)
                .available(true)
                .build();

        when(cargoRequestRepository.findAll()).thenReturn(List.of(cargo));
        when(driverRepository.findAll()).thenReturn(List.of(driver));
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));

        dispatcherService.assignPendingCargoRequests();

        verify(driverRepository).save(driver);
        verify(vehicleRepository).save(vehicle);
        verify(routeService).assignRoute(driver, vehicle, cargo);
    }

    @Test
    void testAssignPendingCargoRequest_Fails_NoDriver()
    {
        CargoRequest cargo = CargoRequest.builder()
                .cargoType("fuel")
                .weight(1000)
                .destination("Carcer City")
                .build();

        Vehicle vehicle = Vehicle.builder()
                .loadCapacity(1500)
                .available(true)
                .build();

        when(cargoRequestRepository.findAll()).thenReturn(List.of(cargo));
        when(driverRepository.findAll()).thenReturn(Collections.emptyList());
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));

        dispatcherService.assignPendingCargoRequests();

        verify(routeService, never()).assignRoute(any(), any(), any());
    }

    @Test
    void testAssignPendingCargoRequest_Fails_NoVehicle() {
        CargoRequest cargo = CargoRequest.builder()
                .cargoType("food")
                .weight(1000)
                .destination("Los Santos")
                .build();

        Driver driver = Driver.builder()
                .experience(5)
                .available(true)
                .build();

        when(cargoRequestRepository.findAll()).thenReturn(List.of(cargo));
        when(driverRepository.findAll()).thenReturn(List.of(driver));
        when(vehicleRepository.findAll()).thenReturn(Collections.emptyList());

        dispatcherService.assignPendingCargoRequests();

        verify(routeService, never()).assignRoute(any(), any(), any());
    }
}
