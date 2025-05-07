package com.example.ss_lab1;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class DetectFragment extends Fragment implements SensorHandler.DetectionListener, MainActivity.wifiResultListener {
    private TrainFragment.ControlListener controlListener;
    private TextView moveTextView;
    private TextView txtC1, txtC3, txtC6, txtC8;
    private TextView[] textViews;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        controlListener = (TrainFragment.ControlListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // define four textViews
        txtC1 = view.findViewById(R.id.C1_text);
        txtC3 = view.findViewById(R.id.C3_text);
        txtC6 = view.findViewById(R.id.C6_text);
        txtC8 = view.findViewById(R.id.C8_text);

        // bind the textView with the ui
        moveTextView = view.findViewById(R.id.result_text);
        ((MainActivity) requireActivity()).getSensorHandler().setDetectionListener(this);

        // bind wifi result listener
        ((MainActivity) requireActivity()).setWifiResultListener(this);

        view.findViewById(R.id.switch_btn).setOnClickListener(v -> {
            ((MainActivity)requireActivity()).showFragment(new TrainFragment());
        });

        view.findViewById(R.id.detect_btn).setOnClickListener(v -> {
            // start moving detect
            controlListener.launchAccScan();
            // start wifi detect
            controlListener.launchWifiScan();
        });

        textViews = new TextView[]{txtC1, txtC3, txtC6, txtC8};
        resetAllTextViews();

    }

    ///  this function is used to update the ui MoveText
    @Override
    public void onDetectionResult(String label) {
        String resultText = getString(R.string.detected_result, label);
        requireActivity().runOnUiThread(() -> moveTextView.setText(resultText));
    }

    @Override
    public void onWifiResult(String label) {
        //select the label realted textview
        requireActivity().runOnUiThread(() -> resetAllTextViews());
        for (int i = 0; i < textViews.length; i++) {
            // log output label
            Log.d("onWifilabel", label);
            if (textViews[i].getText().equals(label)) {
                textViews[i].setSelected(true);
            }
        }
    }


    private void resetAllTextViews() {
        for (TextView txt: textViews) {
            txt.setSelected(false);
        }
    }


}