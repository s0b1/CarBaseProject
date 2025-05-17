package brainacad.util;

import brainacad.model.Route;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RouteLogger
{

    private static final String FILE_PATH = "completed_routes.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logCompletedRoute(Route route)
    {
        String logEntry = String.format(
                "[%s] Route ID: %d | Driver: %s | Vehicle: %s | Destination: %s | Cargo: %s (%.2f kg)%n",
                FORMATTER.format(LocalDateTime.now()),
                route.getId(),
                route.getDriver().getName(),
                route.getVehicle().getType(),
                route.getCargoRequest().getDestination(),
                route.getCargoRequest().getCargoType(),
                route.getCargoRequest().getWeight()
        );

        try (FileWriter fw = new FileWriter(FILE_PATH, true))
        {
            fw.write(logEntry);
        }
        catch (IOException e)
        {
            System.err.println("Failed to write route log: " + e.getMessage());
        }
    }
}
