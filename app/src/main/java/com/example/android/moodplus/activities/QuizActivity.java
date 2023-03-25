package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moodplus.R;
import com.example.android.moodplus.utility.QuizUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {
    private int currentQuestionIndex = 0;
    private TextView tvQuestion, tvQuestionNumber;
    private Button btnNext;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private List<String> questions;
    private Float moodResult = 0.0f;
    private Map<String, Map<String, Float>> questionsAnswerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionsAnswerMap = QuizUtils.getRandomQuestions(this,"",7);
        questions = new ArrayList<>(questionsAnswerMap.keySet());

        tvQuestion = findViewById(R.id.testQuestion);
        tvQuestionNumber = findViewById(R.id.currentQuestion);
        btnNext = findViewById(R.id.btnNextQuestion);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if(selectedId==-1){
                    Toast.makeText(getApplicationContext(), "Nothing is selected", Toast.LENGTH_SHORT).show();
                    selectedId = radioGroup.getCheckedRadioButtonId();
                }
                else {
                    RadioButton radioButton = findViewById(selectedId);
                    Float answer = questionsAnswerMap.get(questions.get(currentQuestionIndex)).get(radioButton.getText());
                    moodResult += answer;
                    currentQuestionIndex++;


                    if (btnNext.getText().equals("Next")) {
                        displayNextQuestions();
                        radioGroup.clearCheck();
                    } else {
                        Intent intentResult = new Intent(getApplicationContext(), ResultActivity.class);
                        intentResult.putExtra("getMood", moodResult);
                        intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentResult);
                        finish();
                    }
                }

            }
        });
        displayData();
    }

    private void displayNextQuestions() {
        setAnswersToRadioButton();
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText("Current Question: " + (currentQuestionIndex + 1));

        if (currentQuestionIndex == 6){
            btnNext.setText("Finish");
        }
    }

    private void displayData() {
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText("Current Question: " + (currentQuestionIndex + 1));
        setAnswersToRadioButton();
    }

    private void setAnswersToRadioButton(){
        ArrayList<String> questionKey = new ArrayList(questionsAnswerMap.get(questions.get(currentQuestionIndex)).keySet());
        radioButton1.setText(questionKey.get(0));
        radioButton2.setText(questionKey.get(1));
        radioButton3.setText(questionKey.get(2));
        radioButton4.setText(questionKey.get(3));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),GratitudeActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}