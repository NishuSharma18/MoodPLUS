package com.example.android.moodplus.fragments;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.betaproject.model.Message;
import com.example.android.betaproject.adapter.MessageAdapter;
import com.example.android.betaproject.helper.MySingleton;
import com.example.android.betaproject.R;
import com.example.android.moodplus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatGptFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private EditText mEditText;
    private Button mButton;
    private String apiUrl = "https://api.openai.com/v1/completions";
    private String accessToken = getResources().getString(R.string.ChatGpt_key);
    private List < Message > mMessages;

    private TextView chatSuggestions;
    private ArrayList<String> sadSuggestionsList;
    private ArrayList<String> happySuggestionsList;
    private ArrayList<String> fearSuggestionsList;
    private ArrayList<String> angerSuggestionList;
    private int suggestionNo = 0;
    private int moodVal;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_chat_gpt, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = getView().findViewById(R.id.show_msg_recycler);
        mEditText = getView().findViewById(R.id.write_ed);
        mButton = getView().findViewById(R.id.send_button);

        mMessages = new ArrayList<>();
        mAdapter = new MessageAdapter(mMessages);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        chatSuggestions = getView().findViewById(R.id.chat_suggestions_tv);
        sadSuggestionsList = new ArrayList<>();
        happySuggestionsList = new ArrayList<>();
        fearSuggestionsList = new ArrayList<>();
        angerSuggestionList = new ArrayList<>();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAPI("");
            }
        });
        getCurrentMood();

        sadSuggestionsList.add("I'm a bit sad today can you help me get away with my sadness.");
        sadSuggestionsList.add("Ok fine. Tell me some jokes to cheer up my mood.");
        sadSuggestionsList.add("Feeling better. Suggest me some tips to remain joyful around the clock.");
        sadSuggestionsList.add("Can you ask me some questions to cheer up my mood?");
        sadSuggestionsList.add("Provide me with some motivational thoughts.");

        happySuggestionsList.add("Hi I am happy today, how can I share my happiness with others and spread positivity in the world?");
        happySuggestionsList.add("What are some things I can be grateful for in my life right now?");
        happySuggestionsList.add("Suggest me some tips to remain happy round the clock.");
        happySuggestionsList.add("Can you recommend any uplifting books, movies, or songs that might complement my current mood?");
        happySuggestionsList.add("How can I use my current happiness to fuel my future goals and aspirations?");

        fearSuggestionsList.add("Something is there that causes me to fear. Whom should I express my thoughts to?");
        fearSuggestionsList.add("How can I identify the triggers that cause me to feel afraid?");
        fearSuggestionsList.add("Feeling better. Suggest me some tips and techniques to reduce my fear.");
        fearSuggestionsList.add("Can you ask me some questions to cheer up my mood?");
        fearSuggestionsList.add("Provide me some motivational quotes that helps me reduce my fear.");

        angerSuggestionList.add("I'm a bit angry today can you help me get away with my anger");
        angerSuggestionList.add("What are some effective ways to calm down when I'm feeling angry?");
        angerSuggestionList.add("Suggest me some tips and techniques to manage my anger next time.");
        angerSuggestionList.add("How can I communicate my anger in a healthy way without lashing out at others?");
        angerSuggestionList.add("How can I identify the triggers that cause me to feel angry?");


        chatSuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moodVal==1){
                    String userSuggestionText = sadSuggestionsList.get(suggestionNo);
                    callAPI(userSuggestionText);
                    suggestionNo = (suggestionNo+1)%5;
                    chatSuggestions.setText(sadSuggestionsList.get(suggestionNo));
                }
                else if(moodVal==2){
                    String userSuggestionText = happySuggestionsList.get(suggestionNo);
                    callAPI(userSuggestionText);
                    suggestionNo = (suggestionNo+1)%5;
                    chatSuggestions.setText(happySuggestionsList.get(suggestionNo));
                }
                else if(moodVal==3){
                    String userSuggestionText = fearSuggestionsList.get(suggestionNo);
                    callAPI(userSuggestionText);
                    suggestionNo = (suggestionNo+1)%5;
                    chatSuggestions.setText(fearSuggestionsList.get(suggestionNo));
                }
                else if(moodVal==4){
                    String userSuggestionText = angerSuggestionList.get(suggestionNo);
                    callAPI(userSuggestionText);
                    suggestionNo = (suggestionNo+1)%5;
                    chatSuggestions.setText(angerSuggestionList.get(suggestionNo));
                }
            }
        });
    }

    private void callAPI(String userSuggestionText) {
        String text;
        if(!userSuggestionText.equals("")){
            text = userSuggestionText;
        }
        else{
            text = mEditText.getText().toString();
        }
        mMessages.add(new Message(text, true));
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        mEditText.getText().clear();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("model", "text-davinci-003");
            requestBody.put("prompt", text);
            requestBody.put("max_tokens", 4076);
            requestBody.put("temperature", 1);
            requestBody.put("top_p", 1);
            requestBody.put("n", 1);
            requestBody.put("stream", false);
            requestBody.put("logprobs", null);
            requestBody.put("stop", ".");


//            requestBody.put("model", "text-davinci-003");
//            requestBody.put("prompt", text);
//            requestBody.put("max_tokens", 100);
//            requestBody.put("temperature", 1);
//            requestBody.put("top_p", 1);
//            requestBody.put("frequency_penalty", 0.0);
//            requestBody.put("presence_penalty", 0.0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, requestBody, new Response.Listener <JSONObject> () {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray choicesArray = response.getJSONArray("choices");
                    JSONObject choiceObject = choicesArray.getJSONObject(0);
                    String text = choiceObject.getString("text");
                    Log.e("API Response", response.toString());
                    //Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();
                    mMessages.add(new Message(text.replaceFirst("\n", "").replaceFirst("\n", ""), false));
                    mAdapter.notifyItemInserted(mMessages.size() - 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", error.toString());
            }
        }) {
            @Override
            public Map < String, String > getHeaders() throws AuthFailureError {
                Map < String, String > headers = new HashMap < > ();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            protected Response <JSONObject> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };
        int timeoutMs = 25000; // 25 seconds timeout
        RetryPolicy policy = new DefaultRetryPolicy(timeoutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        // Add the request to the RequestQueue
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void getCurrentMood(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, getActivity().MODE_PRIVATE);
        float currentMoodVal = sharedPreferences.getFloat("newMoodAvg",0);
        long sadnessCount = sharedPreferences.getInt("Sad",0);
        long angerCount = sharedPreferences.getInt("Anger",0);
        long fearCount = sharedPreferences.getInt("Fear",0);
        Toast.makeText(getActivity(), String.valueOf(currentMoodVal), Toast.LENGTH_SHORT).show();
        if(currentMoodVal>=0.2f){
            moodVal = 2; // happy mood
        }
        else if(currentMoodVal<0.2f && currentMoodVal>=0.2){
            // neutral mood
        }
        else{
            if(fearCount > sadnessCount && fearCount > angerCount){
                moodVal = 3; // fear mood detected
            }
            else if(sadnessCount >= fearCount && sadnessCount >= angerCount){
                moodVal = 1; // sadness detected
            }
            else {
                moodVal = 4; // anger detected
            }
        }
    }
}
