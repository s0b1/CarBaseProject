package brainacad.service;

import brainacad.model.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SimulationService
{

    private final CargoRequestService cargoRequestService;
    private final DispatcherService dispatcherService;
    private final RouteService routeService;
    private final PaymentService paymentService;

    private final Random random = new Random();

    public void runSimulationCycle(int cargoCount, boolean autoPay)
    {
        System.out.println("\nStarting simulation cycle...");

        for (int i = 0; i < cargoCount; i++)
        {
            var request = cargoRequestService.generateRandomRequest();
            System.out.println("Generated cargo: " + request);
        }

        dispatcherService.assignPendingCargoRequests();

        List<Route> activeRoutes = routeService.getIncompleteRoutes();
        for (Route route : activeRoutes)
        {
            routeService.completeRoute(route.getId());

            if (autoPay) {
                BigDecimal payment = BigDecimal.valueOf(100 + random.nextInt(900)); // 100–1000
                paymentService.payDriver(route.getDriver(), route, payment);
            }
        }

        System.out.println("Simulation cycle completed.\n");
    }
}
