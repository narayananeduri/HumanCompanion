package com.example.pet.humanco;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Item_Data extends AppCompatActivity {
    private ProgressDialog pDialog;
    String url;
    JSONObject jsonObject = null;
    String str;
    TextView Type_Item_Data, Breed_Item_Data, Age_Item_Data, Gender_Item_Data, Description_Item_Data, Adopte_Item_Data, Amount_Item_Data, Location_Item_Data;
    String type_Item_Data, breed_Item_Data, age_Item_Data, gender_Item_Data, description_Item_Data, adopte_Item_Data, amount_Item_Data, location_Item_Data, mobile_number;
    RequestQueue requestQueue;
    Button call, makeChat;
    LayoutInflater inflater;
    ImageView SelectedImages1,SelectedImages2,SelectedImages3,SelectedImages4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item__data);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Type_Item_Data = findViewById(R.id.type_Item_Data);
        Breed_Item_Data = findViewById(R.id.breed_Item_Data);
        Age_Item_Data = findViewById(R.id.age_Item_Data);
        Gender_Item_Data = findViewById(R.id.gender_Item_Data);
        Description_Item_Data = findViewById(R.id.description_Item_Data);
        Adopte_Item_Data = findViewById(R.id.adopte_Item_Data);
        Amount_Item_Data = findViewById(R.id.amount_Item_Data);
        Location_Item_Data = findViewById(R.id.location_Item_Data);
        SelectedImages1 = findViewById(R.id.SelectedImages1);
        SelectedImages2 = findViewById(R.id.SelectedImages2);
        SelectedImages3 = findViewById(R.id.SelectedImages3);
        SelectedImages4 = findViewById(R.id.SelectedImages4);
        call = findViewById(R.id.makeCall);
        makeChat = findViewById(R.id.makeChat);
        requestQueue = Volley.newRequestQueue(this);
        Bundle gt = getIntent().getExtras();
        assert gt != null;
        str = gt.getString("abc");
        url = getString(R.string.BasicURL) + "/pets/id/" + str;
        new GetPetsData().execute();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isPermissionGranted()) {
                call_action();
            }
            }
        });
        makeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Item_Data.this, "Sorry....! We will back soon...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Item_Data.this.finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void call_action() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobile_number));
        startActivity(callIntent);
    }
    @SuppressLint("StaticFieldLeak")
    private class GetPetsData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Item_Data.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        JSONObject json_result1 = jsonObject.getJSONObject("result");

                        if (json_result.equals("200")) {
                            type_Item_Data = json_result1.getString("type_name");
                            breed_Item_Data = json_result1.getString("breend_name");
                            age_Item_Data = json_result1.getString("age");
                            gender_Item_Data = json_result1.getString("gender");
                            description_Item_Data = json_result1.getString("description");
                            adopte_Item_Data = json_result1.getString("adopte");
                            amount_Item_Data = json_result1.getString("amount");
                            location_Item_Data = json_result1.getString("location");
                            mobile_number = json_result1.getString("mobile_number");

                            Type_Item_Data.setText(type_Item_Data);
                            getSupportActionBar().setTitle(type_Item_Data);//used to set name
                            Breed_Item_Data.setText(breed_Item_Data);
                            Age_Item_Data.setText(age_Item_Data);
                            if("0".equalsIgnoreCase(gender_Item_Data)){
                                Gender_Item_Data.setText("Male");
                            }else if("1".equalsIgnoreCase(gender_Item_Data)){
                                Gender_Item_Data.setText("Female");
                            }
                            Description_Item_Data.setText(description_Item_Data);
                            Adopte_Item_Data.setText(adopte_Item_Data);
                            Amount_Item_Data.setText(amount_Item_Data);
                            Location_Item_Data.setText(location_Item_Data);

                            JSONArray jsonArray = json_result1.getJSONArray("images");
                            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            if (jsonArray.get(0) != null) {
                                SelectedImages1.setVisibility(View.VISIBLE);
                                Glide.with(Item_Data.this).load(jsonArray.get(0))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(SelectedImages1);
                            } else {
                                SelectedImages1.setVisibility(View.GONE);
                            }
                            if (jsonArray.get(1) != null) {
                                SelectedImages2.setVisibility(View.VISIBLE);
                                Glide.with(Item_Data.this).load(jsonArray.get(1))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(SelectedImages2);
                            } else {
                                SelectedImages2.setVisibility(View.GONE);
                            }
                            if (jsonArray.get(2) != null) {
                                SelectedImages3.setVisibility(View.VISIBLE);
                                Glide.with(Item_Data.this).load(jsonArray.get(2))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(SelectedImages3);
                            } else {
                                SelectedImages3.setVisibility(View.GONE);

                            }
                            if (jsonArray.get(3) != null) {
                                SelectedImages4.setVisibility(View.VISIBLE);
                                Glide.with(Item_Data.this).load(jsonArray.get(3))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(SelectedImages4);
                            } else {
                                SelectedImages4.setVisibility(View.GONE);

                            }
                            pDialog.dismiss();


                        /*for(int i = 0; i < 2; i++){

                            myView = inflater.inflate(R.layout.image_list_items, null);
                            ImageView SelectedImages1 = (ImageView)myView.findViewById(R.id.SelectedImages1);
                            Glide.with(Item_Data.this).load(jsonArray.get(i))
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(SelectedImages1);
                            ImageLinearLayOut.addView(myView);
                        }
                        for(int j=2;j<jsonArray.length();j++){
                            myView1 = inflater.inflate(R.layout.image_list_items, null);
                            ImageView SelectedImages1 = (ImageView)myView1.findViewById(R.id.SelectedImages1);
                            Glide.with(Item_Data.this).load(jsonArray.get(j))
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(SelectedImages1);
                            ImageLinearLayOut1.addView(myView1);
                        }*/
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(Item_Data.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        pDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            requestQueue.add(stringRequest);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
