package com.example.medistation_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SymptomAddActivity extends AppCompatActivity {

    private Button SymptomAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_add);

        SymptomAddButton = findViewById(R.id.SymptomAddButton);
        SymptomAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.d("Test", "Button Working");
            }
        });
    }
    public void AddSymptoms (View view)
    {

    }
}