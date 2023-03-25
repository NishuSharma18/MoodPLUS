package com.example.android.moodplus.utility;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class QuizUtils {

    public static Map<String,Map<String,Float >> getQuestions(){
        HashMap<String,Map<String,Float>> questions = new HashMap<>();

        HashMap<String,Float> answer1 = new HashMap<>();
        answer1.put("None of the time",0.2f);
        answer1.put("Some of the time",0.075f);
        answer1.put("Most of the time",0.01f);
        answer1.put("All the time",0.001f);
        questions.put("During the last 7 days, about how often did you feel tired out for no good reason?",answer1);

        HashMap<String,Float> answer2 = new HashMap<>();
        answer2.put("None of the time",0.2f);
        answer2.put("Some of the time",0.05f);
        answer2.put("Most of the time",0.01f);
        answer2.put("All the time",0.001f);
        questions.put("During the last 7 days, about how often did you feel nervous?",answer2);

        HashMap<String,Float> answer3 = new HashMap<>();
        answer3.put("None of the time",0.1f);
        answer3.put("Some of the time",0.01f);
        answer3.put("Most of the time",0.005f);
        answer3.put("All the time",0.0001f);
        questions.put("During the last 7 days, about how often did you feel so nervous that nothing could calm you down?",answer3);

        HashMap<String,Float> answer4 = new HashMap<>();
        answer4.put("None of the time",0.25f);
        answer4.put("Some of the time",0.05f);
        answer4.put("Most of the time",0.005f);
        answer4.put("All the time",0.0001f);
        questions.put("During the last 7 days, about how often did you feel hopeless?",answer4);

        HashMap<String,Float> answer5 = new HashMap<>();
        answer5.put("None of the time",0.2f);
        answer5.put("Some of the time",0.05f);
        answer5.put("Most of the time",0.01f);
        answer5.put("All the time",0.001f);
        questions.put("During the last 7 days, about how often did you feel restless or fidgety?",answer5);

        HashMap<String,Float> answer6 = new HashMap<>();
        answer6.put("None of the time",0.1f);
        answer6.put("Some of the time",0.01f);
        answer6.put("Most of the time",0.005f);
        answer6.put("All the time",0.0001f);
        questions.put("During the last 7 days, about how often did you feel so restless that you could not sit still?",answer6);

        HashMap<String,Float> answer7 = new HashMap<>();
        answer7.put("None of the time",0.01f);
        answer7.put("Some of the time",0.05f);
        answer7.put("Most of the time",0.005f);
        answer7.put("All the time",0.00001f);
        questions.put("During the last 7 days, about how often did you feel depressed?",answer7);

        HashMap<String,Float> answer8 = new HashMap<>();
        answer8.put("None of the time",0.1f);
        answer8.put("Some of the time",0.01f);
        answer8.put("Most of the time",0.005f);
        answer8.put("All the time",0.0001f);
        questions.put("During the last 7 days, about how often did you feel so depressed that nothing could cheer you up?",answer8);

        HashMap<String,Float> answer9 = new HashMap<>();
        answer9.put("None of the time",0.005f);
        answer9.put("Some of the time",0.01f);
        answer9.put("Most of the time",0.1f);
        answer9.put("All the time",0.5f);
        questions.put("During the last 7 days, about how often did you feel that everything was an effort?",answer9);

        HashMap<String,Float> answer10 = new HashMap<>();
        answer10.put("None of the time",0.005f);
        answer10.put("Some of the time",0.1f);
        answer10.put("Most of the time",0.25f);
        answer10.put("All the time",0.75f);
        questions.put("During the last 7 days, about how often did you feel worthless?",answer10);
        return questions;
    }

    public static Map<String,Map<String,Float>> getRandomQuestions(Context context, String subject, int SIZE){
        Map<String,Map<String,Float>> questionsMap = new HashMap<>();
        Map<String, Map<String, Float>> originalQuestion;
        originalQuestion = getQuestions();

        int originalSize =  originalQuestion.size();
        ArrayList<String> keyList = new ArrayList<String>(originalQuestion.keySet());

        while (questionsMap.size()<=SIZE){
            Random random = new Random();
            int randomNumber = random.nextInt(originalSize);
            String question = keyList.get(randomNumber);
            if (!questionsMap.containsKey(question)){
                questionsMap.put(question,originalQuestion.get(question));
            }
        }
        return questionsMap;
    }
}
