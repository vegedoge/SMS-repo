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
        knn.printTrainSet();
    }

    @Test
    public void testPredict() {
        float[] rawInputStill1 = new float[] {-0.20930018f,	2.2678692f,	9.267372f, 0.392722f, 0.35653067f, 0.332901f, 9.544078f};
        float[] rawInputStill2 = new float[] {0.044123646f,	2.4320655f,	9.299658f,	0.13878371f,	0.16031909f,	0.6248255f,	9.612972f};
        float[] rawInputMove1 = new float[] {-0.5685346f,	3.4986897f,	8.767387f,	0.8117651f,	0.7663014f,	2.81067f,	9.4634f};
        float[] rawInputMove2 = new float[] {0.3226243f,	2.715357f,	10.536249f,	0.689432f,	1.2281163f,	2.2384853f,	10.898175f};
        String label1 = knn.predict(rawInputStill1);
        String label2 = knn.predict(rawInputStill2);
        String label3 = knn.predict(rawInputMove1);
        String label4 = knn.predict(rawInputMove2);

        Assert.assertEquals("Still", label1);
        Assert.assertEquals("Still", label2);
        Assert.assertEquals("Walking", label3);
        Assert.assertEquals("Walking", label4);
//        assertEquals("walking", label);
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
