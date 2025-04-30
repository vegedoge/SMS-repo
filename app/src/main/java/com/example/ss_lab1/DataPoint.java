package com.example.ss_lab1;

public class DataPoint {
    private static final long serialVersionUID = 1L; // for serialization
    private double[] features;
    private String label;

    public DataPoint(double[] features, String label) {
        this.features = features;
        this.label = label;
    }

    public double[] getFeatures() {
        return features;
    }

    public String getLabel() {
        return label;
    }
}
