package com.example.android.moodplus.fragments;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moodplus.R;
import com.example.android.moodplus.activities.SuggestionActivity;
import com.example.android.moodplus.data.ContactsListContract;
import com.example.android.moodplus.data.ContactsListDbHelper;
import com.example.android.moodplus.helper.DBHelper1;
import com.example.android.moodplus.model.MyContact;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class MoodFragment extends Fragment {
    TextView moodType;
    LineChart chart ;
    private TextView userHappy;
    private TextView userSad;
    private TextView userNeutral;
    private TextView userStressed;
    private TextView queryView;
    private float userUpdateValue;
    private ArrayList<String> questionsList;
    private ArrayList<String> option1List;
    private ArrayList<String> option2List;
    private ArrayList<String> option3List;
    private ArrayList<String> option4List;
    public static final String SHARED_PREFS = "sharedPrefs";
    private String moodVal;

    private ArrayList<MyContact> userContacts;

    private ArrayList<Float> emotionValueList;
    ArrayList<Entry> dataVals;
    DBHelper1 db1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_mood, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        moodType = getView().findViewById(R.id.moodType);

        db1 = new DBHelper1(getActivity());
        emotionValueList = new ArrayList<>();
        dataVals = new ArrayList<Entry>();
        queryView = getView().findViewById(R.id.query_tv);
        userSad = getView().findViewById(R.id.feel_sad_tv);
        userNeutral = getView().findViewById(R.id.feel_okay_tv);
        userHappy = getView().findViewById(R.id.feel_happy_tv);
        userStressed = getView().findViewById(R.id.feel_stressed_tv);

        ArrayList<String> suggestionsList = new ArrayList<>();
        userContacts = new ArrayList<>();
        questionsList = new ArrayList<>();
        option1List = new ArrayList<>();
        option2List = new ArrayList<>();
        option3List = new ArrayList<>();
        option4List = new ArrayList<>();

        suggestionsList.add("Take a break from busy life. Try to meditate a few minutes.");
        suggestionsList.add("You keep eating junk fruits, for today eat some fruits or have a healthy meal.");
        suggestionsList.add("Hey, how pleasant the weather is!! Lets go for a walk.");
        suggestionsList.add("Maintain your sleep schedule well.");

        addToQuestionsList("Wanna eat any dish for dinner? I can order for you?",
                "Yupp",
                "Nooo",
                "",
                "");
        addToQuestionsList("You spend time too much on phone. Why don't you go out and try out something different",
                "Going now.",
                "Won't go",
                "Have work",
                "");
        addToQuestionsList("Have you taken a bath today?",
                "Yupp",
                "A secret",
                "No",
                "");
        addToQuestionsList("Wanna hangout with friends? Let's go.",
                "Yupp",
                "Nah",
                "Later",
                "Can't");
        addToQuestionsList("How is your day going?",
                "Awesome",
                "Stressed",
                "Okay",
                "Fine");
        addToQuestionsList("Did something bad happen?",
                "No",
                "Yes",
                "Get lost",
                "Neutral");

        getCurrentMood();

        final int random = new Random().nextInt(2);
        unsetQuery();
        // setting suggestion
        if(random==1){
            final int randomIndex = new Random().nextInt(suggestionsList.size());
            queryView.setText(suggestionsList.get(randomIndex));
        }
        // setting question
        else{
            final int randomIndex = new Random().nextInt(questionsList.size());
            queryView.setText(questionsList.get(randomIndex));
            userHappy.setText(option1List.get(randomIndex));
            userSad.setText(option2List.get(randomIndex));
            userNeutral.setText(option3List.get(randomIndex));
            userStressed.setText(option4List.get(randomIndex));
            if(option4List.get(randomIndex).equals("")){
                userStressed.setVisibility(View.GONE);
            }
            if(option3List.get(randomIndex).equals("")){
                userNeutral.setVisibility(View.GONE);
            }
            if(option2List.get(randomIndex).equals("")){
                userSad.setVisibility(View.GONE);
            }
            if(option1List.get(randomIndex).equals("")){
                userHappy.setVisibility(View.GONE);
            }

            userHappy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userUpdateValue = 0.01f;
                    check(getActivity(),userUpdateValue);
                    unsetView();
//                    Toast.makeText(MainActivity.this, "your response recorded", Toast.LENGTH_SHORT).show();
                }
            });

            userSad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userUpdateValue = 0.5f;
                    check(getActivity(),userUpdateValue);
                    unsetView();
