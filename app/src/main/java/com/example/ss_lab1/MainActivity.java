package com.example.ss_lab1;

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
            "d0",
            ""
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
        processWifiScanResults(scanResults);
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
     private void processWifiScanResults(List<ScanResult> scanResults)  {
        // first we filter all the weak signals, and maybe put signals
        // lower than a threshold to -100dbm, not decided yet
//        double[] features = LocationDataPoint.genWifiFeatures(scanResults, AP_LIST);

        if(curMode == Mode.TRAINING) {
            if (curLocationLabel == null) return;
//            LocationDataPoint locationDP = new LocationDataPoint(curLocationLabel, features);
//            CsvStorage.saveData(this, locationDP);
            saveWifiScanResults(getApplicationContext(), curLocationLabel, scanResults);
        } else {
//            List<LocationDataPoint> trainingData = CsvStorage.loadData(this, LocationDataPoint.class);
//            String predictedLabel = new KNNFilter(trainingData, K_VALUE_LOCATION).predict(features);

            // update curLocation
//            curLocation = Location.valueOf(predictedLabel);
        }
     }

}