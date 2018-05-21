package com.example.pet.humanco;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class My_Profile extends AppCompatActivity
{
    TextView UserName,EMail,MobileNo;
    Button update,updatePassword;
    EditText Username,email,mobileno,oldPassword,newPassword,reEnterPassword;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    String name,data_Url,userName,eMail,mobileNo,Uname,eMAIL,mobile_No,Update_Url,OldPassword_PSW,NewPassword_PSW,ReEnterPassword_PSW;
    SessionManagement session;
    JSONObject jsonObject,object;
    Dialog dialog;
    AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__profile);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManagement.userId);
        UserName = findViewById(R.id.Name_P);
        EMail = findViewById(R.id.e_Mail_P);
        MobileNo = findViewById(R.id.mobile_P);
        update = findViewById(R.id.updateBtn);
        updatePassword = findViewById(R.id.utdPwdBtn);
        mAdView = findViewById(R.id.adView);

        requestQueue = Volley.newRequestQueue(this);
        data_Url = getString(R.string.BasicURL)+"/userlogin/"+name;
        Update_Url = getString(R.string.BasicURL)+"/userlogin/update/"+name;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, data_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonObject = new JSONObject(response);
                    object = jsonObject.getJSONObject("userdata");
                    String json_result = jsonObject.getString("status");
                    if (json_result.equals("200"))
                    {
                        try {
                            userName = object.getString("name");
                            eMail = object.getString("email");
                            mobileNo = object.getString("mobile_number");
                            UserName.setText(userName);
                            EMail.setText(eMail);
                            MobileNo.setText(mobileNo);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        Toast.makeText(My_Profile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(My_Profile.this);
                dialog.setContentView(R.layout.update_profile);
                //dialog.setTitle("Update Profile");

                // set the custom dialog components - text, image and button
                Button updateProfile = dialog.findViewById(R.id.btn_update_Update);
                Button cancel = dialog.findViewById(R.id.btn_Cancel_Update);
                Username = dialog.findViewById(R.id.input_name_Update);
                email = dialog.findViewById(R.id.input_email_Update);
                mobileno = dialog.findViewById(R.id.input_mobile_Update);
                // if button is clicked, close the custom dialog
                updateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Uname = Username.getText().toString();
                        eMAIL = email.getText().toString();
                        mobile_No = mobileno.getText().toString();

                        if (Uname.isEmpty())
                        {
                            Username.setError("Please Enter Name");
                            Username.requestFocus();
                        }
                        else
                        {
                            if (eMAIL.isEmpty())
                            {
                                email.setError("Please Enter E mail");
                                email.requestFocus();
                            }
                            else
                            {
                                if (mobile_No.isEmpty())
                                {
                                    mobileno.setError("Please Enter Mobile No");
                                    mobileno.requestFocus();
                                }
                                else
                                {
                                    new UpdateProfile().execute();

                                }
                            }
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(My_Profile.this);
                dialog.setContentView(R.layout.password_update_profile);
                //dialog.setTitle("Update Profile");

                // set the custom dialog components - text, image and button
                Button updateProfile = dialog.findViewById(R.id.btn_update_Update_PSW);
                Button cancel = dialog.findViewById(R.id.btn_Cancel_Update_PSW);
                oldPassword = dialog.findViewById(R.id.input_old_password_Update);
                newPassword = dialog.findViewById(R.id.input_new_password_Update);
                reEnterPassword = dialog.findViewById(R.id.input_re_enter_Update);
                // if button is clicked, close the custom dialog
                updateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OldPassword_PSW = oldPassword.getText().toString();
                        NewPassword_PSW = newPassword.getText().toString();
                        ReEnterPassword_PSW = reEnterPassword.getText().toString();

                        if (OldPassword_PSW.isEmpty())
                        {
                            oldPassword.setError("Please Enter Old Password");
                            oldPassword.requestFocus();
                        }
                        else
                        {
                            if (NewPassword_PSW.isEmpty())
                            {
                                newPassword.setError("Please Enter New Password");
                                newPassword.requestFocus();
                            }
                            else
                            {
                                if (!ReEnterPassword_PSW.equals(NewPassword_PSW))
                                {
                                    reEnterPassword.setError("Password Mismatch");
                                    reEnterPassword.requestFocus();
                                    oldPassword.setText("");
                                    newPassword.setText("");
                                    reEnterPassword.setText("");
                                }
                                else
                                {
                                    new UpdatePassword().execute();
                                }
                            }
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(My_Profile.this);
                dialog.setContentView(R.layout.update_profile);
                //dialog.setTitle("Update Profile");
                // set the custom dialog components - text, image and button
                Button updateProfile = dialog.findViewById(R.id.btn_update_Update);
                Button cancel = dialog.findViewById(R.id.btn_Cancel_Update);
                Username = dialog.findViewById(R.id.input_name_Update);
                email = dialog.findViewById(R.id.input_email_Update);
                mobileno = dialog.findViewById(R.id.input_mobile_Update);
                Username.setText(userName);
                email.setText(eMail);
                mobileno.setText(mobileNo);
                // if button is clicked, close the custom dialog
                updateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        Uname = Username.getText().toString();
                        eMAIL = email.getText().toString();
                        mobile_No = mobileno.getText().toString();

                        if (Uname.isEmpty())
                        {
                            Username.setError("Please Enter Name");
                            Username.requestFocus();
                        }
                        else
                        {
                            if (eMAIL.isEmpty())
                            {
                                email.setError("Please Enter E-mail");
                                email.requestFocus();
                            }
                            else
                            {
                                if (mobile_No.isEmpty())
                                {
                                    mobileno.setError("Please Enter Mobile No");
                                    mobileno.requestFocus();
                                }
                                else
                                {
                                    new UpdateProfile().execute();

                                }
                            }
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            My_Profile.this.finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("StaticFieldLeak")
    private class UpdateProfile extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(My_Profile.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            stringRequest = new StringRequest(Request.Method.POST, Update_Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        if (json_result.equals("200")) {
                            startActivity(new Intent(My_Profile.this,My_Profile.class));
                            finish();
                            pDialog.dismiss();
                            dialog.dismiss();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            Toast.makeText(My_Profile.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

                        } else {
                            pDialog.dismiss();
                            Toast.makeText(My_Profile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    hashMap.put("name", Uname);
                    hashMap.put("email", eMAIL);
                    hashMap.put("mobile_number", mobile_No);
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
    @SuppressLint("StaticFieldLeak")
    private class UpdatePassword extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(My_Profile.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            stringRequest = new StringRequest(Request.Method.POST, getString(R.string.BasicURL)+"/userlogin/updatepassword", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        if (json_result.equals("200")) {
                            Toast.makeText(My_Profile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(My_Profile.this, Login.class));
                            finish();
                            pDialog.dismiss();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(My_Profile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    hashMap.put("uid",name );
                    hashMap.put("old_password",OldPassword_PSW );
                    hashMap.put("new_password", ReEnterPassword_PSW);
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
}