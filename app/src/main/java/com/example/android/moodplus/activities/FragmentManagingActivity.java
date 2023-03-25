package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.moodplus.R;
import com.example.android.moodplus.adapter.VPadapter;
import com.example.android.moodplus.fragments.CommunitiesFragment;
import com.example.android.moodplus.model.User;
import com.example.android.moodplus.fragments.TherapyFragment;
import com.example.android.moodplus.fragments.ChartsFragment;
import com.example.android.moodplus.fragments.ChatGptFragment;
import com.example.android.moodplus.fragments.MoodFragment;
import com.example.android.moodplus.service.NotificationReceiver;
import com.example.android.moodplus.utility.AlarmUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class FragmentManagingActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "channel_id";
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String senderName;
    private String senderEmail;
    private String senderPic;
    private String senderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_managing);


        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        //Setting tab layout to show viewPager
        tabLayout.setupWithViewPager(viewPager);

        //Getting user data to be sent to Global Chat Activity and Profile Activity.
        getUserData();
        createNotificationChannel();
//        setUpNotification();

        //Setting up VP adapter and adding fragments to be shown.
        VPadapter vPadapter = new VPadapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        vPadapter.addFragment(new MoodFragment(),"Mood");
        vPadapter.addFragment(new ChartsFragment(),"Charts");
        vPadapter.addFragment(new ChatGptFragment(),"Talk");
        vPadapter.addFragment(new CommunitiesFragment(),"Community");
        vPadapter.addFragment(new TherapyFragment(),"Therapy");
        viewPager.setAdapter(vPadapter);

        // Set up the alarm to send notifications every 4 hours
        AlarmUtils.setAlarm(this);

    }
    //Inflating menu options.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_vpadapter,menu);
        return true;
    }

    //Setting what happens when any menu item is clicked.
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.menu_setting){
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.menu_usage_stats){
            Intent intent = new Intent(getApplicationContext(),UsageStatsActivity.class);
            startActivity(intent);

//              Intent intent = new Intent(getApplicationContext(),ThoughtNoteActivity.class);
//              startActivity(intent);
        }
        else if(item.getItemId()==R.id.menu_profile){
            if(!senderName.isEmpty()) { //Data received

                //Sending data to profile activity.
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);

                intent.putExtra("update_from_allList",true);
                intent.putExtra("sender_name", senderName);
                intent.putExtra("sender_pic", senderPic);
                intent.putExtra("sender_number",senderNumber);
                startActivity(intent);
            }
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
                            senderNumber = snapshot.getValue(User.class).getNumber();
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
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_Name";
            String description = "channel_Desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}