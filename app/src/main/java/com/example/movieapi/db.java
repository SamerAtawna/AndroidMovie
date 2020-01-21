package com.example.movieapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Movie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class db extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Movies";
    private static final String TABLE = "MoviesList";
    private static final String KEY_TITLE = "title";
    private static final String KEY_YEAR = "year";

    public db(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE + "("
                + KEY_TITLE  + " TEXT," + KEY_YEAR+ " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        // Create tables again
        onCreate(db);
    }

    void addMovie(movie mov){
        SQLiteDatabase d = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE,mov.getTitle());
        values.put(KEY_YEAR,mov.getYear());
        // Inserting Row
        d.insert(TABLE, null, values);
        //2nd argument is String containing nullColumnHack
        d.close(); // Closing database connection
    }

    public List<movie> getMovies(){
        List<movie> movieList= new ArrayList<movie>();
        String query = "SELECT * FROM "+TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                movie mov = new movie(cursor.getString(0), cursor.getString(1));

                // Adding contact to list
                movieList.add(mov);
            } while (cursor.moveToNext());
        }

        // return contact list
        return movieList;

    }
}
