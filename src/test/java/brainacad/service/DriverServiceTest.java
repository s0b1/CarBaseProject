package brainacad.service;

import brainacad.model.Driver;
import brainacad.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DriverServiceTest
{

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    public DriverServiceTest()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveDriver()
    {
        Driver driver = Driver.builder()
                .name("Test Driver")
                .experience(5)
                .totalEarnings(BigDecimal.ZERO)
                .available(true)
                .build();

        when(driverRepository.save(driver)).thenReturn(driver);

        Driver saved = driverService.saveDriver(driver);
        assertEquals("Test Driver", saved.getName());
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    public void testGetAvailableDrivers()
    {
        Driver d1 = new Driver(1L, "D1", 5, true, BigDecimal.ZERO);
        when(driverRepository.findByAvailableTrue()).thenReturn(List.of(d1));

        List<Driver> available = driverService.getAvailableDrivers();
        assertEquals(1, available.size());
        assertTrue(available.get(0).isAvailable());
    }

    @Test
    public void testGetDriverById()
    {
        Driver d = new Driver(1L, "Test", 5, true, BigDecimal.ZERO);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(d));

        Optional<Driver> result = driverService.getDriverById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
    }
}
