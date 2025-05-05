package com.example.ss_lab1;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;

public class KNNClassifierTest {
    private KNNClassifier knn;

    @Before
    public void setup() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        knn = new KNNClassifier(context, "acc_train_model.json", 3);
    }
}
