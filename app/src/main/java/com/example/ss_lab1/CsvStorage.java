package com.example.ss_lab1;

import android.content.Context;
import android.net.wifi.ScanResult;

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

    ///  saves as one sample one line csv
    public static void saveWifiScanResults(Context context, String label, List<ScanResult> scanResults) {
        final String WIFI_FILE_NAME = "wifi_scan_results.csv";
        final String CSV_HEADER = "label,mac_address,rssi";

        File file = new File(context.getFilesDir(), WIFI_FILE_NAME);

        try (FileWriter writer = new FileWriter(file, true)) {
            if (file.length() == 0) {
                writer.append(CSV_HEADER).append("\n");
            }

            // iteration
            for (ScanResult result : scanResults) {
                writer.append(label).append(",");
                writer.append(result.BSSID).append(",");      // MAC
                writer.append(String.valueOf(result.level)).append(",");  // (dBm)
                writer.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///  save acc sensor data into csv
    /// format is [label, meanX, meanY, meanZ, rangeX, rangeY, rangeZ, meanMag]
    public static void saveAccResults(Context context, String label, float[] features) {
        final String ACC_FILE_NAME = "acc_scan_results.csv";
        final String CSV_HEADER = "label,meanX,meanY,meanZ,rangeX,rangeY,rangeZ,meanMag";
        File file = new File(context.getFilesDir(), ACC_FILE_NAME);

        try (FileWriter writer = new FileWriter(file, true)) {
            if (file.length() == 0) {
                writer.append(CSV_HEADER).append("\n");
            }
            // write the data with the format
            writer.append(label);
            for (float feature : features) {
                writer.append(",").append(String.valueOf(feature));
            }
            writer.append("\n");
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