package com.example.dayary;


import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Page  implements Comparable<Page>{

    private String date, title, note;
    private Bitmap thumbnail, cover;
    private int moodFavoriteVisibility, moodHappyVisibility, moodSadVisibility, moodBadVisibility;

    public Page(String date, String title, String note, Bitmap thumbnail, Bitmap cover, int moodFavoriteVisibility, int moodHappyVisibility, int moodSadVisibility, int moodBadVisibility) {
        this.date = date;
        this.title = title;
        this.note = note;
        this.thumbnail = thumbnail;
        this.cover = cover;
        this.moodFavoriteVisibility = moodFavoriteVisibility;
        this.moodHappyVisibility = moodHappyVisibility;
        this.moodSadVisibility = moodSadVisibility;
        this.moodBadVisibility = moodBadVisibility;
    }

    public String getDate() {
        return date;
    }

    public String getDateDayOfTheWeek() {
        return new SimpleDateFormat("EEE", Locale.getDefault()).format(new GregorianCalendar(Integer.parseInt(date.substring(6)), Integer.parseInt(date.substring(0, 2))-1, Integer.parseInt(date.substring(3, 5))).getTime());
    }

    public String getDateDayOfTheWeekNum() {
        return date.substring(3, 5);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

   public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getMoodFavoriteVisibility() {
        return moodFavoriteVisibility;
    }

    public void setMoodFavoriteVisibility(int moodFavoriteVisibility) {
        this.moodFavoriteVisibility = moodFavoriteVisibility;
    }

    public int getMoodHappyVisibility() {
        return moodHappyVisibility;
    }

    public void setMoodHappyVisibility(int moodHappyVisibility) {
        this.moodHappyVisibility = moodHappyVisibility;
    }

    public int getMoodSadVisibility() {
        return moodSadVisibility;
    }

    public void setMoodSadVisibility(int moodSadVisibility) {
        this.moodSadVisibility = moodSadVisibility;
    }

    public int getMoodBadVisibility() {
        return moodBadVisibility;
    }

    public void setMoodBadVisibility(int moodBadVisibility) {
        this.moodBadVisibility = moodBadVisibility;
    }

    @Override
    public int compareTo(Page page) {
        return Integer.parseInt(date.substring(3, 5)) - Integer.parseInt(page.getDateDayOfTheWeekNum());
    }
}
