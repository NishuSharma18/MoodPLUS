package com.example.android.moodplus.data;

import android.provider.BaseColumns;

public class ContactsListContract {

    public static final class ContactsListEntry implements BaseColumns {

        public static final String TABLE_NAME = "contactlist";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_NUMBER = "Number";


    }
}
