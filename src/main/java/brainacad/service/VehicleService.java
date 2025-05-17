package brainacad.service;

import brainacad.model.Vehicle;
import brainacad.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VehicleService
{
    private final VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles()
    {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getAvailableVehicles()
    {
        return vehicleRepository.findByAvailableTrue();
    }

    public Optional<Vehicle> getVehicleById(Long id)
    {
        return vehicleRepository.findById(id);
    }

    public Vehicle saveVehicle(Vehicle vehicle)
    {
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id)
    {
        vehicleRepository.deleteById(id);
    }

    public Vehicle generateRandomVehicle()
    {
        String[] types = {"Van", "Box Truck", "Tanker", "Flatbed", "Mini Truck"};
        double[] capacities = {500, 1000, 2000, 1500, 750};

        int index = new Random().nextInt(types.length);

        Vehicle vehicle = Vehicle.builder()
                .type(types[index])
                .loadCapacity(capacities[index])
                .condition("GOOD")
                .available(true)
                .build();

        return saveVehicle(vehicle);
    }


}
