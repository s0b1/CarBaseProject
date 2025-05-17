package brainacad.service;

import brainacad.model.CargoRequest;
import brainacad.model.Driver;
import brainacad.model.Route;
import brainacad.model.Vehicle;
import brainacad.model.RepairRequest;
import brainacad.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import brainacad.util.RouteLogger;
import brainacad.repository.*;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteService
{
    private final RouteRepository routeRepository;
    private final VehicleRepository vehicleRepository;
    private final RepairRequestRepository repairRequestRepository;

    public Route assignRoute(Driver driver, Vehicle vehicle, CargoRequest cargo)
    {
        Route route = Route.builder()
                .driver(driver)
                .vehicle(vehicle)
                .cargoRequest(cargo)
                .completed(false)
                .build();
        return routeRepository.save(route);
    }

    public List<Route> getAllRoutes()
    {
        return routeRepository.findAll();
    }

    public List<Route> getIncompleteRoutes()
    {
        return routeRepository.findByCompletedFalse();
    }

    public Optional<Route> getRouteById(Long id)
    {
        return routeRepository.findById(id);
    }

    private int completedRouteCount = 0;

    public void completeRoute(Long routeId) {
        Optional<Route> optional = routeRepository.findById(routeId);
        optional.ifPresent(route -> {
            route.setCompleted(true);
            routeRepository.save(route);
            RouteLogger.logCompletedRoute(route);

            completedRouteCount++;
            if (completedRouteCount % 3 == 0) { // Every 3rd completion
                simulateBreakdown(route.getVehicle());
            }
        });
    }

    private void simulateBreakdown(Vehicle vehicle)
    {
        vehicle.setAvailable(false);
        vehicle.setCondition("BROKEN");
        vehicleRepository.save(vehicle);

        RepairRequest request = RepairRequest.builder()
                .vehicle(vehicle)
                .description("Simulated breakdown after route completion.")
                .resolved(false)
                .build();

        repairRequestRepository.save(request);
        System.out.println("Vehicle '" + vehicle.getType() + "' broke down after completing a route.");
    }

}
