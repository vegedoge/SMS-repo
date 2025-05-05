//package com.example.ss_lab1;
//
//import java.util.List;
//
/////  This is the sub-class for activity data, derived from DataPoint
//public class ActivityDataPoint extends DataPoint<String> {
//    public ActivityDataPoint(String label, double[] features) {
//        super(DataType.ACTIVITY, label, features);
//    }
//
//    /**
//     *  get acc features from sensor data as an ActivityDataPoint
//     * @param windowData        - window slice from sensor data
//     * @param label             - activity label
//     * @return ActivityDataPoint extends DataPoint
//     */
//    public static ActivityDataPoint getAccFromSensor(List<double[]> windowData, String label) {
//        double[] features = genAccFeatures(windowData);
//        return new ActivityDataPoint(label, features);
//    }
//
//    /**
//     *  get acc features from sensor data
//     * @param windowData        - window slice from sensor data
//     * @return double[]         - array of acc features
//     */
//    private static double[] genAccFeatures(List<double[]> windowData) {
//        // first get max-min of x, y, z
//        // TODO: not sure if the data format is like this, need to check later
//        // just a placeholder
//        double[] features = new double[3];
//        for (int i = 0; i < 3; i++) {
//            double max = Double.MIN_VALUE;
//            double min = Double.MAX_VALUE;
//
//            for (double[] data: windowData) {
//                if (data[i] > max) {
//                    max = data[i];
//                }
//                if (data[i] < min) {
//                    min = data[i];
//                }
//            }
//            features[i] = max - min;
//        }
//
//        return features;
//    }
//}
