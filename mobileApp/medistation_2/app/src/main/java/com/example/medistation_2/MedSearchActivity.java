package com.example.medistation_2;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedSearchActivity extends AppCompatActivity {
    private ArrayList<String> meds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.med_search);

        meds = new ArrayList<>();
        populateMeds();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        MedSearchAdapter adapter = new MedSearchAdapter(meds);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    private void setAdapter() {

    }

    private void populateMeds() {
        meds.add("Advil");
        meds.add("Tylenol");
        meds.add("Adderall");
        meds.add("Penicillin");
    }
}
