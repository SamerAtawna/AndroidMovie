package com.example.movieapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class MoviesList extends AppCompatActivity {
    ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        lst = (ListView)findViewById(R.id.lstView);
        db d = new db(this);
        List<movie> moviesList = d.getMovies();

        MoviesAdapter adapter = new MoviesAdapter(this, R.layout.list, moviesList);
        lst.setAdapter(adapter);




}
}
