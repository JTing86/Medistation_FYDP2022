package com.example.medistation_2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MedSearchAdapter extends RecyclerView.Adapter<MedSearchAdapter.MyViewHolder> implements Filterable {
    private ArrayList<String> meds;
    private ArrayList<String> medsFull;

    public MedSearchAdapter(ArrayList<String> meds) {
        this.meds = meds;
        medsFull = new ArrayList<>(meds); // deep copy of meds list

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView medText;

        public MyViewHolder(final View view) {
            super(view);
            medText = view.findViewById(R.id.medItemTextView);
        }
    }

    @NonNull
    @NotNull
    @Override
    public MedSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.med_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MedSearchAdapter.MyViewHolder holder, int position) {
        String medication = meds.get(position);
        holder.medText.setText(medication);
    }

    @Override
    public int getItemCount() {
        return meds.size();
    }

    private Filter medsFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String> filteredList = new ArrayList<>();
            
            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(medsFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (String med : medsFull) {
                    if(med.toLowerCase().contains(filterPattern)) {
                        filteredList.add(med);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            meds.clear();
            meds.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return medsFilter;
    }
}
