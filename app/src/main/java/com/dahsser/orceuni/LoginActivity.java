package com.dahsser.orceuni;

import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import android.content.Intent;

public class LoginActivity extends AppCompatActivity {
    String query = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void LoginOrce(View view){
        TextView cod = (TextView)findViewById(R.id.codigo);
        final String codigo = cod.getText().toString();
        TextView pass = (TextView)findViewById(R.id.password);
        final String password = pass.getText().toString();
        final TextView mTextView = (TextView) findViewById(R.id.Log);
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://www.orce.uni.edu.pe/mobile/api/login.json.php";
        final StringRequest stringReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject mainObject = null;
                        String  error="";
                        String msg="";
                        try {
                            mainObject = new JSONObject(response);
                            error = mainObject.getString("error");
                            msg = mainObject.getString("msg");
                        } catch (JSONException e) {
                            mTextView.setText(e.toString());
                            e.printStackTrace();
                        }
                        if(error == "true"){
                            mTextView.setText(msg);
                        }else{
                            callIntent(response);
                        }
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("cod", codigo);
                params.put("psw", md5(password));
                params.put("sesion", "1");

                return params;
            }
        };
        queue.add( stringReq);

    }
    public void callIntent(String response){
        Intent intent = new Intent(this, GeneralInformationActivity.class);
        intent.putExtra("response", response);
        startActivity(intent);

    }
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
