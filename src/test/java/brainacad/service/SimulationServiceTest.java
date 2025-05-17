package brainacad.service;

import brainacad.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

public class SimulationServiceTest
{

    @Mock
    private CargoRequestService cargoRequestService;

    @Mock
    private DispatcherService dispatcherService;

    @Mock
    private RouteService routeService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private SimulationService simulationService;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRunSimulationCycle_WithAutoPay()
    {
        CargoRequest cr = CargoRequest.builder().id(1L).cargoType("food").weight(400).destination("San Andreas").build();
        Route r1 = Route.builder()
                .id(1L)
                .completed(false)
                .cargoRequest(cr)
                .driver(Driver.builder().id(1L).name("Olivia").totalEarnings(BigDecimal.ZERO).build())
                .build();


        when(cargoRequestService.generateRandomRequest()).thenReturn(cr);
        when(routeService.getIncompleteRoutes()).thenReturn(List.of(r1));

        simulationService.runSimulationCycle(2, true); // 2 cargo, auto-pay

        verify(cargoRequestService, times(2)).generateRandomRequest();
        verify(dispatcherService, times(1)).assignPendingCargoRequests();
        verify(routeService, times(1)).completeRoute(r1.getId());
        verify(paymentService, times(1)).payDriver(eq(r1.getDriver()), eq(r1), any());
    }

    @Test
    void testRunSimulationCycle_WithoutAutoPay()
    {
        Route r = Route.builder().id(2L).completed(false).build();

        when(cargoRequestService.generateRandomRequest()).thenReturn(mock(CargoRequest.class));
        when(routeService.getIncompleteRoutes()).thenReturn(List.of(r));

        simulationService.runSimulationCycle(1, false); // No payment

        verify(routeService, times(1)).completeRoute(r.getId());
        verify(paymentService, never()).payDriver(any(), any(), any());
    }
}