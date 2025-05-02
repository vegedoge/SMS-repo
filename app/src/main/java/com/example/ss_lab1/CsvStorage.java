package com.example.ss_lab1;

import android.content.Context;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvStorage {
    private static final String ACC_FILE_NAME = "activity.csv";
    private static final String WIFI_FILE_NAME = "location.csv";
    private static final String CSV_HEADER = "label,features";

    public static void saveData(Context context, DataPoint data) {
        String filename = data.getType() == DataPoint.DataType.ACTIVITY ? ACC_FILE_NAME : WIFI_FILE_NAME;
        File file = new File(context.getFilesDir(), filename);

        try (FileWriter writer = new FileWriter(file, true)) { // append
            // check if new
            if (file.length() == 0) {
                writer.append(CSV_HEADER).append("\n");
            }

            // toString
            String featuresStr = Arrays.toString(data.getFeatures())
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "");

            writer.append(data.getLabel()).append(",");
            writer.append(featuresStr).append("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends DataPoint> List<T> loadData(Context context, Class<T> dataType) {
        String filename = (dataType == ActivityDataPoint.class) ? ACC_FILE_NAME : WIFI_FILE_NAME;
        List<T> dataPoints = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(new File(context.getFilesDir(), filename)))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // skip headline
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String label = parts[0];
                    double[] features = Arrays.stream(parts[1].split(","))
                            .mapToDouble(Double::parseDouble)
                            .toArray();

                    T dataPoint;
                    if (dataType == ActivityDataPoint.class) {
                        dataPoint = (T) new ActivityDataPoint(label, features);
                    } else {
                        dataPoint = (T) new LocationDataPoint(label, features);
                    }

                    dataPoints.add(dataPoint);
                }
            }
        } catch (IOException | NumberFormatException e) {
            // return empty list if error
        }
        return dataPoints;
    }
}