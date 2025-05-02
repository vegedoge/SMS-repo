package com.example.ss_lab1;

import android.content.Context;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvStorage {
    private static final String ACC_FILE_NAME = "activity.csv";
    private static final String WIFI_FILE_NAME = "location.csv";
    private static final String CSV_HEADER = "label,features";

    // 保存数据（追加模式）
    public static void saveData(Context context, DataPoint data) {
        String filename = data.getType() == DataPoint.DataType.ACTIVITY ? ACC_FILE_NAME : WIFI_FILE_NAME;
        File file = new File(context.getFilesDir(), filename);

        try (FileWriter writer = new FileWriter(file, true)) { // true表示追加模式
            // 如果是新文件，先写入表头
            if (file.length() == 0) {
                writer.append(CSV_HEADER).append("\n");
            }

            // 转换特征值为字符串
            String featuresStr = Arrays.toString(data.getFeatures())
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "");

            writer.append(data.getLabel()).append(",");
            writer.append(featuresStr).append("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 加载数据
    public static <T extends DataPoint> List<T> loadData(Context context, Class<T> dataType) {
        String filename = (dataType == ActivityDataPoint.class) ? ACC_FILE_NAME : WIFI_FILE_NAME;
        List<T> dataPoints = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(new File(context.getFilesDir(), filename)))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // 跳过表头
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String label = parts[0];
                    double[] features = Arrays.stream(parts[1].split(","))
                            .mapToDouble(Double::parseDouble)
                            .toArray();

                    T dataPoint;
                    if (dataType == ActivityDataPoint.class) {
                        dataPoint = (T) new ActivityDataPoint(label, features);
                    } else {
                        dataPoint = (T) new LocationDataPoint(label, features);
                    }

                    dataPoints.add(dataPoint);
                }
            }
        } catch (IOException | NumberFormatException e) {
            // 文件不存在或格式错误时返回空列表
        }
        return dataPoints;
    }
}