package com.example.android.moodplus.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsListDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contactlist.db";
    private static final int DATABASE_VERSION = 1;


    public ContactsListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE "+ ContactsListContract.ContactsListEntry.TABLE_NAME
                +" (" + ContactsListContract.ContactsListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ContactsListContract.ContactsListEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + ContactsListContract.ContactsListEntry.COLUMN_NUMBER + " TEXT NOT NULL"
                + "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContactsListContract.ContactsListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
