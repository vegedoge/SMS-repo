package com.example.ss_lab1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // --- const values ---
    private static final double WIFI_DEFAULT_LOW_RSSI = -100.0;     // -100dbm is low enough
    private static final int K_VALUE_LOCATION = 3;                  // K value for location (wifi)
    private static final int K_VALUE_ACTIVITY = 3;                  // K value for activity (acc)
    private static final List<String> AP_LIST = Arrays.asList(      // pre defined wifi access point list
            "aa:bb:cc:dd:ee:ff"
    );


    // --- modes ---
    private enum Mode {TRAINING, DETECTION}
    private enum Location {C1, C2, C3, C4, X}
    private Mode curMode = Mode.DETECTION;              // default
    private Location curLocation = Location.X;      // label for rooms

    // --- UI ---
    private Button detectButton;
    private TextView resultText;
    private Button modeSwitchButton;
    private TextView modeStatusText;

    // --- acc  sensor ---
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener accelerometerListener;
    private String curActivityLabel;    // label for activities

    // --- wifi ---
    private WifiManager wifiManager;
    private BroadcastReceiver wifiScanReceiver;
    private boolean isScanning = false;



    // --- data ---
    private final String[] states = {"Moving", "Still", "???"};

    // --- KNN objects ---
    private KNNFilter activityFilter;
    private KNNFilter locationFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind all UIs
        detectButton = findViewById(R.id.detectButton);
        resultText = findViewById(R.id.result_text);
        modeSwitchButton = findViewById(R.id.mode_switch_btn);
        modeStatusText = findViewById(R.id.mode_status_txt);

        // init wifi and acc sensors, cast object to SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            Toast.makeText(this, "failed to get sensor data", Toast.LENGTH_LONG).show();
        }


        // Button logic
        /// click to switch mode
        modeSwitchButton.setOnClickListener(v -> {
            if (curMode == Mode.TRAINING) {
                curMode = Mode.DETECTION;
            } else {
                curMode = Mode.TRAINING;
            }
            updateModeUI();
        });

        ///  click to strat detect
        detectButton.setOnClickListener(v -> {


        });
    }

    /// current mode ui update function
    private void updateModeUI() {
        if (curMode == Mode.TRAINING) {
            modeStatusText.setText(R.string.mode_training);
        } else {
            modeStatusText.setText(R.string.mode_detection);
        }
    }

    ///  current location ui update function
    private void updateLocationUI() {
        switch (curLocation) {
            case C1:
                resultText.setText(R.string.room_C1);
                break;
            case C2:
                resultText.setText(R.string.room_C2);
                break;
            case C3:
                resultText.setText(R.string.room_C3);
                break;
            case C4:
                resultText.setText(R.string.room_C4);
                break;
            case X:
                resultText.setText(R.string.room_unknown);
                break;
            default:
                resultText.setText(R.string.room_unknown);
        }
        
    }

    /// permission check for sensors
    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return false;
        }
        return true;
    }

     private void initSensors() {
        // acc
         sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
         if (sensorManager != null) {
             accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
             if (accelerometer == null) {
                 Toast.makeText(this, "failed to get acc data", Toast.LENGTH_LONG).show();
             } else {
                 // delay registered as 50Hz
                 sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
             }
         } else {
             Toast.makeText(this, "failed to get sensor data", Toast.LENGTH_LONG).show();
         }

         // wifi
        //  wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
         wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
         if (wifiManager == null) {
             Toast.makeText(this, "failed to get wifi data", Toast.LENGTH_LONG).show();
             return;
         }

         // below are ways of continuously scanning wifi, but now I prefer a onClick scan

//         wifiScanReceiver = new BroadcastReceiver() {
//             @Override
//             public void onReceive(Context context, Intent intent) {
//                 if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
//                     List<ScanResult> scanResults = wifiManager.getScanResults();
//                     processWifiScanResults(scanResults);
//                 }
//             }
//         };
//         // listen to the results from os
//         registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//
//         if (checkLocationPermission(){
//             wifiManager.startScan();
//         }
     }

     /// function to handle received wifi results
     private void processWifiScanResults(List<ScanResult> scanResults)  {
        // first we filter all the weak signals, and maybe put signals
        // lower than a threshold to -100dbm, not decided yet
        double[] features = LocationDataPoint.genWifiFeatures(scanResults, AP_LIST);

        if(curMode == Mode.TRAINING) {
            if (curLocationLabel == null) return;
            LocationDataPoint locationDP = new LocationDataPoint(curLocationLabel, features);
            SerialStorage.saveData(this, locationDP);
        } else {
            List<LocationDataPoint> trainingData = SerialStorage.loadData(this, LocationDataPoint.class);
            String predictedLabel = new KNNFilter(trainingData, K_VALUE_LOCATION).predict(features);
            // TODO: add UI functions for update
            updateLocationUI(predictedLabel);
        }

     }

     // TODO: only has location now
     private void callDetection(double[] features) {
         List<DataPoint> trainingData = SerialStorage.loadData(this);
         if (trainingData == null || trainingData.isEmpty()) {
             resultText.setText("No training data available");
             return;
         }

         String predictedLabel = new KNNFilter(trainingData, K_VALUE_LOCATION)
     }
}