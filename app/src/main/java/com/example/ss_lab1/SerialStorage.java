package com.example.ss_lab1;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.io.*;


public class SerialStorage {
    private static final String FILE_NAME = "activity_data.ser";

    public static void saveData(Context context, List<DataPoint> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<DataPoint> loadData(Context context) {
        List<DataPoint> data = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                context.openFileInput(FILE_NAME))) {
            data = (List<DataPoint>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // no exception handle for now
            // cuz we assume the first time there's no data
        }
        return data;
    }
}
