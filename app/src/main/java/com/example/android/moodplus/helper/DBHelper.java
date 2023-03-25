package com.example.android.moodplus.helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "timelist.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table UserDetails(name INTEGER primary key AUTOINCREMENT,contact TEXT,dob TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists UserDetails");
    }

    public boolean insertUserData(String contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contact",contact);
        long result = db.insert("UserDetails",null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteData(int name){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Userdetails where name = ?",new String[]{String.valueOf(name)});
        if(cursor.getCount()>0) {
            long result = db.delete("UserDetails", "name=?", new String[]{String.valueOf(name)});
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
