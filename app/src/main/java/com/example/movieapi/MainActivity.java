package com.example.movieapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageButton btn;
    ImageButton btn2;
    private ProgressBar progressBar;
    TextView txt;
    Integer count = 1;
    String data = "";
    TextView output;
    TextView title;
    TextView year;
    TextView release;
    TextView runtime;
    TextView actors;
    TextView genere;
    ImageView img;
    db movieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieDatabase = new db(this);
        List<movie> moviesList = movieDatabase.getMovies();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(10);
        btn = (ImageButton) findViewById(R.id.btnSearch);
        btn2 = (ImageButton) findViewById(R.id.showMovies);
        txt = (TextView) findViewById(R.id.inputSearch);
        title = (TextView) findViewById(R.id.mTitle);
        year = (TextView) findViewById(R.id.mYear);
        release = (TextView) findViewById(R.id.mReleased);
        genere = (TextView) findViewById(R.id.mGenere);
        actors = (TextView) findViewById(R.id.mActors);
        runtime = (TextView) findViewById(R.id.mRunTime);
        img = (ImageView) findViewById(R.id.poster);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View view) {
                count = 1;
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);


                switch (view.getId()) {
                    case R.id.btnSearch:
                        new MyTask().execute("http://www.omdbapi.com/?apikey=4431e799&t=" + txt.getText());
                        break;
                    case R.id.showMovies:
                        Intent intent = new Intent(getApplicationContext(),MoviesList.class);
                        startActivity(intent);
                        break;
                }
            }
        };
        btn.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
    }

    class MyTask extends AsyncTask<String, Integer, String> {// get movie


        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String response = "";

            try {
                publishProgress(count + 2);
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                publishProgress(count + 2);

                Thread.sleep(0);
                InputStream stream = connection.getInputStream();
                publishProgress(count + 2);
                Thread.sleep(0);
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    publishProgress(count++);
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);
                    response += line;

                }

                publishProgress(count + 4);
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) { //catch sleep thread
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            data = result;
            try {

                JSONObject obj = new JSONObject(data);

                Log.d("My App", obj.toString());
                title.setText(obj.getString("Title"));
                year.setText(obj.getString("Year"));
                release.setText(obj.getString("Released"));
                genere.setText(obj.getString("Genre"));
                actors.setText(obj.getString("Actors"));
                runtime.setText(obj.getString("Runtime"));
                new DownloadImageTask((ImageView) findViewById(R.id.poster))
                        .execute(obj.getString("Poster"));
                movieDatabase.addMovie(new movie(obj.getString("Title"), obj.getString("Year")));


            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON");
            }
        }


        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setProgress(values[0]);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
