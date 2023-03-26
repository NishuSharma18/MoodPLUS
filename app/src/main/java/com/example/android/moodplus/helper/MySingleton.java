package com.example.android.moodplus.helper;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static MySingleton instance;
    private RequestQueue requestQueue;
    private static Context context;
    private MySingleton(Context ctx) {
        context = ctx;
        requestQueue = getRequestQueue();
    }
    public static synchronized MySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new MySingleton(context);
        }
        return instance;
    }
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public < T > void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
