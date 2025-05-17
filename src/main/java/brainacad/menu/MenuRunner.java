package brainacad.menu;

import brainacad.model.Driver;
import brainacad.service.DriverService;
import brainacad.model.Vehicle;
import brainacad.service.VehicleService;
import brainacad.model.CargoRequest;
import brainacad.service.CargoRequestService;
import brainacad.model.Route;
import brainacad.service.RouteService;
import brainacad.model.RepairRequest;
import brainacad.service.RepairRequestService;
import brainacad.model.Payment;
import brainacad.service.PaymentService;
import brainacad.service.StatisticsService;
import brainacad.service.DispatcherService;
import brainacad.service.SimulationService;


import brainacad.util.RouteLogger;
import brainacad.util.DispatcherLogger;
import brainacad.util.StatisticsExporter;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Scanner;
import java.util.Optional;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class MenuRunner implements CommandLineRunner
{

    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final CargoRequestService cargoRequestService;
    private final RouteService routeService;
    private final RepairRequestService repairRequestService;
    private final PaymentService paymentService;
    private final StatisticsService statisticsService;
    private final DispatcherService dispatcherService;
    private final SimulationService simulationService;

    @Override
    public void run(String... args)
    {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n CARBASE MENU ");
            System.out.println("1. Show all drivers");
            System.out.println("2. Show available drivers");
            System.out.println("3. Add a new driver");
            System.out.println("4. Delete a driver");
            System.out.println("5. Show all vehicles");
            System.out.println("6. Show available vehicles");
            System.out.println("7. Add a new vehicle");
            System.out.println("8. Delete a vehicle");
            System.out.println("9. Show all cargo requests");
            System.out.println("10. Add a cargo request");
            System.out.println("11. Delete a cargo request");
            System.out.println("12. Find cargo requests by destination");
            System.out.println("13. Find cargo requests heavier than weight");
            System.out.println("14. Show all routes");
            System.out.println("15. Assign a route");
            System.out.println("16. Mark a route as completed");
            System.out.println("17. Simulate vehicle breakdown");
            System.out.println("18. Show unresolved repairs");
            System.out.println("19. Mark repair as resolved");
            System.out.println("20. Pay driver for a route");
            System.out.println("21. Show all payments");
            System.out.println("22. Show payments for a driver");
            System.out.println("23. Show top-earning driver");
            System.out.println("24. Show total cargo per destination");
            System.out.println("25. Show total cargo per driver");
            System.out.println("26. Show total earnings per driver");
            System.out.println("27. Run dispatcher to assign pending cargo");
            System.out.println("28. Generate random cargo requests");
            System.out.println("29. Export cargo per destination to file");
            System.out.println("30. Generate random drivers");
            System.out.println("31. Generate random vehicles");
            System.out.println("32. Run automated simulation cycle");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option)
            {
                case 1 ->
                {
                    List<Driver> all = driverService.getAllDrivers();
                    all.forEach(System.out::println);
                }
                case 2 ->
                {
                    List<Driver> available = driverService.getAvailableDrivers();
                    available.forEach(System.out::println);
                }
                case 3 ->
                {
                    System.out.print("Enter driver name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter experience (years): ");
                    int exp = scanner.nextInt();

                    Driver newDriver = Driver.builder()
                            .name(name)
                            .experience(exp)
                            .available(true)
                            .build();

                    driverService.saveDriver(newDriver);
                    System.out.println("Driver saved.");
                }
                case 4 ->
                {
                    System.out.print("Enter driver ID to delete: ");
                    Long id = scanner.nextLong();
                    driverService.deleteDriver(id);
                    System.out.println("Driver deleted.");
                }
                case 5 ->
                {
                    List<Vehicle> all = vehicleService.getAllVehicles();
                    all.forEach(System.out::println);
                }
                case 6 ->
                {
                    List<Vehicle> available = vehicleService.getAvailableVehicles();
                    available.forEach(System.out::println);
                }
                case 7 ->
                {
                    System.out.print("Enter vehicle type: ");
                    String type = scanner.nextLine();

                    System.out.print("Enter load capacity: ");
                    double capacity = scanner.nextDouble();
                    scanner.nextLine(); // clear newline

                    System.out.print("Enter condition (e.g. GOOD, BROKEN): ");
                    String condition = scanner.nextLine();

                    Vehicle newVehicle = Vehicle.builder()
                            .type(type)
                            .loadCapacity(capacity)
                            .condition(condition)
                            .available(true)
                            .build();

                    vehicleService.saveVehicle(newVehicle);
                    System.out.println("Vehicle saved.");
                }
                case 8 ->
                {
                    System.out.print("Enter vehicle ID to delete: ");
                    Long id = scanner.nextLong();
                    vehicleService.deleteVehicle(id);
                    System.out.println("Vehicle deleted.");
                }
                case 9 ->
                {
                    List<CargoRequest> all = cargoRequestService.getAllRequests();
                    all.forEach(System.out::println);
                }
                case 10 ->
                {
                    System.out.print("Enter destination: ");
                    String destination = scanner.nextLine();

                    System.out.print("Enter cargo type: ");
                    String type = scanner.nextLine();

                    System.out.print("Enter weight: ");
                    double weight = scanner.nextDouble();
                    scanner.nextLine(); // clear buffer

                    CargoRequest request = CargoRequest.builder()
                            .destination(destination)
                            .cargoType(type)
                            .weight(weight)
                            .build();

                    cargoRequestService.saveRequest(request);
                    System.out.println("Cargo request saved.");
                }
                case 11 ->
                {
                    System.out.print("Enter cargo request ID to delete: ");
                    Long id = scanner.nextLong();
                    cargoRequestService.deleteRequest(id);
                    System.out.println("Cargo request deleted.");
                }
                case 12 ->
                {
                    System.out.print("Enter destination: ");
                    String dest = scanner.nextLine();
                    cargoRequestService.findByDestination(dest).forEach(System.out::println);
                }
                case 13 ->
                {
                    System.out.print("Enter weight limit: ");
                    double limit = scanner.nextDouble();
                    scanner.nextLine();
                    cargoRequestService.findHeavy(limit).forEach(System.out::println);
                }
                case 14 -> {
                    List<Route> routes = routeService.getAllRoutes();
                    routes.forEach(System.out::println);
                }
                case 15 -> {
                    System.out.print("Enter driver ID: ");
                    Long driverId = scanner.nextLong();
                    System.out.print("Enter vehicle ID: ");
                    Long vehicleId = scanner.nextLong();
                    System.out.print("Enter cargo request ID: ");
                    Long cargoId = scanner.nextLong();
                    scanner.nextLine(); // clear buffer

                    Optional<Driver> driver = driverService.getDriverById(driverId);
                    Optional<Vehicle> vehicle = vehicleService.getVehicleById(vehicleId);
                    Optional<CargoRequest> cargo = cargoRequestService.getRequestById(cargoId);

                    if (driver.isPresent() && vehicle.isPresent() && cargo.isPresent()) {
                        routeService.assignRoute(driver.get(), vehicle.get(), cargo.get());
                        System.out.println("Route assigned.");
                    } else {
                        System.out.println("Invalid IDs provided.");
                    }
                }
                case 16 ->
                {
                    System.out.print("Enter route ID to complete: ");
                    Long routeId = scanner.nextLong();
                    scanner.nextLine();
                    routeService.completeRoute(routeId);
                    System.out.println("Route marked as completed.");
                }
                case 17 ->
                {
                    System.out.print("Enter vehicle ID: ");
                    Long vehicleId = scanner.nextLong();
                    scanner.nextLine();

                    Optional<Vehicle> vehicleOpt = vehicleService.getVehicleById(vehicleId);
                    if (vehicleOpt.isPresent()) {
                        Vehicle vehicle = vehicleOpt.get();

                        System.out.print("Enter breakdown description: ");
                        String description = scanner.nextLine();

                        vehicle.setAvailable(false);
                        vehicle.setCondition("BROKEN");
                        vehicleService.saveVehicle(vehicle);

                        repairRequestService.createRepairRequest(vehicle, description);
                        System.out.println("Repair request submitted.");
                    }
                    else
                    {
                        System.out.println("Vehicle not found.");
                    }
                }
                case 18 ->
                {
                    List<RepairRequest> unresolved = repairRequestService.getUnresolvedRequests();
                    unresolved.forEach(System.out::println);
                }
                case 19 ->
                {
                    System.out.print("Enter repair request ID to resolve: ");
                    Long repairId = scanner.nextLong();
                    scanner.nextLine();

                    repairRequestService.resolveRepair(repairId);
                    System.out.println("Repair marked as resolved.");
                }
                case 20 ->
                {
                    System.out.print("Enter driver ID: ");
                    Long driverId = scanner.nextLong();
                    System.out.print("Enter route ID: ");
                    Long routeId = scanner.nextLong();
                    System.out.print("Enter payment amount: ");
                    BigDecimal amount = scanner.nextBigDecimal();
                    scanner.nextLine();

                    var driverOpt = driverService.getDriverById(driverId);
                    var routeOpt = routeService.getRouteById(routeId);

                    if (driverOpt.isPresent() && routeOpt.isPresent())
                    {
                        paymentService.payDriver(driverOpt.get(), routeOpt.get(), amount);
                        System.out.println("Payment recorded and total earnings updated.");
                    }
                    else
                    {
                        System.out.println("Invalid driver or route ID.");
                    }
                }
                case 21 ->
                {
                    paymentService.getAllPayments().forEach(System.out::println);
                }
                case 22 ->
                {
                    System.out.print("Enter driver ID: ");
                    Long driverId = scanner.nextLong();
                    scanner.nextLine();

                    var driverOpt = driverService.getDriverById(driverId);
                    driverOpt.ifPresentOrElse(
                            d -> paymentService.getPaymentsForDriver(d).forEach(System.out::println),
                            () -> System.out.println("Driver not found.")
                    );
                }
                case 23 ->
                {
                    statisticsService.getTopEarningDriver().ifPresentOrElse(
                            driver -> System.out.printf("Top earner: %s (%.2f)%n",
                                    driver.getName(), driver.getTotalEarnings()),
                            () -> System.out.println("No data available.")
                    );
                }
                case 24 ->
                {
                    var map = statisticsService.getCargoWeightPerDestination();
                    map.forEach((dest, weight) ->
                            System.out.printf("Destination: %s | Total cargo: %.2f kg%n", dest, weight));
                }
                case 25 ->
                {
                    var map = statisticsService.getCargoWeightPerDriver();
                    map.forEach((driver, weight) ->
                            System.out.printf("Driver: %s | Total cargo: %.2f kg%n", driver, weight));
                }
                case 26 ->
                {
                    var map = statisticsService.getEarningsPerDriver();
                    map.forEach((driver, total) ->
                            System.out.printf("Driver: %s | Total earnings: %.2f%n", driver, total));
                }
                case 27 ->
                {
                    dispatcherService.assignPendingCargoRequests();
                }
                case 28 ->
                {
                    System.out.print("How many requests to generate? ");
                    int count = scanner.nextInt();
                    scanner.nextLine();

                    for (int i = 0; i < count; i++)
                    {
                        CargoRequest created = cargoRequestService.generateRandomRequest();
                        System.out.println("Generated: " + created);
                    }
                }
                case 29 ->
                {
                    var data = statisticsService.getCargoWeightPerDestination();
                    StatisticsExporter.exportMapToFile(data, "cargo_by_destination.txt", "Cargo per Destination");
                }
                case 30 -> {
                    System.out.print("How many drivers to generate? ");
                    int count = scanner.nextInt();
                    scanner.nextLine();

                    for (int i = 0; i < count; i++) {
                        Driver d = driverService.generateRandomDriver();
                        System.out.println("Generated: " + d);
                    }
                }
                case 31 -> {
                    System.out.print("How many vehicles to generate? ");
                    int count = scanner.nextInt();
                    scanner.nextLine();

                    for (int i = 0; i < count; i++) {
                        Vehicle v = vehicleService.generateRandomVehicle();
                        System.out.println("Generated: " + v);
                    }
                }
                case 32 ->
                {
                    System.out.print("How many cargo requests to generate? ");
                    int count = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Auto-pay drivers? (y/n): ");
                    boolean autoPay = scanner.nextLine().equalsIgnoreCase("y");

                    simulationService.runSimulationCycle(count, autoPay);
                }
                case 0 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }

        System.out.println("Exiting menu...");


    }
}
