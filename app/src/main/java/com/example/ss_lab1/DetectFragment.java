package com.example.ss_lab1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class DetectFragment extends Fragment {
    private TrainFragment.ControlListener controlListener;

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
        view.findViewById(R.id.switch_btn).setOnClickListener(v -> {
            ((MainActivity)requireActivity()).showFragment(new TrainFragment());
        });

        view.findViewById(R.id.detect_btn).setOnClickListener(v -> {
            // start moving detect
            controlListener.launchAccScan();
            // start wifi detect
//            controlListener.launchWifiScan();
        });
    }


}