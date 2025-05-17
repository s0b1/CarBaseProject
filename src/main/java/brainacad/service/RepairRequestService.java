package brainacad.service;

import brainacad.model.RepairRequest;
import brainacad.model.Vehicle;
import brainacad.repository.RepairRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepairRequestService
{

    private final RepairRequestRepository repairRequestRepository;

    public RepairRequest createRepairRequest(Vehicle vehicle, String description)
    {
        RepairRequest request = RepairRequest.builder()
                .vehicle(vehicle)
                .description(description)
                .resolved(false)
                .build();
        return repairRequestRepository.save(request);
    }

    public List<RepairRequest> getUnresolvedRequests()
    {
        return repairRequestRepository.findByResolvedFalse();
    }

    public void resolveRepair(Long id)
    {
        Optional<RepairRequest> optional = repairRequestRepository.findById(id);
        optional.ifPresent(req ->
        {
            req.setResolved(true);
            repairRequestRepository.save(req);
        });
    }

    public List<RepairRequest> getRequestsByVehicle(Vehicle vehicle)
    {
        return repairRequestRepository.findByVehicle(vehicle);
    }
}
