package brainacad.service;

import brainacad.model.CargoRequest;
import brainacad.repository.CargoRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CargoRequestServiceTest
{

    @Mock
    private CargoRequestRepository cargoRequestRepository;

    @InjectMocks
    private CargoRequestService cargoRequestService;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveRequest()
    {
        CargoRequest request = CargoRequest.builder()
                .destination("Lost Heaven")
                .cargoType("electronics")
                .weight(300.5)
                .build();

        when(cargoRequestRepository.save(request)).thenReturn(request);

        CargoRequest saved = cargoRequestService.saveRequest(request);
        assertEquals("Lost Heaven", saved.getDestination());
        verify(cargoRequestRepository, times(1)).save(request);
    }

    @Test
    void testGetAllRequests()
    {
        CargoRequest r = new CargoRequest(1L, "San Andreas", "food", 150.0);
        when(cargoRequestRepository.findAll()).thenReturn(List.of(r));

        List<CargoRequest> all = cargoRequestService.getAllRequests();
        assertEquals(1, all.size());
    }

    @Test
    void testFindByDestination()
    {
        CargoRequest r = new CargoRequest(1L, "Liberty City", "chemicals", 200.0);
        when(cargoRequestRepository.findByDestinationIgnoreCase("Liberty City"))
                .thenReturn(List.of(r));

        var result = cargoRequestService.findByDestination("Liberty City");
        assertEquals(1, result.size());
        assertEquals("chemicals", result.get(0).getCargoType());
    }

    @Test
    void testGetRequestById()
    {
        CargoRequest r = new CargoRequest(1L, "San Andreas", "fuel", 500.0);
        when(cargoRequestRepository.findById(1L)).thenReturn(Optional.of(r));

        var result = cargoRequestService.getRequestById(1L);
        assertTrue(result.isPresent());
        assertEquals("San Andreas", result.get().getDestination());
    }
}
