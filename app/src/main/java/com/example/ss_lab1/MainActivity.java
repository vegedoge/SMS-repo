package com.example.ss_lab1;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // --- const values ---
    private static final double WIFI_DEFAULT_LOW_RSSI = -120.0;     // -120dbm is low enough
    private static final int K_VALUE_LOCATION = 3;      // K value for location (wifi)
    private static final int K_VALUE_ACTIVITY = 3;      // K value for activity (acc)


    // --- UI ---
    private Button detectButton;
    private TextView resultText;

    // --- sensors ---
    private WifiManager wifiManager;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener accelerometerListener;

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

        // init wifi and acc sensors, cast object to SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            Toast.makeText(this, "failed to get sensor data", Toast.LENGTH_LONG).show();
        }


    }
}