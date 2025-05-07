package com.example.ss_lab1;

import static com.example.ss_lab1.CsvStorage.saveWifiScanFeatures;
import static com.example.ss_lab1.CsvStorage.saveWifiScanResults;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Toast;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TrainFragment.ControlListener {
    // --- const values ---
    private static final double WIFI_DEFAULT_LOW_RSSI = -100.0;     // -100dbm is low enough
    private static final int K_VALUE_LOCATION = 3;                  // K value for location (wifi)
    private static final int K_VALUE_ACTIVITY = 3;                  // K value for activity (acc)
    private static final List<String> AP_LIST = Arrays.asList(      // pre defined wifi access point list
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
    private static final int REQUEST_LOCATION_PERMISSION = 1;


    // --- modes ---
    public enum Mode {TRAINING, DETECTION}
    private enum Location {C1, C2, C3, C4, X}
    private Mode curMode = Mode.DETECTION;              // default
    private Location curLocation = Location.X;      // label for rooms


    // --- acc  sensor ---
    private String curActivityLabel;    // label for activities
    private SensorHandler sensorHandler; // a handler which deals with moving sensor

    // --- wifi ---
    private WifiManager wifiManager;
    private BroadcastReceiver wifiScanReceiver;
    private boolean isScanning = false;
    private String curLocationLabel;
    private KNNClassifier wifiKNN;



    // --- data ---
    private final String[] states = {"Moving", "Still", "???"};

    // --- KNN objects ---
//    private KNNFilter activityFilter;
//    private KNNFilter locationFilter;

    // --- interface ---
    @Override
    public void launchWifiScan() {
        startWifiScan();
    }
    @Override
    public void launchAccScan() {
        startAccScan();
    }
    @Override
    public void onLocationSelected(String label) {
        curLocationLabel = label;
    }
    @Override
    public void onActivitySelected(String label) {
        curActivityLabel = label;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorHandler.register(); // start listening to the sensor event
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorHandler.unregister(); // stop listening to the sensor event
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get fragment
        showFragment(new DetectFragment());

        // init wifi and acc sensors, cast object to SensorManager
        initSensors();

        try {
            wifiKNN = new KNNClassifier(this, "wifi_train_model.json", K_VALUE_LOCATION);
        } catch(Exception e) {
            System.out.println("init KNN for wifi failed" + e);
        }


    }

    ///  UI page
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        if (fragment instanceof TrainFragment) {
            curMode =  Mode.TRAINING;
        } else {
            curMode =  Mode.DETECTION;
        }
    }

    /// permission check for sensors
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return false;
        }
        return true;
    }

    private void startWifiScan() {
        checkLocationPermission();
        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        float[] wifi_features = genWifiFeatures(scanResults, AP_LIST);
        processWifiScanResults(wifi_features);
    }

    private void startAccScan() {
        if (curMode == Mode.TRAINING) {
            sensorHandler.startTraining(curActivityLabel);
        } else {
            sensorHandler.startDetecting();
        }
    }

     private void initSensors() {
        // acc
         sensorHandler = new SensorHandler(this);

         // wifi
         wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
         if (wifiManager == null) {
             Toast.makeText(this, "failed to get wifi data", Toast.LENGTH_LONG).show();
             return;
         }
     }

     /// function to handle received wifi results
     private void processWifiScanResults(float[] features)  {
        // first we filter all the weak signals, and maybe put signals
        // lower than a threshold to -100dbm, not decided yet
//        double[] features = LocationDataPoint.genWifiFeatures(scanResults, AP_LIST);

        if(curMode == Mode.TRAINING) {
            if (curLocationLabel == null) {
                Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
                return;
            }
//            LocationDataPoint locationDP = new LocationDataPoint(curLocationLabel, features);
//            CsvStorage.saveData(this, locationDP);
//            saveWifiScanResults(getApplicationContext(), curLocationLabel, scanResults);
            Toast.makeText(this, "Starting wifi scan", Toast.LENGTH_SHORT).show();
            saveWifiScanFeatures(getApplicationContext(), curLocationLabel, features);
        } else {
            String result = wifiKNN.predict(features);
            Toast.makeText(this, "Location: " + result, Toast.LENGTH_SHORT).show();
        }
     }

    private static float[] genWifiFeatures (
            List<ScanResult> scannedAps,
            List<String> apList
    ) {
        float[] features = new float[apList.size()];
        for (int i = 0; i < apList.size(); i++) {
            // we try to match ids
            String targetBSSID = apList.get(i);
            features[i] = -100.0f;
            for (ScanResult result : scannedAps) {
                if (result.BSSID.equals(targetBSSID)) {
                    // default low value or the actual value
                    if (result.level > features[i]) {
                        features[i] = result.level;
                    }
                }
                // too low equals not found
            }
        }

        return features;
    }

     public SensorHandler getSensorHandler() {
         return this.sensorHandler;
     }
}