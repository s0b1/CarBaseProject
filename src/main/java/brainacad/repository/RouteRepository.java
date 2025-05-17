package brainacad.repository;

import brainacad.model.Route;
import brainacad.model.Driver;
import brainacad.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>
{
    List<Route> findByCompletedFalse();

    List<Route> findByDriver(Driver driver);

    List<Route> findByVehicle(Vehicle vehicle);
}
