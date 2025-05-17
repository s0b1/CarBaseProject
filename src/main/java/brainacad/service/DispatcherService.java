package brainacad.service;

import brainacad.model.*;
import brainacad.repository.CargoRequestRepository;
import brainacad.repository.DriverRepository;
import brainacad.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import brainacad.util.DispatcherLogger;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DispatcherService
{

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final CargoRequestRepository cargoRequestRepository;
    private final RouteService routeService;

    public void assignPendingCargoRequests()
    {
        List<CargoRequest> allRequests = cargoRequestRepository.findAll();

        for (CargoRequest request : allRequests)
        {
            Optional<Driver> suitableDriver = driverRepository.findAll().stream()
                    .filter(Driver::isAvailable)
                    .filter(d -> d.getExperience() >= requiredExperienceForCargo(request.getCargoType()))
                    .findFirst();

            Optional<Vehicle> suitableVehicle = vehicleRepository.findAll().stream()
                    .filter(Vehicle::isAvailable)
                    .filter(v -> v.getLoadCapacity() >= request.getWeight())
                    .min(Comparator.comparingDouble(v -> v.getLoadCapacity() - request.getWeight()));

            if (suitableDriver.isPresent() && suitableVehicle.isPresent())
            {
                Driver driver = suitableDriver.get();
                Vehicle vehicle = suitableVehicle.get();

                driver.setAvailable(false);
                vehicle.setAvailable(false);
                driverRepository.save(driver);
                vehicleRepository.save(vehicle);

                routeService.assignRoute(driver, vehicle, request);


                System.out.printf("Assigned cargo '%s' to driver %s and vehicle %s%n",
                        request.getCargoType(), driver.getName(), vehicle.getType());

                DispatcherLogger.logAssignment(request, driver, vehicle);
            }
            else
            {
                String reason = suitableDriver.isEmpty() ? "No suitable driver"
                        : suitableVehicle.isEmpty() ? "No suitable vehicle"
                        : "Unknown reason";

                System.out.printf("No available driver/vehicle for cargo '%s': %s%n", request.getCargoType(), reason);

                DispatcherLogger.logFailure(request, reason);
            }

        }
    }

    private int requiredExperienceForCargo(String cargoType)
    {
        return switch (cargoType.toLowerCase())
        {
            case "fuel", "chemicals" -> 5;
            case "electronics" -> 2;
            default -> 1;
        };
    }
}
