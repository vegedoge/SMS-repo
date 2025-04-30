package com.example.ss_lab1;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

public class KNNFilter {
    private List<DataPoint> trainingData = new ArrayList<>();
    private int k;
    private Context context;

    // construct
    public KNNFilter(Context context, int k) {
        this.context = context;
        this.k = k;
        // load data
        trainingData = SerialStorage.loadData(context);
    }

    // add training data
    public void addTrainingData(DataPoint dataPoint) {
        trainingData.add(dataPoint);
        // TODO! save data
        SerialStorage.saveData(context, trainingData);
    }

    // Euclidean distance
    private double calculateDistance(double[] x, double[] y) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(x[i] - y[i], 2);
        }
        return Math.sqrt(sum);
    }

    // get result
    public String predict(DataPoint input) {
        if (trainingData == null || trainingData.isEmpty()) {
            return "Err: No training data available";
        }

        // maintain a p-queue of size k
        PriorityQueue<Pair<DataPoint, Double>> KNneighbours = new PriorityQueue<> (
                Comparator.comparingDouble(pair -> pair.second)
        );

        for (DataPoint dataPoint : trainingData) {
            double distance = calculateDistance(input.getFeatures(), dataPoint.getFeatures());
            KNneighbours.add(new Pair<>(dataPoint, distance));
            if (KNneighbours.size() > k) {
                KNneighbours.poll();
            }
        }

        // find the most common label
        Map<String, Integer> resultLabels = new HashMap<>();
        while (!KNneighbours.isEmpty()) {
            Pair<DataPoint, Double> pair = KNneighbours.poll();
            String label = pair.first.getLabel();
            // if not exist before, put 1
            resultLabels.put(label, resultLabels.getOrDefault(label, 0) + 1);
        }

        String mostCommonLabel = null;
        mostCommonLabel = Collections.max(resultLabels.entrySet(), Map.Entry.comparingByValue()).getKey();
        return mostCommonLabel;
    }
}
