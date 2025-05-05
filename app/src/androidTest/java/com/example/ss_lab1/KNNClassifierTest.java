package com.example.ss_lab1;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KNNClassifierTest {
    private KNNClassifier knn;

    @Before
    public void setup() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        knn = new KNNClassifier(context, "acc_train_model.json", 3);
    }

//    @Test
//    public void testLoadModelFromAssets() {
//        for (int i = 0; i < knn.minVals.length; ++i) {
//            Log.d("KNNLoadTest", "Min Value: " + knn.minVals[i]);
//            Log.d("KNNLoadTest", "Max Value: " + knn.maxVals[i]);
//        }
//        knn.printTrainSet();
//    }
//
//    @Test
//    public void testNormalized() {
//        float[] raw = new float[]{0.51236314f, 2.4797895f, 10.649527f, 0.7540382f, 2.3294127f, 3.3900318f, 10.997206f};
//        float[] expected = new float[]{0.6454485069052441f, 0.44996623737647146f, 0.9800634177421508f, 0.42976724065479277f, 1.0f, 0.9249329477556962f, 1.0f};
//        float[] result = knn.normalize(raw);
//
//        for (int i = 0; i < raw.length; ++i) {
////            Log.d("KNNTest", "Value: " + result[i]);
//            Assert.assertEquals(expected[i], result[i], 0.01);
//        }
//    }
}
