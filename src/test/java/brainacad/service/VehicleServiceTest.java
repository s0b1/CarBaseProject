package brainacad.service;

import brainacad.model.Vehicle;
import brainacad.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleServiceTest
{

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveVehicle()
    {
        Vehicle vehicle = Vehicle.builder()
                .type("Van")
                .loadCapacity(1000)
                .condition("GOOD")
                .available(true)
                .build();

        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle saved = vehicleService.saveVehicle(vehicle);
        assertEquals("Van", saved.getType());
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void testGetAvailableVehicles()
    {
        Vehicle v1 = new Vehicle(1L, "Truck", 1200, "GOOD", true);
        when(vehicleRepository.findByAvailableTrue()).thenReturn(List.of(v1));

        List<Vehicle> available = vehicleService.getAvailableVehicles();
        assertEquals(1, available.size());
        assertTrue(available.get(0).isAvailable());
    }

    @Test
    void testGetVehicleById()
    {
        Vehicle v = new Vehicle(1L, "Box Truck", 1500, "GOOD", true);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(v));

        Optional<Vehicle> result = vehicleService.getVehicleById(1L);
        assertTrue(result.isPresent());
        assertEquals("Box Truck", result.get().getType());
    }
}
