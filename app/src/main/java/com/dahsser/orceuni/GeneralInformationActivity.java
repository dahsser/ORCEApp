package com.dahsser.orceuni;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GeneralInformationActivity extends AppCompatActivity {
    String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);
        Intent intent = getIntent();
        String messageText = intent.getStringExtra("response");
        TextView messageView = (TextView)findViewById(R.id.info);
        messageView.setText(messageText);
        getCursos();
    }
    void getCursos(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://www.orce.uni.edu.pe/mobile/api/cursos.json.php";
        final StringRequest stringReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TextView messageView = (TextView)findViewById(R.id.cursos);
                        messageView.setText(response);
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params =  super.getHeaders();
                if(params==null)params = new HashMap<>();
                params.put("Authorization","Your authorization");
                //..add other headers
                return params;
            }
        };
        queue.add(stringReq);
    }
}
