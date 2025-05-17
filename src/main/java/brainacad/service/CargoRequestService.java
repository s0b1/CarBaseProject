package brainacad.service;

import brainacad.model.CargoRequest;
import brainacad.repository.CargoRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CargoRequestService
{

    private final CargoRequestRepository cargoRequestRepository;

    public List<CargoRequest> getAllRequests()
    {
        return cargoRequestRepository.findAll();
    }

    public Optional<CargoRequest> getRequestById(Long id)
    {
        return cargoRequestRepository.findById(id);
    }

    public CargoRequest saveRequest(CargoRequest request)
    {
        return cargoRequestRepository.save(request);
    }

    public void deleteRequest(Long id)
    {
        cargoRequestRepository.deleteById(id);
    }

    public List<CargoRequest> findByDestination(String destination)
    {
        return cargoRequestRepository.findByDestinationIgnoreCase(destination);
    }

    public List<CargoRequest> findByType(String type)
    {
        return cargoRequestRepository.findByCargoTypeIgnoreCase(type);
    }

    public List<CargoRequest> findHeavy(double weightLimit)
    {
        return cargoRequestRepository.findByWeightGreaterThan(weightLimit);
    }

    public CargoRequest generateRandomRequest()
    {
        String[] destinations = {"Lost Heaven", "Empire Bay", "Liberty City", "Alderney", "New Bordeaux"};
        String[] types = {"electronics", "furniture", "fuel", "chemicals", "food"};

        String destination = destinations[new Random().nextInt(destinations.length)];
        String cargoType = types[new Random().nextInt(types.length)];

        double weight = 50 + (Math.random() * 950); // 50kg to 1000kg

        CargoRequest request = CargoRequest.builder()
                .destination(destination)
                .cargoType(cargoType)
                .weight(weight)
                .build();

        return saveRequest(request);
    }

}
