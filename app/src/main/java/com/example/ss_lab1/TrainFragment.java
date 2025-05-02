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
    private Button btnC1, btnC2, btnC3, btnC4;
    private Button[] buttons;

    // states
    private String curLocation = "";
    private String curActivity = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_train, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // button init and bind
        btnC1 = view.findViewById(R.id.C1_btn);
        btnC2 = view.findViewById(R.id.C2_btn);
        btnC3 = view.findViewById(R.id.C3_btn);
        btnC4 = view.findViewById(R.id.C4_btn);

        buttons = new Button[]{btnC1, btnC2, btnC3, btnC4};

        // used for button selection
        for (Button button : buttons) {
            button.setOnClickListener(v -> {
                resetAllButtons();
                button.setSelected(true);
            });
        }

        // spinner init
        Spinner activitySpinner = view.findViewById(R.id.activity_spinner);
        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curActivity = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        view.findViewById(R.id.switch_btn).setOnClickListener(v -> {
            ((MainActivity)requireActivity()).showFragment(new DetectFragment());
        });
    }

    private void resetAllButtons() {
        for (Button button: buttons) {
            button.setSelected(false);
        }
    }
}
