package com.example.movieapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

class MoviesAdapter extends ArrayAdapter<movie> {
    private Context mContext;
     int mResource;
    public MoviesAdapter( Context context, int resource,  List<movie> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        String title = getItem(position).getTitle();
        String year = getItem(position).getYear();

        movie mov = new movie(title, year);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent,false);

        TextView mYear =(TextView)convertView.findViewById(R.id.year);
        TextView mTitle =(TextView)convertView.findViewById(R.id.ttl);
        mYear.setText(year);
        mTitle.setText(title);

        return convertView;


    }
}
