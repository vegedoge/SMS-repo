package com.example.ss_lab1;

import android.content.Context;
import android.net.wifi.ScanResult;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvStorage {
    private static final String ACC_FILE_NAME = "activity.csv";
    private static final String WIFI_FILE_NAME = "location.csv";
    private static final String CSV_HEADER = "label,features";
    private static final List<String> INTERESTED_MACS = Arrays.asList(
            "1c:28:af:61:df:00",
            "1c:28:af:61:df:01",
            "1c:28:af:61:df:02",
            "1c:28:af:61:df:10",
            "1c:28:af:61:df:11",
            "1c:28:af:61:df:12",
            "1c:28:af:61:e5:c0",
            "1c:28:af:61:e5:c1",
            "1c:28:af:61:e5:c2",
            "1c:28:af:61:e5:d0",
            "1c:28:af:61:e5:d1",
            "1c:28:af:61:e5:d2",
            "1c:28:af:61:e7:c0",
            "1c:28:af:61:e7:c1",
            "1c:28:af:61:e7:c2",
            "1c:28:af:61:e7:d1",
            "1c:28:af:61:ec:40",
            "1c:28:af:61:ec:41",
            "1c:28:af:61:ec:42",
            "1c:28:af:61:ec:a0",
            "1c:28:af:61:ec:a1",
            "1c:28:af:61:ec:a2",
            "1c:28:af:61:ec:b0",
            "1c:28:af:61:ec:b1",
            "1c:28:af:61:ec:b2",
            "1c:28:af:61:ef:c0",
            "1c:28:af:61:ef:c1",
            "1c:28:af:61:ef:c2",
            "1c:28:af:61:ef:d0",
            "1c:28:af:61:ef:d1",
            "1c:28:af:61:ef:d2",
            "1c:28:af:61:f9:00",
            "1c:28:af:61:f9:01",
            "1c:28:af:61:f9:02",
            "1c:28:af:61:f9:10",
            "1c:28:af:61:f9:11",
            "1c:28:af:61:f9:12",
            "1c:28:af:61:fc:40",
            "1c:28:af:61:fc:41",
            "1c:28:af:61:fc:42",
            "1c:28:af:62:04:a0",
            "1c:28:af:62:04:a1",
            "1c:28:af:62:04:a2",
            "1c:28:af:62:04:b0",
            "1c:28:af:62:04:b1",
            "1c:28:af:62:04:b2",
            "1c:28:af:62:42:a0",
            "1c:28:af:62:42:a1",
            "1c:28:af:62:42:a2",
            "1c:28:af:62:42:b0",
            "1c:28:af:62:42:b1",
            "1c:28:af:62:42:b2",
            "1c:28:af:62:4f:80",
            "1c:28:af:62:4f:81",
            "1c:28:af:62:4f:82",
            "1c:28:af:62:64:40",
            "1c:28:af:62:64:41",
            "1c:28:af:62:64:42",
            "1c:28:af:62:64:50",
            "1c:28:af:62:64:51",
            "1c:28:af:62:64:52",
            "1c:28:af:62:7c:c0",
            "1c:28:af:62:7c:c2",
            "1c:28:af:62:7c:d0",
            "1c:28:af:62:7c:d1",
            "1c:28:af:62:7c:d2",
            "1c:28:af:62:b1:00",
            "1c:28:af:62:b1:01",
            "1c:28:af:62:b1:02",
            "1c:28:af:62:b1:10",
            "1c:28:af:62:b1:11",
            "1c:28:af:62:b1:12",
            "1c:28:af:65:ba:40",
            "1c:28:af:65:ba:41",
            "1c:28:af:65:ba:42",
            "1c:28:af:65:ba:50",
            "1c:28:af:65:ba:51",
            "1c:28:af:65:ba:52",
            "1c:28:af:65:ce:20",
            "1c:28:af:65:ce:31",
            "1c:28:af:65:ce:32",
            "1c:28:af:65:d1:60",
            "1c:28:af:65:d1:61",
            "1c:28:af:65:d1:62",
            "1c:28:af:65:e6:e0",
            "1c:28:af:65:e6:e1",
            "1c:28:af:65:e6:e2",
            "1c:28:af:65:e6:f0",
            "1c:28:af:65:e6:f1",
            "1c:28:af:65:e6:f2",
            "1c:28:af:65:fd:c0",
            "1c:28:af:65:fd:c1",
            "1c:28:af:65:fd:c2",
            "1c:28:af:65:fd:d0",
            "1c:28:af:65:fd:d1",
            "1c:28:af:65:fd:d2",
            "1c:28:af:65:fe:a0",
            "1c:28:af:65:fe:a1",
            "1c:28:af:65:fe:a2",
            "1c:28:af:65:fe:b0",
            "1c:28:af:65:fe:b1",
            "1c:28:af:65:fe:b2",
            "50:88:11:a2:45:64",
            "d0:4d:c6:f1:5d:60",
            "d0:4d:c6:f1:5d:61",
            "d0:4d:c6:f1:5d:62",
            "d0:4d:c6:f1:9f:a0",
            "d0:4d:c6:f1:9f:a1",
            "d0:4d:c6:f1:9f:a2",
            "d0:4d:c6:f2:08:20",
            "d0:4d:c6:f2:08:21",
            "d0:4d:c6:f2:08:22",
            "d0:4d:c6:f2:08:30",
            "d0:4d:c6:f2:08:31",
            "d0:4d:c6:f2:08:32",
            "d0:4d:c6:f2:2a:e0",
            "d0:4d:c6:f2:2a:e1",
            "d0:4d:c6:f2:2a:e2",
            "d0:4d:c6:f2:2a:f0",
            "d0:4d:c6:f2:2a:f1",
            "d0:4d:c6:f2:2a:f2",
            "d0:4d:c6:f2:33:c0",
            "d0:4d:c6:f2:33:c1",
            "d0:4d:c6:f2:33:c2",
            "d0:4d:c6:f2:33:d2",
            "d0:4d:c6:f2:34:e0",
            "d0:4d:c6:f2:34:e1",
            "d0:4d:c6:f2:34:e2",
            "d0:4d:c6:f2:34:f0",
            "d0:4d:c6:f2:34:f1",
            "d0:4d:c6:f2:34:f2",
            "d0:4d:c6:f2:36:a0",
            "d0:4d:c6:f2:36:a1",
            "d0:4d:c6:f2:36:a2",
            "d0:4d:c6:f2:36:b0",
            "d0:4d:c6:f2:36:b1",
            "d0:4d:c6:f2:36:b2",
            "d0:4d:c6:f2:3d:40",
            "d0:4d:c6:f2:3d:41",
            "d0:4d:c6:f2:3d:42",
            "d0:4d:c6:f2:43:60",
            "d0:4d:c6:f2:43:61",
            "d0:4d:c6:f2:43:62",
            "d0:4d:c6:f2:43:70",
            "d0:4d:c6:f2:43:71",
            "d0:4d:c6:f2:43:72",
            "d0:4d:c6:f2:43:a0",
            "d0:4d:c6:f2:43:a1",
            "d0:4d:c6:f2:43:a2",
            "d0:4d:c6:f2:43:b0",
            "d0:4d:c6:f2:43:b1",
            "d0:4d:c6:f2:43:b2",
            "d0:4d:c6:f2:49:00",
            "d0:4d:c6:f2:49:01",
            "d0:4d:c6:f2:49:02",
            "d0:4d:c6:f2:49:10",
            "d0:4d:c6:f2:49:11",
            "d0:4d:c6:f2:49:12",
            "d0:4d:c6:f2:50:80",
            "d0:4d:c6:f2:50:81",
            "d0:4d:c6:f2:50:82",
            "d0:4d:c6:f2:50:90",
            "d0:4d:c6:f2:50:91",
            "d0:4d:c6:f2:50:92",
            "d0:4d:c6:f2:5a:40",
            "d0:4d:c6:f2:5a:41",
            "d0:4d:c6:f2:5a:42",
            "d0:4d:c6:f2:5d:40",
            "d0:4d:c6:f2:5d:41",
            "d0:4d:c6:f2:5d:42",
            "d0:4d:c6:f2:5d:50",
            "d0:4d:c6:f2:5d:51",
            "d0:4d:c6:f2:5d:52",
            "d0:4d:c6:f2:74:e0",
            "d0:4d:c6:f2:74:e1",
            "d0:4d:c6:f2:74:e2",
            "d0:4d:c6:f2:81:00",
            "d0:4d:c6:f2:81:01",
            "d0:4d:c6:f2:81:02",
            "d0:4d:c6:f2:81:10",
            "d0:4d:c6:f2:81:11",
            "d0:4d:c6:f2:81:12",
            "d0:4d:c6:f2:a8:00",
            "d0:4d:c6:f2:a8:01",
            "d0:4d:c6:f2:a8:02",
            "d0:4d:c6:f2:a8:10",
            "d0:4d:c6:f2:a8:11",
            "d0:4d:c6:f2:a8:12",
            "d0:4d:c6:f2:ae:b0",
            "d0:4d:c6:f2:bb:80",
            "d0:4d:c6:f2:bb:81",
            "d0:4d:c6:f2:bb:82",
            "d0:4d:c6:f2:e7:60",
            "d0:4d:c6:f2:e7:61",
            "d0:4d:c6:f2:e7:62",
            "d0:4d:c6:f2:ed:50",
            "d0:4d:c6:f2:ed:51",
            "d0:4d:c6:f2:ed:52",
            "d0:4d:c6:f2:f1:00",
            "d0:4d:c6:f2:f1:01",
            "d0:4d:c6:f2:f1:02",
            "d0:4d:c6:f2:f1:10",
            "d0:4d:c6:f2:f1:11",
            "d0:4d:c6:f2:f1:12",
            "d0:4d:c6:f2:f9:c0",
            "d0:4d:c6:f2:f9:c1",
            "d0:4d:c6:f2:f9:c2",
            "d0:4d:c6:f2:f9:d0",
            "d0:4d:c6:f2:f9:d1",
            "d0:4d:c6:f2:f9:d2",
            "d0:4d:c6:f2:fc:80",
            "d0:4d:c6:f2:fc:81",
            "d0:4d:c6:f2:fc:82",
            "d0:4d:c6:f2:fc:90",
            "d0:4d:c6:f2:fc:91",
            "d0:4d:c6:f2:fc:92",
            "d0:4d:c6:f3:02:40",
            "d0:4d:c6:f3:02:41",
            "d0:4d:c6:f3:02:42",
            "d0:4d:c6:f3:03:a0",
            "d0:4d:c6:f3:03:a1",
            "d0:4d:c6:f3:03:a2",
            "d0:4d:c6:f3:03:b0",
            "d0:4d:c6:f3:03:b1",
            "d0:4d:c6:f3:03:b2",
            "d0:4d:c6:f3:15:c0",
            "d0:4d:c6:f3:15:c1",
            "d0:4d:c6:f3:15:c2",
            "d0:4d:c6:f3:15:d0",
            "d0:4d:c6:f3:15:d1",
            "d0:4d:c6:f3:15:d2",
            "f8:e9:03:ca:c0:38"
    );




    public static void saveWifiScanFeatures(Context context, String label, float[] features) {
        final String WIFI_FILE_NAME = "wifi_scan_features.csv";

        File file = new File(context.getFilesDir(), WIFI_FILE_NAME);

        try (FileWriter writer = new FileWriter(file, true)) {

            if (file.length() == 0) {
                List<String> headers = new ArrayList<>();
                headers.add("label");
                headers.addAll(INTERESTED_MACS);
                writer.append(String.join(",", headers)).append("\n");
            }

//            writer.append(String.join(",", record)).append("\n");
            for(float feature: features){
                writer.append(String.valueOf(feature)).append(",");
            }
            writer.append("\n");

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
    /// format is [,label, meanX, meanY, meanZ, rangeX, rangeY, rangeZ, meanMag]
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

}