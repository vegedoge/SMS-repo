package com.example.ss_lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class TrainFragment extends Fragment {
    // uis
    private Button btnC1, btnC3, btnC6, btnC8;
    private Button[] buttons;

    // states
    private String curLocation = "";
    private String curActivity = "";

    public interface ControlListener {
        void launchWifiScan();
        void onLocationSelected(String label);
        void onActivitySelected(String label);
    }

    private ControlListener controlListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_train, container, false);
    }

    @Override
    public void onAttach(@NonNull android.content.Context context) {
        super.onAttach(context);
        controlListener = (ControlListener) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // get Main activity
//        MainActivity mainActivity = (MainActivity) requireActivity();

        // button init and bind
        btnC1 = view.findViewById(R.id.C1_btn);
        btnC6 = view.findViewById(R.id.C6_btn);
        btnC3 = view.findViewById(R.id.C3_btn);
        btnC8 = view.findViewById(R.id.C8_btn);

        buttons = new Button[]{btnC1, btnC3, btnC6, btnC8};

        // used for button selection
        for (Button button : buttons) {
            button.setOnClickListener(v -> {
                resetAllButtons();
                button.setSelected(true);
                curLocation = button.getText().toString();
                controlListener.onLocationSelected(curLocation);
            });
        }

        // spinner init
        Spinner activitySpinner = view.findViewById(R.id.activity_spinner);
        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curActivity = parent.getItemAtPosition(position).toString();
                controlListener.onActivitySelected(curActivity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing happens
            }
        });

        view.findViewById(R.id.switch_btn).setOnClickListener(v -> {
            ((MainActivity)requireActivity()).showFragment(new DetectFragment());
        });

        // bind wifi scan button
        view.findViewById(R.id.train_wifi_btn).setOnClickListener(v -> {
            controlListener.launchWifiScan();
        });
    }

    private void resetAllButtons() {
        for (Button button: buttons) {
            button.setSelected(false);
        }
    }

}
