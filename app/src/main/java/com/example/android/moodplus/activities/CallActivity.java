package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.moodplus.data.ContactsListContract;
import com.example.android.moodplus.data.ContactsListDbHelper;
import com.example.android.moodplus.R;
import com.example.android.moodplus.adapter.ContactsAdapter;
import com.example.android.moodplus.model.MyContact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


public class CallActivity extends AppCompatActivity {
    private ArrayList<MyContact> userContacts;
    FloatingActionButton addContact;
    private ProgressBar progressBar;
    private ContactsAdapter contactsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    ContactsAdapter.onContactsClickListener onContactsClickListener;
    private SQLiteDatabase mDb;

    private static final int CONTACT_PERMISSION_CODE = 1;
    private static final int CONTACT_PICK_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        swipeRefreshLayout = findViewById(R.id.swip);
        addContact = findViewById(R.id.fab_callActivity);
        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recycler);
        userContacts = new ArrayList<>();

        ContactsListDbHelper dbHelper = new ContactsListDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkContactPermission()){
                    pickContactIntent();
                }
                else{
                    requestContactPermission();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userContacts = addToContactlist();
                setAdapter(userContacts);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        onContactsClickListener = new ContactsAdapter.onContactsClickListener() {
            @Override
            public void onContactsClicked(int position) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+userContacts.get(position).getNumber()));
                startActivity(callIntent);
            }
        };

        userContacts = addToContactlist();
        setAdapter(userContacts);
    }

    public ArrayList<MyContact> addToContactlist() {

        // on below line we are creating a
        // database for reading our database.
        ContactsListDbHelper dbHelper2 = new ContactsListDbHelper(this);
        SQLiteDatabase db = dbHelper2.getReadableDatabase();

        // on below line we are creating a cursor with query to
        // read data from database.
        Cursor cursorContacts
                = db.rawQuery("SELECT * FROM " + ContactsListContract.ContactsListEntry.TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<MyContact> myContactArrayList
                = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorContacts.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                myContactArrayList.add(new MyContact(
                        cursorContacts.getString(1),
                        cursorContacts.getString(2)));
            } while (cursorContacts.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorContacts.close();
        return myContactArrayList;
    }

    private long addNewGuest(String name, String number) {

        ContentValues cv = new ContentValues();
        cv.put(ContactsListContract.ContactsListEntry.COLUMN_NAME, name);
        cv.put(ContactsListContract.ContactsListEntry.COLUMN_NUMBER, number);

        return mDb.insert(ContactsListContract.ContactsListEntry.TABLE_NAME, null, cv);
    }

    private boolean removeGuest(Long id){

        return mDb.delete(ContactsListContract.ContactsListEntry.TABLE_NAME,
                ContactsListContract.ContactsListEntry._ID+"="+id,null)>0;
    }

    private boolean checkContactPermission(){

        boolean result = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS) == (PackageManager.PERMISSION_GRANTED);

        return  result;
    }

    private void requestContactPermission(){
        String[] permission = {Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(this,permission,CONTACT_PERMISSION_CODE);
    }

    private void pickContactIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,CONTACT_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CONTACT_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                pickContactIntent();
            }
            else{
                Toast.makeText(getApplicationContext(), "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == CONTACT_PICK_CODE){
                Cursor cursor1,cursor2;
                Uri uri = data.getData();
                cursor1 = getContentResolver().query(uri,null,null,null,null);
                if(cursor1.moveToFirst()){
                    String contactId = cursor1.
                            getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                    String contactName = cursor1.
                            getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String idResults = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    int idResultHold = Integer.parseInt(idResults);
                    if(idResultHold==1){
                        cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+
                                        contactId,null,null);
                        boolean z = true;
                        String contactNumber = "";
                        while(cursor2.moveToNext() && z==true){
                            contactNumber = cursor2.getString(cursor2.
                                    getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            z=false;
                        }
                        addNewGuest(contactName,contactNumber);
                        cursor2.close();
                    }
                    cursor1.close();
                }
            }
        }
        else{
            //Calls when user clicks back button

        }
    }
    private void setAdapter(ArrayList<MyContact> arrayList){
        contactsAdapter = new ContactsAdapter(arrayList , getApplicationContext(), onContactsClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(contactsAdapter);

    }
}