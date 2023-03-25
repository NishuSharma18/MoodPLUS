package com.example.android.moodplus.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.moodplus.R;
import com.example.android.moodplus.adapter.GlobalMessageAdapter;
import com.example.android.moodplus.model.MyMessage;
import com.example.android.moodplus.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GlobalChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText MessageInput;
    private ProgressBar progressBar;
    private ImageView sendingArrow;
    private GlobalMessageAdapter globalMessageAdapter;

    private String senderName;
    private String senderEmail;
    private String senderPic;
    private String commName;

    private ArrayList<MyMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chat);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.gb_recycler_Messages);
        MessageInput = findViewById(R.id.gb_Write_Chat);
        progressBar = findViewById(R.id.gb_message_progress);
        sendingArrow = findViewById(R.id.gb_imageSend);

        messages = new ArrayList<>();

        Intent intent = getIntent();
        commName = intent.getStringExtra("Community_name");


        attachMessageListener();
        getUserData();

        sendingArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference(commName+"/")
                        .push().setValue(new MyMessage(senderName,
                        senderEmail,senderPic,MessageInput.getText().toString()));
                MessageInput.setText("");
            }
        });
    }

    private void attachMessageListener(){
        try {
            FirebaseDatabase.getInstance().getReference(commName)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            messages.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                messages.add(dataSnapshot.getValue(MyMessage.class));
                            }
                            globalMessageAdapter = new GlobalMessageAdapter(messages,
                                    FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                    GlobalChatActivity.this);

                            globalMessageAdapter.notifyDataSetChanged();
                            recyclerView.setLayoutManager(new LinearLayoutManager(GlobalChatActivity.this));
                            recyclerView.setAdapter(globalMessageAdapter);
                            recyclerView.scrollToPosition(messages.size() - 1);
                            recyclerView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Slow Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUserData(){
        try {

            //Getting data about user from database.
            FirebaseDatabase.getInstance().getReference("users/" +
                    FirebaseAuth.getInstance().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            //Saving the data in various variables.
                            senderName = snapshot.getValue(User.class).getName();
                            senderEmail = snapshot.getValue(User.class).getEmail();
                            senderPic = snapshot.getValue(User.class).getProfilePic();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Slow Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }
    }
}