package com.example.ss_lab1;
//
//import java.io.Serializable;
//
///// designed as a base class for wifi / acc data
//public abstract class DataPoint<TLabel> implements Serializable{
//    public enum DataType {
//        ACTIVITY,
//        LOCATION
//    }
//
//    protected final DataType dataType;
//    protected final TLabel label;
//    protected final double[] features;
//    protected final long timestamp;
//
//    public DataPoint(DataType dataType, TLabel label, double[] features) {
//        this.dataType = dataType;
//        this.label = label;
//        this.features = features;
//        this.timestamp = System.currentTimeMillis();
//    }
//
//    //
//    public DataType getType() {
//        return dataType;
//    }
//    public CharSequence getLabel() {
//        return (CharSequence) label;
//    }
//    public double[] getFeatures() {
//        return features;
//    }
//}

public  class DataPoint {
    String label;
    float[] features;

    DataPoint(String label, float[] features) {
        this.label = label;
        this.features = features;
    }
}