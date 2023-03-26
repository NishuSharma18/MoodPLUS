package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.moodplus.R;
import com.example.android.moodplus.adapter.ThoughtAdapter;
import com.example.android.moodplus.model.Thoughts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ThoughtNoteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText MessageInput;
    private ProgressBar progressBar;
    private ImageView sendingArrow;
    private ThoughtAdapter thoughtAdapter;

    private ArrayList<Thoughts> thoughts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thought_note);

        recyclerView = findViewById(R.id.th_recycler_Messages);
        MessageInput = findViewById(R.id.th_Write_Chat);
        progressBar = findViewById(R.id.th_message_progress);
        sendingArrow = findViewById(R.id.th_imageSend);

        thoughts = new ArrayList<>();

        attachMessageListener();
        String time = DateUtils.formatSameDayTime(System.currentTimeMillis(),System.currentTimeMillis(),
                DateFormat.MEDIUM,DateFormat.MEDIUM).toString();

        sendingArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Thoughts/"+FirebaseAuth.getInstance().getUid()+"/")
                        .push().setValue(new Thoughts(MessageInput.getText().toString(), time));
                MessageInput.setText("");
            }
        });
    }

    private void attachMessageListener(){
        try {
            FirebaseDatabase.getInstance().getReference("Thoughts/"+FirebaseAuth.getInstance().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            thoughts.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                thoughts.add(dataSnapshot.getValue(Thoughts.class));
                            }
                            thoughtAdapter = new ThoughtAdapter(thoughts,
                                    ThoughtNoteActivity.this);

                            thoughtAdapter.notifyDataSetChanged();
                            recyclerView.setLayoutManager(new LinearLayoutManager(ThoughtNoteActivity.this));
                            recyclerView.setAdapter(thoughtAdapter);
                            recyclerView.scrollToPosition(thoughts.size() - 1);
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
}