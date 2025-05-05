//package com.example.ss_lab1;
//
//import android.net.wifi.ScanResult;
//
//import java.util.List;
//
/////  This is the sub-class for wifi location data, derived from DataPoint
//public class LocationDataPoint extends DataPoint<String>{
//    public LocationDataPoint(String label, double[] wifiFeatures) {
//        super(DataType.LOCATION, label, wifiFeatures);
//    }
//
//    /**
//     *  get wifi features from scanned wifis in LocationDataPoint
//     * @param scannedAps        - list of scanned aps
//     * @return LocationDataPoint extends DataPoint
//     */
//    public static LocationDataPoint getWifiFromScan(
//            List<ScanResult> scannedAps,
//            String label,
//            List<String> apList
//    ) {
//        double[] features = genWifiFeatures(scannedAps, apList);
//        return new LocationDataPoint(label, features);
//    }
//
//    /**
//     *  get wifi features from scanned wifis
//     * @param scannedAps    - list of scanned aps
//     * @param apList        - predefined available wifi list
//     * @return double[]     - array of wifi features
//     */
//    public static double[] genWifiFeatures (
//            List<ScanResult> scannedAps,
//            List<String> apList
//    ) {
//        double[] features = new double[apList.size()];
//        for (int i = 0; i < apList.size(); i++) {
//            // we try to match ids
//            String targetBSSID = apList.get(i);
//            features[i] = -100.0;
//            for (ScanResult result: scannedAps)  {
//                if (result.BSSID.equals(targetBSSID))  {
//                    // default low value or the actual value
//                    if (result.level > features[i]) {
//                        features[i] = result.level;
//                    }
//                }
//                // too low equals not found
//            }
//        }
//
//        return features;
//    }
//
//}
