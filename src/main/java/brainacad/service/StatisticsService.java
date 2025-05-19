package brainacad.service;

import brainacad.model.CargoRequest;
import brainacad.model.Driver;
import brainacad.model.Payment;
import brainacad.model.Route;
import brainacad.repository.CargoRequestRepository;
import brainacad.repository.DriverRepository;
import brainacad.repository.PaymentRepository;
import brainacad.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService
{

    private final DriverRepository driverRepository;
    private final PaymentRepository paymentRepository;
    private final RouteRepository routeRepository;
    private final CargoRequestRepository cargoRequestRepository;

    public Optional<Driver> getTopEarningDriver()
    {
        return driverRepository.findAll().stream()
                .filter(d -> d.getTotalEarnings() != null)
                .max(Comparator.comparing(Driver::getTotalEarnings));
    }

    public Map<String, Double> getCargoWeightPerDestination()
    {
        List<Route> routes = routeRepository.findAll();

        Map<String, Double> destinationTotals = new HashMap<>();
        for (Route route : routes)
        {
            if (route.isCompleted())
            {
                CargoRequest cargo = route.getCargoRequest();
                String dest = cargo.getDestination();
                destinationTotals.merge(dest, cargo.getWeight(), Double::sum);
            }
        }
        return destinationTotals;
    }

    public Map<String, Double> getCargoWeightPerDriver()
    {
        List<Route> routes = routeRepository.findAll();

        Map<String, Double> driverTotals = new HashMap<>();
        for (Route route : routes) {
            if (route.isCompleted())
            {
                String driverName = route.getDriver().getName();
                driverTotals.merge(driverName, route.getCargoRequest().getWeight(), Double::sum);
            }
        }
        return driverTotals;
    }

    public Map<String, String> getEarningsPerDriver()
    {
        return driverRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Driver::getName,
                        d -> d.getTotalEarnings() != null ? d.getTotalEarnings().toString() : "0.0"
                ));
    }

}