//                    Toast.makeText(MainActivity.this, "your response recorded", Toast.LENGTH_SHORT).show();
                }
            });

            userNeutral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unsetView();
//                    userUpdateValue = 0.01f;
//                    check(getActivity(),userUpdateValue);
//                    Toast.makeText(MainActivity.this, "your response recorded", Toast.LENGTH_SHORT).show();
                }
            });

            userStressed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    userUpdateValue = 0.01f;
//                    check(getActivity(),userUpdateValue);
//                    Toast.makeText(getActivity(), "your response recorded", Toast.LENGTH_SHORT).show();
                    unsetView();
                }
            });
        }

        String m = moodType.getText().toString();

        moodType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SuggestionActivity.class);
                intent.putExtra("getMood",m);
                startActivity(intent);
            }
        });

//        TextView Quotes = getView().findViewById(R.id.quotes);

        readData(dataVals);
        userContacts = addToContactlist();
        sendEmergencySms();

    }
    public void readTable(){

        Cursor cursor = db1.getData();
        if(cursor.getCount()==0){
            Toast.makeText(getActivity(), "No entry exists", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            buffer.append("Id :" + cursor.getInt(0) + "\n");
            buffer.append("Smile :" + cursor.getString(2) + "\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("User Entries");
        builder.setMessage(buffer.toString());
        builder.show();
    }

    public void readData(ArrayList<Entry> dataVals) {

        Cursor cursor = db1.getData();

        int i=0;

        addDummyData(dataVals);
        while (cursor.moveToNext()) {
            String val = cursor.getString(2);
            float emotionValue = Float.parseFloat(val);
            addData(i,emotionValue*1000,dataVals);
            i++;
        }

        drawChart(dataVals);

    }

    private void addData(int i, float emotionValue,ArrayList<Entry> dataVals) {
        dataVals.add(new Entry(i,emotionValue));
    }

    private void drawChart(ArrayList<Entry> dataVals){

        chart = (LineChart) getView().findViewById(R.id.chart_mv);
        LineDataSet lineDataSet = new LineDataSet(dataVals, "");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        chart.setBackgroundColor(Color.LTGRAY);
        chart.setNoDataText("Oops! No Data to Show.");
        chart.setNoDataTextColor(Color.BLUE);
        chart.setDrawGridBackground(true);
        chart.setDrawBorders(true);
        chart.setBorderColor(Color.RED);
        chart.setBorderWidth(6);

        lineDataSet.setLineWidth(4);
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleColor(Color.GRAY);
        lineDataSet.setCircleHoleColor(Color.GREEN);
        lineDataSet.setCircleRadius(9);
        lineDataSet.setCircleHoleRadius(6);
        lineDataSet.setValueTextSize(10);
        lineDataSet.setValueTextColor(Color.BLACK);

        chart.animateXY(0,1000, Easing.EaseInOutBounce,Easing.EaseInOutBounce);

        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate();

    }

    private void addDummyData(ArrayList<Entry> dataVals){
        dataVals.add(new Entry(0,356));
        dataVals.add(new Entry(1,378));
        dataVals.add(new Entry(2,395));
        dataVals.add(new Entry(3,412));
        dataVals.add(new Entry(4,472));
        dataVals.add(new Entry(5,461));
    }

    public void check(Context context, float newValue){
        Calendar currentDate = Calendar.getInstance();
        int currentDayOfYear = currentDate.get(Calendar.DAY_OF_YEAR);
        int currentYear = currentDate.get(Calendar.YEAR);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        int lastCompletedDayOfYear = sharedPreferences.getInt("lastCompletedDayOfYear", -1);
        int lastCompletedYear = sharedPreferences.getInt("lastCompletedYear", -1);

        if (lastCompletedDayOfYear != -1 && lastCompletedYear != -1){
            if (lastCompletedYear < currentYear || lastCompletedDayOfYear < currentDayOfYear) {
                // Day has been completed
                // Save the current date as the last completed date
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("lastCompletedDayOfYear", currentDayOfYear);
                editor.putInt("lastCompletedYear", currentYear);
                // inserting db
                editor.putFloat("newMoodAvg",newValue);
                editor.putLong("noOfData",1);
                editor.apply();
                Log.e("TAG",String.valueOf(newValue));
            } else {
                // Day has not been completed
                float currentMoodAvg = sharedPreferences.getFloat("newMoodAvg", 0);
                long no = sharedPreferences.getLong("noOfData",0);
                currentMoodAvg = calculateAvg(currentMoodAvg, no, newValue);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("newMoodAvg", currentMoodAvg);
                editor.putLong("noOfData",no+1);
                editor.apply();
                Log.e("TAG",String.valueOf(currentMoodAvg));
            }
        } else {
            // This is the first time the app is being used
            // Save the current date as the last completed date
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("lastCompletedDayOfYear", currentDayOfYear);
            editor.putInt("lastCompletedYear", currentYear);
            // checks if value is present or not
            editor.putFloat("newMoodAvg",newValue);
            editor.putLong("noOfData",1);
            editor.apply();
            Log.e("TAG","First val");
        }
    }

    private float calculateAvg(float currentMoodAvg,long currentNo,float currentMood){
        return (currentMoodAvg*currentNo + currentMood)/(currentNo + 1);
    }

    private void addToQuestionsList(String question,String option1,String option2,
                                    String option3,String option4) {
        questionsList.add(question);
        option1List.add(option1);
        option2List.add(option2);
        option3List.add(option3);
        option4List.add(option4);
    }

    private void unsetQuery(){
        userHappy.setText("");
        userNeutral.setText("");
        userStressed.setText("");
        userSad.setText("");
    }

    private void unsetView(){
        userHappy.setVisibility(View.GONE);
        userNeutral.setVisibility(View.GONE);
        userStressed.setVisibility(View.GONE);
        userSad.setVisibility(View.GONE);
        queryView.setVisibility(View.GONE);
    }

    private void getCurrentMood(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, getActivity().MODE_PRIVATE);
        float currentMoodVal = sharedPreferences.getFloat("newMoodAvg",0);
        long sadnessCount = sharedPreferences.getInt("Sad",0);
        long angerCount = sharedPreferences.getInt("Anger",0);
        long fearCount = sharedPreferences.getInt("Fear",0);
        Toast.makeText(getActivity(), String.valueOf(currentMoodVal), Toast.LENGTH_SHORT).show();
        if(currentMoodVal>=0.2f){
            moodVal = "Happy"; // happy mood
        }
        else if(currentMoodVal<0.2f && currentMoodVal>=0.2){
            moodVal = "Neutral"; // neutral mood
        }
        else{
            if(fearCount > sadnessCount && fearCount > angerCount){
                moodVal = "Fear"; // fear mood detected
            }
            else if(sadnessCount >= fearCount && sadnessCount >= angerCount){
                moodVal = "Sadness"; // sadness detected
            }
            else {
                moodVal = "Anger"; // anger detected
            }
        }
        moodType.setText(moodVal);
    }

    private void sendEmergencySms(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,getActivity().MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName","");
        float currentMood = sharedPreferences.getFloat("newMoodAvg",0);
        int size = userContacts.size();
        if(currentMood<=0.0001f) {
            for (int index = 0; index < size; index++) {
                String phoneNum = userContacts.get(index).getNumber();
                String message = userName + " is experiencing very low moods for quite a time. We recommend you take care of"
                        + userName + " and contact a therapist.";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNum, null, message, null, null);
            }
        }
    }

    public ArrayList<MyContact> addToContactlist() {

        // on below line we are creating a
        // database for reading our database.
        ContactsListDbHelper dbHelper2 = new ContactsListDbHelper(getActivity());
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

}