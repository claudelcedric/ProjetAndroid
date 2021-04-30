package com.example.mesfrigos4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Aliment_frigidaire> {
    private final OnOptionsListener mListener;
    List<Aliment_frigidaire> list;

    public interface OnOptionsListener {
        public void onOptions(int position);
    }

    public CustomAdapter(Context context, ArrayList<Aliment_frigidaire> food, OnOptionsListener mListener) {
        super(context, 0, food);
        this.list = food;
        this.mListener = mListener;
    }
    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Aliment_frigidaire food = list.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.aliment, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.food_name);
        TextView tvCategory = (TextView) convertView.findViewById(R.id.food_category);
        TextView tvExpiration_date = (TextView) convertView.findViewById(R.id.food_expiration_date);

        // Populate the data into the template view using the data object
        tvName.setText(food.getName());
        tvCategory.setText((CharSequence) food.getCategory());
        tvExpiration_date.setText(food.getExpiration_date());

        FloatingActionButton bouton_options = (FloatingActionButton) convertView.findViewById(R.id.Options_button);

        bouton_options.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("yo",food.getName());
                if(mListener != null)
                    mListener.onOptions(position);
            }

        });

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void add(@Nullable Aliment_frigidaire object) {
        list.add(object);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public Aliment_frigidaire get(int position) {
        return list.get(position);
    }
}