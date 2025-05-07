package com.example.ss_lab1;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SensorHandler implements SensorEventListener {
    // sensor
    private final Context context;
    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private TextView resultTextView;

    // window
    private final ArrayList<float[]> windowData = new ArrayList<>();
    private final int WINDOW_SIZE = 50;

    // training and detecting
    private boolean isTraining = false;
    private String trainingLabel = "";
    private boolean isDetecting = false;
    private KNNClassifier sensorKNN;
    private DetectionListener detectionListener; // used to display the action text
//    private String tra

    public SensorHandler(Context ctx) {
        this.context = ctx;
        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // maybe initialize the KNN here
        try {
            sensorKNN = new KNNClassifier(ctx, "acc_train_model.json", 3);
        } catch (Exception e) {
            System.out.println("Init KNN error in sensorHandler, " + e);
        }
    }

    ///  this function should be called to bind the listener to detectFragment
    public void setDetectionListener(DetectionListener listener) {
        this.detectionListener = listener;
    }

    ///  start the accel
    public void register() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    ///  stop the accel
    public void unregister() {
        sensorManager.unregisterListener(this);
    }

    public void startTraining(String label) {
        this.isTraining = true;
        this.trainingLabel = label;
        Toast.makeText(this.context, "Start Collecting Data: " + label, Toast.LENGTH_SHORT).show();
    }
    
    public void startDetecting() {
        this.isDetecting = true;
//        Toast.makeText(this.context, "Start Detecting Data", Toast.LENGTH_SHORT).show();
    }

    public static float[] extractFeatures(ArrayList<float[]> data) {
        int N = data.size();
        float sumX = 0, sumY = 0, sumZ = 0, sumX2 = 0, sumY2 = 0, sumZ2 = 0, sumMag = 0;
        float maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
        float minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;

        for (float[] d : data) {
            float x = d[0], y = d[1], z = d[2];
            sumX += x; sumY += y; sumZ += z;
            maxX = Math.max(maxX, x); minX = Math.min(minX, x);
            maxY = Math.max(maxY, y); minY = Math.min(minY, y);
            maxZ = Math.max(maxZ, z); minZ = Math.min(minZ, z);
//            sumX2 += x * x; sumY2 += y * y; sumZ2 += z * z;
            sumMag += (float) Math.sqrt(x * x + y * y + z * z);
        }

        float meanX = sumX / N, meanY = sumY / N, meanZ = sumZ / N;
//        System.out.printf("x=%.2f, y=%.2f, z=%.2f\n", maxX, minX, maxY);
        float rangeX = maxX - minX, rangeY = maxY - minY, rangeZ = maxZ - minZ;
//        float stdX = (float) Math.sqrt(sumX2 / N - meanX * meanX);
//        float stdY = (float) Math.sqrt(sumY2 / N - meanY * meanY);
//        float stdZ = (float) Math.sqrt(sumZ2 / N - meanZ * meanZ);
        float meanMag = sumMag / N;
//        System.out.printf("x=%.2f, y=%.2f, z=%.2f\n", rangeX, rangeY, rangeZ);
        return new float[]{meanX, meanY, meanZ, rangeX, rangeY, rangeZ, meanMag};
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] accel = event.values.clone();
        this.windowData.add(accel);

        if (this.windowData.size() >= WINDOW_SIZE) {
            // maybe extract the features
            float[] features = extractFeatures(this.windowData);

            if (this.isTraining) {
                CsvStorage.saveAccResults(this.context, this.trainingLabel, features);
                this.isTraining = false;
                Toast.makeText(context, "Saved Training Sample", Toast.LENGTH_SHORT).show();
            } else if (this.isDetecting) {
                String result = sensorKNN.predict(features);
                if (detectionListener != null) {
                    // notify the detectFragment to update the ui
                    detectionListener.onDetectionResult(result);
                }
//                Toast.makeText(this.context, "Detecting Mode: " + result, Toast.LENGTH_SHORT).show();
                this.isDetecting = false;
            }
            this.windowData.clear();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public interface DetectionListener {
        void onDetectionResult(String label);
    }
}
