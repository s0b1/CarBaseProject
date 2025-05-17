package brainacad.repository;

import brainacad.model.CargoRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoRequestRepository extends JpaRepository<CargoRequest, Long>
{
    List<CargoRequest> findByDestinationIgnoreCase(String destination);

    List<CargoRequest> findByCargoTypeIgnoreCase(String cargoType);

    List<CargoRequest> findByWeightGreaterThan(double minWeight);
}
