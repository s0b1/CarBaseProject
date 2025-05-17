package brainacad.service;

import brainacad.model.RepairRequest;
import brainacad.model.Vehicle;
import brainacad.repository.RepairRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RepairRequestServiceTest
{

    @Mock
    private RepairRequestRepository repairRequestRepository;

    @InjectMocks
    private RepairRequestService repairRequestService;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRepairRequest()
    {
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .type("Tanker")
                .build();

        RepairRequest request = RepairRequest.builder()
                .vehicle(vehicle)
                .description("Engine failure")
                .resolved(false)
                .build();

        when(repairRequestRepository.save(any())).thenReturn(request);

        RepairRequest result = repairRequestService.createRepairRequest(vehicle, "Engine failure");

        assertNotNull(result);
        assertEquals("Engine failure", result.getDescription());
        assertFalse(result.isResolved());
        assertEquals(vehicle, result.getVehicle());
    }

    @Test
    void testGetUnresolvedRequests()
    {
        RepairRequest r = RepairRequest.builder().resolved(false).build();
        when(repairRequestRepository.findByResolvedFalse()).thenReturn(List.of(r));

        List<RepairRequest> unresolved = repairRequestService.getUnresolvedRequests();
        assertEquals(1, unresolved.size());
        assertFalse(unresolved.get(0).isResolved());
    }

    @Test
    void testResolveRepair()
    {
        RepairRequest r = RepairRequest.builder().id(5L).resolved(false).build();
        when(repairRequestRepository.findById(5L)).thenReturn(Optional.of(r));
        when(repairRequestRepository.save(any())).thenReturn(r);

        repairRequestService.resolveRepair(5L);
        assertTrue(r.isResolved());
    }

    @Test
    void testGetRequestsByVehicle()
    {
        Vehicle vehicle = Vehicle.builder().id(10L).type("Flatbed").build();
        RepairRequest r = RepairRequest.builder().vehicle(vehicle).build();
        when(repairRequestRepository.findByVehicle(vehicle)).thenReturn(List.of(r));

        List<RepairRequest> list = repairRequestService.getRequestsByVehicle(vehicle);
        assertEquals(1, list.size());
        assertEquals("Flatbed", list.get(0).getVehicle().getType());
    }
}
