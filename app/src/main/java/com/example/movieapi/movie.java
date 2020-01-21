package com.example.movieapi;

public class movie {
    String year;
    String title;

    public movie(String year, String title) {
        this.year = year;
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
