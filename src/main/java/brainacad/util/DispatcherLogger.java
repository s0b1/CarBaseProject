package brainacad.util;

import brainacad.model.CargoRequest;
import brainacad.model.Driver;
import brainacad.model.Vehicle;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DispatcherLogger
{

    private static final String FILE_PATH = "dispatcher.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logAssignment(CargoRequest cargo, Driver driver, Vehicle vehicle)
    {
        String entry = String.format(
                "[%s] Assigned cargo '%s' (%.2f kg) to Driver '%s' and Vehicle '%s'%n",
                FORMATTER.format(LocalDateTime.now()),
                cargo.getCargoType(),
                cargo.getWeight(),
                driver.getName(),
                vehicle.getType()
        );
        writeToFile(entry);
    }

    public static void logFailure(CargoRequest cargo, String reason)
    {
        String entry = String.format(
                "[%s] Failed to assign cargo '%s' (%.2f kg) to destination '%s': %s%n",
                FORMATTER.format(LocalDateTime.now()),
                cargo.getCargoType(),
                cargo.getWeight(),
                cargo.getDestination(),
                reason
        );
        writeToFile(entry);
    }

    private static void writeToFile(String entry)
    {
        try (FileWriter fw = new FileWriter(FILE_PATH, true))
        {
            fw.write(entry);
        } catch (IOException e) {
            System.err.println("Failed to log dispatcher action: " + e.getMessage());
        }
    }
}
