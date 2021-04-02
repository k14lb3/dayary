package com.example.dayary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DiaryDBHelper extends SQLiteOpenHelper {

    public static final String PAGE_TABLE = "PAGE_TABLE";
    public static final String COLUMN_PAGE_DATE = "COLUMN_PAGE_DATE";
    private ByteArrayOutputStream pageByteArrayOutputStream;
    public static final String COLUMN_PAGE_THUMBNAIL = "COLUMN_PAGE_THUMBNAIL";
    public static final String COLUMN_PAGE_COVER = "COLUMN_PAGE_COVER";
    public static final String COLUMN_PAGE_TITLE = "COLUMN_PAGE_TITLE";
    public static final String COLUMN_PAGE_NOTE = "COLUMN_PAGE_NOTE";
    public static final String COLUMN_PAGE_FAVORITE = "COLUMN_PAGE_FAVORITE";
    public static final String COLUMN_PAGE_HAPPY = "COLUMN_PAGE_HAPPY";
    public static final String COLUMN_PAGE_SAD = "COLUMN_PAGE_SAD";
    public static final String COLUMN_PAGE_BAD = "COLUMN_PAGE_BAD";

    public DiaryDBHelper(@Nullable Context context) {
        super(context, "pages.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + PAGE_TABLE + " (" +
                COLUMN_PAGE_DATE + " TEXT, " +
                COLUMN_PAGE_THUMBNAIL + " BLOB, " +
                COLUMN_PAGE_COVER + " BLOB, " +
                COLUMN_PAGE_TITLE + " TEXT, " +
                COLUMN_PAGE_NOTE + " TEXT, " +
                COLUMN_PAGE_FAVORITE + " INT, " +
                COLUMN_PAGE_HAPPY + " INT, " +
                COLUMN_PAGE_SAD + " INT, " +
                COLUMN_PAGE_BAD + " INT)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Adds a page in the diary //
    public boolean addPage(Page page){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PAGE_DATE, page.getDate());

        Bitmap image;
        byte[] byteImage;

        image = page.getThumbnail();
        pageByteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, pageByteArrayOutputStream);
        byteImage = pageByteArrayOutputStream.toByteArray();
        cv.put(COLUMN_PAGE_THUMBNAIL, byteImage);

        image = page.getCover();
        pageByteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, pageByteArrayOutputStream);
        byteImage = pageByteArrayOutputStream.toByteArray();
        cv.put(COLUMN_PAGE_COVER, byteImage);

        cv.put(COLUMN_PAGE_TITLE, page.getTitle());
        cv.put(COLUMN_PAGE_NOTE, page.getNote());
        cv.put(COLUMN_PAGE_FAVORITE, page.getMoodFavoriteVisibility());
        cv.put(COLUMN_PAGE_HAPPY, page.getMoodHappyVisibility());
        cv.put(COLUMN_PAGE_SAD, page.getMoodSadVisibility());
        cv.put(COLUMN_PAGE_BAD, page.getMoodBadVisibility());

        long insert = sqLiteDatabase.insert(PAGE_TABLE, null, cv);
        return insert != -1;
    }

    // Update the page's contents in the diary //
    public boolean updatePage(String date, Page page){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PAGE_DATE, page.getDate());

        Bitmap image;
        pageByteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byteImage;

        image = page.getThumbnail();
        pageByteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, pageByteArrayOutputStream);
        byteImage = pageByteArrayOutputStream.toByteArray();
        cv.put(COLUMN_PAGE_THUMBNAIL, byteImage);

        image = page.getCover();
        pageByteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, pageByteArrayOutputStream);
        byteImage = pageByteArrayOutputStream.toByteArray();
        cv.put(COLUMN_PAGE_COVER, byteImage);

        cv.put(COLUMN_PAGE_TITLE, page.getTitle());
        cv.put(COLUMN_PAGE_NOTE, page.getNote());
        cv.put(COLUMN_PAGE_FAVORITE, page.getMoodFavoriteVisibility());
        cv.put(COLUMN_PAGE_HAPPY, page.getMoodHappyVisibility());
        cv.put(COLUMN_PAGE_SAD, page.getMoodSadVisibility());
        cv.put(COLUMN_PAGE_BAD, page.getMoodBadVisibility());

        long update = sqLiteDatabase.update(PAGE_TABLE, cv, COLUMN_PAGE_DATE + "=?", new String[]{date});
        return update != -1;
    }

    // Gets all the pages depending on the date set //
    public ArrayList<Page> getPages(String currentlyPickedDate){

        ArrayList<Page> pages = new ArrayList<>();

        String queryString = "SELECT * FROM " + PAGE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{

                String pageDate = cursor.getString(0);
                if(pageDate.substring(0, 2).equals(currentlyPickedDate.substring(0, 2)) && pageDate.substring(6).equals(currentlyPickedDate.substring(3))){
                    byte[] pageImageBlob;

                    pageImageBlob = cursor.getBlob(1);
                    Bitmap pageThumbnail = BitmapFactory.decodeByteArray(pageImageBlob, 0 , pageImageBlob.length);

                    pageImageBlob = cursor.getBlob(2);
                    Bitmap pageImage = BitmapFactory.decodeByteArray(pageImageBlob, 0 , pageImageBlob.length);

                    String pageTitle = cursor.getString(3);
                    String pageNote = cursor.getString(4);
                    int pageMoodFavoriteVisibility = cursor.getInt(5);
                    int pageMoodHappyVisibility = cursor.getInt(6);
                    int pageMoodSadVisibility = cursor.getInt(7);
                    int pageMoodBadVisibility = cursor.getInt(8);

                    Page page = new Page(pageDate, pageTitle, pageNote, pageThumbnail, pageImage, pageMoodFavoriteVisibility, pageMoodHappyVisibility, pageMoodSadVisibility, pageMoodBadVisibility);
                    pages.add(page);
                }

            }while(cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return pages;

    }

    // Checks if there is no pages //
    public boolean isEmpty(){

        String queryString = "SELECT * FROM " + PAGE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        boolean empty = false;

        if(!(cursor.moveToFirst())){
            empty = true;
        }

        cursor.close();
        db.close();

        return empty;
    }

    // Checks if there is a page with a duplicate date in the diary //
    public boolean checkDuplicate(String date){
        String queryString = "SELECT * FROM " + PAGE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(0).equals(date)){
                    cursor.close();
                    db.close();
                    return true;
                }

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return false;
    }

    // Delete a specific page in the diary //
    public boolean deletePage(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + PAGE_TABLE + " WHERE " + COLUMN_PAGE_DATE + " = '" + date + "'";

        Cursor cursor = db.rawQuery(queryString, null);
        return cursor.moveToFirst();
    }

    // Deletes all the pages of the diary //
    public void clearDiary(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String queryString = "DELETE FROM " + PAGE_TABLE;
        sqLiteDatabase.execSQL(queryString);
    }

}
