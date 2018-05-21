package com.example.pet.humanco;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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

public class Signup extends AppCompatActivity {

    TextView link_login;
    EditText name, Email, mobile, password, Re_EnterPassword;
    Button Create_Account;
    String name_S, Email_S, mobile_S, password_S, Re_EnterPassword_S;
    TextInputLayout Name,EmailId,Mobile_No,Password,RE_EnterPassword;
    private ProgressDialog pDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String MobilePattern = "[0-9]{10}";
    // url to create new product
    String String_url;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    JSONObject jsonObject;
    AdView mAdView;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        requestQueue = Volley.newRequestQueue(this);
        link_login = findViewById(R.id.link_login_SignUp);
        name = findViewById(R.id.input_name_SignUp);
        Email = findViewById(R.id.input_email_SignUp);
        mobile = findViewById(R.id.input_mobile_SignUp);
        password = findViewById(R.id.input_password_SignUp);
        Re_EnterPassword = findViewById(R.id.input_reEnterPassword_SignUp);
        Create_Account = findViewById(R.id.btn_signup_SignUp);
        Name = findViewById(R.id.Name_Input);
        EmailId = findViewById(R.id.Email_Id_Input);
        Mobile_No = findViewById(R.id.Mobile_Input);
        Password = findViewById(R.id.Password_Input);
        RE_EnterPassword = findViewById(R.id.Re_Enter_Input);
        mAdView = findViewById(R.id.adView);
        String_url = getString(R.string.BasicURL)+"/userlogin/register";
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("SignUp");
        }
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, Login.class));
                finish();
                overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
            }
        });
        Create_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_S = name.getText().toString();
                Email_S = Email.getText().toString();
                mobile_S = mobile.getText().toString();
                password_S = password.getText().toString();
                Re_EnterPassword_S = Re_EnterPassword.getText().toString();


                if (name_S.isEmpty()) {
                    name.requestFocus();
                    Name.setError("Field Should not Empty");
                } else {
                    Name.setError("");
                    if (Email_S.isEmpty()) {
                        Email.requestFocus();
                        EmailId.setError("Field Should not Empty");
                    } else {
                        if (!Email_S.matches(emailPattern) && Email_S.length() > 0) {
                            Email.requestFocus();
                            EmailId.setError("Please Enter Email Format");
                        } else {
                            EmailId.setError("");
                            if (mobile_S.isEmpty()) {
                                mobile.requestFocus();
                                Mobile_No.setError("Field Should not Empty");
                            } else {
                                if (!(mobile_S.length() == 10)) {
                                    mobile.requestFocus();
                                    Mobile_No.setError("Please Enter Correct Phone");
                                    mobile.setText("");
                                } else {
                                    Mobile_No.setError("");
                                    if (password_S.isEmpty()) {
                                        password.requestFocus();
                                        Password.setError("Field Should not Empty");
                                    } else {
                                        if (!(password_S.length() >= 6)) {
                                            password.requestFocus();
                                            Password.setError("Password contains minimum 6 characters");
                                            password.setText("");
                                        } else {
                                            Password.setError("");
                                            if (Re_EnterPassword_S.isEmpty()) {
                                                Re_EnterPassword.requestFocus();
                                                RE_EnterPassword.setError("Field Should not Empty");
                                            } else {
                                                RE_EnterPassword.setError("");
                                                if (Re_EnterPassword_S.length() > 0 && password_S.length() > 0) {
                                                    if (!Re_EnterPassword_S.equals(password_S)) {
                                                        RE_EnterPassword.setError("Password Mismatch");
                                                        Password.requestFocus();
                                                        Re_EnterPassword.setText("");
                                                        password.setText("");

                                                    } else {
                                                        RE_EnterPassword.setError("");
                                                        IsPermission_Available();


                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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
    @Override
    protected void onStart() {
        super.onStart();
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class signUp extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Signup.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            stringRequest = new StringRequest(Request.Method.POST, String_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        if (json_result.equals("200")) {
                            startActivity(new Intent(Signup.this, Login.class));
                            finish();
                            pDialog.dismiss();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(Signup.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    hashMap.put("name", name_S);
                    hashMap.put("email", Email_S);
                    hashMap.put("mobile_number", mobile_S);
                    hashMap.put("user_password", password_S);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            Signup.this.finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onOptionsItemSelected(item);
    }
    public void IsPermission_Available() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new signUp().execute();
            } else {
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }