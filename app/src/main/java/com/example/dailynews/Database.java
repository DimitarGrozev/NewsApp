package com.example.dailynews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DailyNews";
    private static final String TABLE_CONTACTS = "WORDS";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "Word";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);

    }


    public Boolean AddWord(String word) {
        if(!this.WordExists(word) && !word.equals("")) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, word); // Име на Потребител
            db.insert(TABLE_CONTACTS, null, values);
            db.close(); // Затравяне на връзката с базата от данни

            return true;
        }

        return false;
    }

    private Boolean WordExists(String word){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS,
                new String[]{KEY_ID,KEY_NAME},
                KEY_NAME +"=?", new String[]{word},
                null,null,null,null);
        if(cursor.getCount() == 1){
            return true;
        }

        return false;
    }

    public List<String> GetMatchingWords(String word) {
        List<String> wordsList = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS,new String[]{KEY_NAME},KEY_NAME + " LIKE ?", new String[]{"%"+word+"%"},null,null,null);

        if (cursor.moveToFirst()) {
            do {
                String dbWord = cursor.getString(0);
                wordsList.add(dbWord);
            } while (cursor.moveToNext());
        }

        return wordsList;
    }
}