package com.example.pet.humanco;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText userName, password;
    Button logIn;
    TextView createAccount,link_forgotPassword;
    private ProgressDialog pDialog;
    String String_url;

    String UserName, Password;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    JSONObject jsonObject;

    // Session Manager Class
    SessionManagement session;
    LinearLayout llSignUpLayout;
    AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(this);
        String_url = getString(R.string.BasicURL)+"/userlogin/login";
        llSignUpLayout = findViewById(R.id.ll_sign_up);
        userName =  findViewById(R.id.input_userName);
        password = findViewById(R.id.login_password);
        logIn = findViewById(R.id.btn_login);
        createAccount = findViewById(R.id.link_signup);
        link_forgotPassword = findViewById(R.id.link_forgotPassword);
        mAdView = findViewById(R.id.adView);
        session = new SessionManagement(getApplicationContext());
        if (session.isLoggedIn()) {
            startActivity(new Intent(this, HomePage.class));
            finish();
        }
        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserName = userName.getText().toString().trim();
                Password = password.getText().toString().trim();
                if (UserName.isEmpty()) {
                    userName.requestFocus();
                    userName.setError("Please Enter Value");
                } else if (Password.isEmpty()) {
                    password.requestFocus();
                    password.setError("Please Enter Value");
                } else {
                    IsPermission_Available();
                }
            }
        });


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Signup.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        link_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173713");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                finish();
            }
        });
    }
    public void showInterstitial()
    {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        showInterstitial();
    }
    @SuppressLint("StaticFieldLeak")
    private class login extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            stringRequest = new StringRequest(Request.Method.POST, String_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        if (json_result.equals("200")) {
                            JSONObject object = jsonObject.getJSONObject("userdata");
                            String json_result1 = object.getString("user_id");
                            Intent intent = new Intent(Login.this, HomePage.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("stuff", json_result1);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                            pDialog.dismiss();
                            //Toast.makeText(Login.this, "User Id : "+object.getString("user_id")+"\n"+object.getString("uemail")+"\n"+object.getString("umoblie")+"\n"+object.getBoolean("t_loggedin"), Toast.LENGTH_SHORT).show();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            session.createLoginSession(object.getString("user_id"), object.getString("uemail"), object.getString("umoblie"), object.getBoolean("t_loggedin"));
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("emailmobile", UserName);
                    hashMap.put("userpassword", Password);
                    return hashMap;
                }
            };
            requestQueue.add(stringRequest);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void IsPermission_Available() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new login().execute();
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
