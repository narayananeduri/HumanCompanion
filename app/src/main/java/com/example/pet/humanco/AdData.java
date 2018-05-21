package com.example.pet.humanco;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

public class AdData extends AppCompatActivity {
    private ProgressDialog pDialog;
    // URL to get contacts JSON
    String DeleteUrl,UpdateUrl;
    JSONObject jsonObject = null;
    String str, genderSelected=null, Type_String, Breed_String,Age_String,Description_String,location_String;
    TextView Type_Item_Data, Breed_Item_Data, Age_Item_Data, Gender_Item_Data, Description_Item_Data, Adopte_Item_Data, Amount_Item_Data, Location_Item_Data;
    String GetURL,type_Item_Data,mobile_number, breed_Item_Data, age_Item_Data, gender_Item_Data, description_Item_Data, adopte_Item_Data, amount_Item_Data, location_Item_Data;
    RequestQueue requestQueue;
    Button call, Post_UP;
    EditText TypeUP, BreedUP, ageUP, DescriptionUP, locationUP;
    Spinner genderSpinnerUP;
    String GenderData[] = {"Select Gender","Male","Female"};
    Button updateAdData,DeleteAdData;
    Dialog dialog;
    ImageView SelectedImages1,SelectedImages2,SelectedImages3,SelectedImages4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_data);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Type_Item_Data =  findViewById(R.id.type_Item_DataAdv);
        Breed_Item_Data = findViewById(R.id.breed_Item_DataAdv);
        Age_Item_Data = findViewById(R.id.age_Item_DataAdv);
        Gender_Item_Data = findViewById(R.id.gender_Item_DataAdv);
        Description_Item_Data = findViewById(R.id.description_Item_DataAdv);
        Amount_Item_Data = findViewById(R.id.amount_Item_DataAdv);
        Adopte_Item_Data = findViewById(R.id.adopte_Item_DataAdv);
        Location_Item_Data = findViewById(R.id.Location_Item_Data);
        updateAdData = findViewById(R.id.updateAdData);
        DeleteAdData = findViewById(R.id.DeleteAdData);
        call = findViewById(R.id.makeCall);
        SelectedImages1 = findViewById(R.id.SelectedImages1);
        SelectedImages2 = findViewById(R.id.SelectedImages2);
        SelectedImages3 = findViewById(R.id.SelectedImages3);
        SelectedImages4 = findViewById(R.id.SelectedImages4);
        requestQueue = Volley.newRequestQueue(this);
        Bundle gt = getIntent().getExtras();
        assert gt != null;
        str = gt.getString("abc");
        int str1 = Integer.parseInt(str);
        Log.e("Url: ",str);
        GetURL = getString(R.string.BasicURL)+"/pets/id/" + str1;
        DeleteUrl = getString(R.string.BasicURL)+"/pets/" + str1;
        UpdateUrl = getString(R.string.BasicURL)+"/pets/update/"+str;
        Log.e("Urlstr1 ",""+UpdateUrl);
        IsPermission_Available();

        updateAdData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog = new Dialog(AdData.this);
                dialog.setContentView(R.layout.pet_update);
                TypeUP = dialog.findViewById(R.id.type_UP);
                BreedUP = dialog.findViewById(R.id.breed_UP);
                ageUP = dialog.findViewById(R.id.age_UP);
                DescriptionUP = dialog.findViewById(R.id.description_UP);
                locationUP = dialog.findViewById(R.id.location_UP);
                Post_UP = dialog.findViewById(R.id.Post_UP);
                genderSpinnerUP = dialog.findViewById(R.id.genderSpinner_UP);
                ArrayAdapter arrayAdapter = new ArrayAdapter(AdData.this,android.R.layout.simple_dropdown_item_1line,GenderData);
                genderSpinnerUP.setAdapter(arrayAdapter);
                TypeUP.setText(type_Item_Data);
                BreedUP.setText(breed_Item_Data);
                ageUP.setText(age_Item_Data);
                DescriptionUP.setText(description_Item_Data);
                locationUP.setText(location_Item_Data);
                genderSpinnerUP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        switch (position) {
                            case 0: {

                                genderSelected = "null";
                                break;
                            }
                            case 1: {

                                genderSelected = "0";
                                break;
                            }
                            case 2: {
                                genderSelected = "1";
                                break;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Post_UP.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Type_String = TypeUP.getText().toString();
                        Breed_String = BreedUP.getText().toString();
                        Age_String = ageUP.getText().toString();
                        Description_String = DescriptionUP.getText().toString();
                        location_String = locationUP.getText().toString();


                        if (Type_String.isEmpty())
                        {
                            TypeUP.requestFocus();
                            TypeUP.setError("Please Enter Values");
                        }
                        else
                        {
                            if (Breed_String.isEmpty())
                            {
                                BreedUP.requestFocus();
                                BreedUP.setError("Please Enter Values");
                            }
                            else
                            {
                                if (Age_String.isEmpty())
                                {
                                    ageUP.requestFocus();
                                    ageUP.setError("Please Enter Values");
                                }
                                else
                                {
                                    if (Description_String.isEmpty())
                                    {
                                        DescriptionUP.requestFocus();
                                        DescriptionUP.setError("Please Enter Values");
                                    }
                                    else
                                    {
                                        if (location_String.isEmpty())
                                        {
                                            locationUP.requestFocus();
                                            locationUP.setError("Please Enter Values");
                                        }
                                        else
                                        {
                                            if (genderSelected == null )
                                            {
                                                Toast.makeText(AdData.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                new UpdateAdData().execute();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                dialog.show();
            }
        });
        DeleteAdData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdData.this)
                        .setTitle("Delete Pet")
                        .setMessage("Do you really want to Delete?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                new DeletePet().execute();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            AdData.this.finish();
            startActivity(new Intent(AdData.this,My_Ads.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("StaticFieldLeak")
    class UpdateAdData extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(AdData.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, UpdateUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        if (json_result.equals("200")) {
                            startActivity(new Intent(AdData.this,My_Ads.class));
                            finish();
                            pDialog.dismiss();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            Toast.makeText(AdData.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(AdData.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    hashMap.put("type_name",Type_String);
                    hashMap.put("breend_name",Breed_String);
                    hashMap.put("age",Age_String);
                    hashMap.put("gender",genderSelected); //0: male, 1: female
                    hashMap.put("description",Description_String);
                    hashMap.put("location",location_String);
                    hashMap.put("amount", amount_Item_Data);
                    hashMap.put("adopte",adopte_Item_Data);
                    return hashMap;
                }
            };
            requestQueue.add(stringRequest1);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    private class GetPetsData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AdData.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0)
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, GetURL, new Response.Listener<String>() {
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
                            Log.e("Gender",gender_Item_Data);
                            Description_Item_Data.setText(description_Item_Data);
                            Adopte_Item_Data.setText(adopte_Item_Data);
                            Amount_Item_Data.setText(amount_Item_Data);
                            Location_Item_Data.setText(location_Item_Data);
                            pDialog.dismiss();

                            JSONArray jsonArray = json_result1.getJSONArray("images");
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            if (jsonArray.get(0) != null) {
                                SelectedImages1.setVisibility(View.VISIBLE);
                                Glide.with(AdData.this).load(jsonArray.get(0))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(SelectedImages1);
                            } else {
                                SelectedImages1.setVisibility(View.GONE);
                            }
                            if (jsonArray.get(1) != null) {
                                SelectedImages2.setVisibility(View.VISIBLE);
                                Glide.with(AdData.this).load(jsonArray.get(1))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(SelectedImages2);
                            } else {
                                SelectedImages2.setVisibility(View.GONE);
                            }
                            if (jsonArray.get(2) != null) {
                                SelectedImages3.setVisibility(View.VISIBLE);
                                Glide.with(AdData.this).load(jsonArray.get(2))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(SelectedImages3);
                            } else {
                                SelectedImages3.setVisibility(View.GONE);

                            }
                            if (jsonArray.get(3) != null) {
                                SelectedImages4.setVisibility(View.VISIBLE);
                                Glide.with(AdData.this).load(jsonArray.get(3))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(SelectedImages4);
                            } else {
                                SelectedImages4.setVisibility(View.GONE);

                            }

                            /*for(int i = 0;i < 2 ;i++){

                                View myView = inflater.inflate(R.layout.image_list_items, null);
                                ImageView SelectedImages1 = (ImageView)myView.findViewById(R.id.SelectedImages1);
                                Glide.with(AdData.this).load(jsonArray.get(i))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(SelectedImages1);
                                ImageLinearLayOut.addView(myView);

                            }

                            for(int j=2;j<jsonArray.length();j++){
                                View myView1 = inflater.inflate(R.layout.image_list_items, null);
                                ImageView SelectedImages1 = (ImageView)myView1.findViewById(R.id.SelectedImages1);
                                Glide.with(AdData.this).load(jsonArray.get(j))
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(SelectedImages1);
                                ImageLinearLayOut1.addView(myView1);
                            }*/


                        } else {
                            pDialog.dismiss();
                            Toast.makeText(AdData.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            // Dismiss the progress dialog
        }
    }
    private class DeletePet extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AdData.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0)
        {
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, DeleteUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        //Toast.makeText(AdData.this, json_result, Toast.LENGTH_SHORT).show();
                        if (json_result.equals("200"))
                        {
                            finish();
//                            My_Ads.finish();
                            pDialog.dismiss();
                            startActivity(new Intent(AdData.this,My_Ads.class));
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            Toast.makeText(AdData.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(AdData.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            // Dismiss the progress dialog
        }
    }
    public void IsPermission_Available()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new GetPetsData().execute();
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

}