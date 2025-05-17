package brainacad.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class StatisticsExporter
{

    public static void exportMapToFile(Map<String, ?> data, String fileName, String title)
    {
        try (FileWriter writer = new FileWriter(fileName))
        {
            writer.write("=== " + title + " ===\n");
            data.forEach((key, value) ->
            {
                try {
                    writer.write(String.format("%s : %s%n", key, value));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Exported stats to " + fileName);
        } catch (IOException e) {
            System.err.println("Error exporting stats: " + e.getMessage());
        }
    }
}
