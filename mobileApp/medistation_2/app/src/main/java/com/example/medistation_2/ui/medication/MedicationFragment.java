package com.example.medistation_2.ui.medication;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.medistation_2.R;
import com.example.medistation_2.ui.profile.ProfileFragment;

public class MedicationFragment extends Fragment {

    private MedicationViewModel mViewModel;
    private static final String TAG = MedicationFragment.class.getSimpleName();

    public static MedicationFragment newInstance() {
        return new MedicationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medication, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        ToggleButton testButton = (ToggleButton) view.findViewById(R.id.toggleButton);
        Button buttonpress = view.findViewById(R.id.button);
        buttonpress.setOnClickListener(v -> {
            Log.d(TAG,"Buttonnn Pressed");
            Boolean ToggleButtonState = testButton.isChecked();
            Log.d(TAG,String.valueOf(ToggleButtonState));
            TableLayout table = (TableLayout)view.findViewById(R.id.medi);
            for(int i=0;i<3;++i) {
                    // Inflate your row "template" and fill out the fields.
                    TableRow row = (TableRow)LayoutInflater.from(requireActivity()).inflate(R.layout.schedule_table_format, null);
                    ((TextView)row.findViewById(R.id.attrib_name)).setText("name");
                    //((TextView)row.findViewById(R.id.attrib_value)).setText("value");
                    table.addView(row);
                table.requestLayout();     // Not sure if this is needed.
            }
        });
    }
}