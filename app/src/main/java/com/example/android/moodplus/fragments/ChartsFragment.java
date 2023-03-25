package com.example.android.moodplus.fragments;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moodplus.R;
import com.example.android.moodplus.activities.QuizActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class ChartsFragment extends Fragment {

    // String keys for shared preferences
    private final String angry = "Angry";
    private final String disgust = "Disgust";
    private final String fear = "Fear";
    private final String happy = "Happy";
    private final String neutral = "Neutral";
    private final String sad = "Sad";
    private final String surprise = "Surprise";

    public static final String SHARED_PREFS = "sharedPrefs";
    private TextView startQuizView;

    PieChart pieChart;
    int [] colorClassArray = new int[] {Color.GREEN,Color.BLUE,Color.MAGENTA, Color.LTGRAY,Color.YELLOW,Color.RED,Color.DKGRAY};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_charts, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, getActivity().MODE_PRIVATE);

        int happyVal = sharedPreferences.getInt(happy,0);
        int disgustVal = sharedPreferences.getInt(disgust,0);
        int fearVal = sharedPreferences.getInt(fear,0);
        int sadVal = sharedPreferences.getInt(sad,0);
        int surpriseVal = sharedPreferences.getInt(surprise,0);
        int angryVal = sharedPreferences.getInt(angry,0);
        int neutralVal = sharedPreferences.getInt(neutral,0);

        pieChart = getView().findViewById(R.id.pieChart);
        startQuizView = getView().findViewById(R.id.start_quiz_tv);

        // moving to quiz activity
        startQuizView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),QuizActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<PieEntry> dataValsPie = new ArrayList<>();
        dataValsPie.add(new PieEntry(happyVal,"Happy"));
        dataValsPie.add(new PieEntry(sadVal,"Sad"));
        dataValsPie.add(new PieEntry(neutralVal,"Neutral"));
        dataValsPie.add(new PieEntry(fearVal,"Fear"));
        dataValsPie.add(new PieEntry(disgustVal,"Disgust"));
        dataValsPie.add(new PieEntry(angryVal,"Anger"));
        dataValsPie.add(new PieEntry(surpriseVal,"Surprise"));


        PieDataSet pieDataSet = new PieDataSet(dataValsPie,"");
        pieDataSet.setColors(colorClassArray);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Mood Data");
        pieChart.setCenterTextSize(15 );
        pieChart.setCenterTextRadiusPercent(55);
        pieChart.setHoleRadius(30);
        pieChart.setTransparentCircleRadius(50);

    }

}