package brainacad.repository;

import brainacad.model.RepairRequest;
import brainacad.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long>
{
    List<RepairRequest> findByResolvedFalse();
    List<RepairRequest> findByVehicle(Vehicle vehicle);
}
