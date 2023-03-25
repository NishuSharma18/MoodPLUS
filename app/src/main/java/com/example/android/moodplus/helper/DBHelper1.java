package com.example.android.moodplus.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper1 extends SQLiteOpenHelper {

    public DBHelper1(Context context) {
        super(context, "dailysmiledata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table UserDetails(id INTEGER primary key AUTOINCREMENT,emotion TEXT,smile TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists UserDetails");
    }

    public boolean insertUserData(String emotionName, String smileValue){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("emotion",emotionName);
        contentValues.put("smile",smileValue);
        long result = db.insert("UserDetails",null,contentValues);
        if(result==-1){
            return false;
        } else {
            return true;
        }
    }

    public boolean updateUserData(int _id,String emotionName, String smileValue){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("emotion",emotionName);
        contentValues.put("smile",smileValue);

        Cursor cursor = db.rawQuery("Select * from Userdetails where id = ?",new String[]{String.valueOf(_id)});
        if(cursor.getCount()>0) {
            long result = db.update("UserDetails", contentValues, "id=?", new String[]{String.valueOf(_id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }

    }

    public boolean deleteData(int _id){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Userdetails where id = ?",new String[]{String.valueOf(_id)});
        if(cursor.getCount()>0) {
            long result = db.delete("UserDetails", "id=?", new String[]{String.valueOf(_id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }

    public Cursor getData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from UserDetails",null);
        return cursor;
    }
}
