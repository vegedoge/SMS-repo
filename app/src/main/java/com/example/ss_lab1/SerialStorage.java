package com.example.ss_lab1;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.io.*;


public class SerialStorage {
    private static final String ACC_FILE_NAME = "activity_points.ser";
    private static final String WIFI_FILE_NAME = "location_points.ser";

    ///  save data based on dataType
    public static void saveData(Context context, List<? extends DataPoint> data) {
        // getType to see what is the data type
        String filename = data.get(0).getType() == DataPoint.DataType.ACTIVITY ? ACC_FILE_NAME : WIFI_FILE_NAME;

        try (ObjectOutputStream oos = new ObjectOutputStream(
                context.openFileOutput(filename, Context.MODE_PRIVATE))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///  load data based on class
    public static <T extends DataPoint> List<T> loadData(Context context, Class<T> dataType) {
        String filename = (dataType == ActivityDataPoint.class) ? ACC_FILE_NAME : WIFI_FILE_NAME;
        List<T> data = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                context.openFileInput(filename))) {
            data = (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // no exception handle for now
            // cuz we assume the first time there's no data
        }
        return data;
    }
}
