package com.example.ss_lab1;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class KNNClassifier {
    private float[] minVals;
    private float[] maxVals;
    private ArrayList<DataPoint> trainSet = new ArrayList<>();
    private int k;

    public KNNClassifier(Context context, String assetFilename, int k) throws Exception {
        this.k = k;
        loadModelFromAssets(context, assetFilename);
    }

    /**
     * Predicts the label for a given input using the KNN
     *
     * @param inputRaw An array of raw feature values for a single test sample
     * @return String: The predicted label as a String
     */
    public String predict(float[] inputRaw) {
        float[] input = normalize(inputRaw); // normalize the input

        // calculate the distance with each training sample
        PriorityQueue<Neighbor> priorityQueue = new PriorityQueue<>(Comparator.comparing(neighbor -> neighbor.distance));
        for (DataPoint data : trainSet) {
            float distance = 0;
            for (int i = 0; i < input.length; ++i) {
                distance += (input[i] - data.features[i]) * (input[i] - data.features[i]);
            }
            priorityQueue.add(new Neighbor(data.label, (float) Math.sqrt(distance)));
        }

        Map<String, Integer> votes = new HashMap<>();
        for (int i = 0; i < k && !priorityQueue.isEmpty(); ++i) {
            Neighbor neighbor = priorityQueue.poll();
            votes.put(neighbor.label, votes.getOrDefault(neighbor.label, 0) + 1);
        }

        return votes.entrySet().stream()
                .max(Map.Entry.comparingByValue()) // find the maximum entry
                .map(Map.Entry::getKey) // obtain the key, if exist
                .orElse("unknown"); // or return unknown label
    }

    /// read the json file in assets folder
    /// by calling this function, you will get the minVals, maxVals, and trainSet
    private void loadModelFromAssets(Context context, String filename) throws Exception {
        AssetManager assetManager = context.getAssets();
        InputStream is = assetManager.open(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader((is)));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String jsonContent = stringBuilder.toString();
        JSONObject object = new JSONObject(jsonContent);
        JSONArray minArr = object.getJSONArray("min_vals");
        JSONArray maxArr = object.getJSONArray("max_vals");
        JSONArray trainArr = object.getJSONArray("train_set");

        minVals = new float[minArr.length()];
        maxVals = new float[maxArr.length()];
        for (int i  = 0; i < minVals.length; ++i) {
            minVals[i] = (float) minArr.getDouble(i);
            maxVals[i] = (float) maxArr.getDouble(i);
        }

        for (int i = 0; i < trainArr.length(); ++i) {
            JSONArray row = trainArr.getJSONArray(i);
            String label = row.getString(0);
            float[] features = new float[row.length() - 1];
            for (int j = 1; j < row.length(); ++j) {
                features[j - 1] = (float) row.getDouble(j);
            }
            trainSet.add(new DataPoint(label, features));
        }
    }

    ///  normalize the test input with json's min,max values
    private float[] normalize(float[] input) {
        float[] normalize = new float[input.length];
        for (int i = 0; i < input.length; ++i) {
            if (maxVals[i] != minVals[i]) {
                normalize[i] = (input[i] - minVals[i]) / (maxVals[i] - minVals[i]);
            } else {
                normalize[i] = 0;
            }
        }
        return normalize;
    }

    ///  helper class only used in KNN
    private static class Neighbor {
        String label;
        float distance;

        Neighbor(String label, float distance) {
            this.label = label;
            this.distance = distance;
        }
    }

    ///  this function is only used in android test
    public void printTrainSet() {
        for (int i = 0; i < trainSet.size(); ++i) {
            DataPoint dp = trainSet.get(i);
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(i).append("] Label: ").append(dp.label).append(", Features: ");
            for (int j = 0; j < dp.features.length; ++j) {
                sb.append(dp.features[j]);
                if (j < dp.features.length - 1) sb.append(", ");
            }
            Log.d("KNNTrainSet", sb.toString());
        }
    }

}
