package com.example.pet.humanco;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class My_PromotionsAd extends AppCompatActivity {
    RequestQueue requestQueue;
    JSONObject jsonObject;
    Dialog dialog;
    EditText ServiceNameEditText,StockEditText;
    private ProgressDialog pDialog;
    StringRequest stringRequest;
    Button inset_Promotion_btn,deletePromotion, Edit_Promotions, Service_Add,ViewAllServices;
    String id, shop_name, UpdateServiceURL, mobile_number, location, image, rating, url, userId, DeleteUrl, id_Delete, Shopname,mobile,loc,DeleteURLComplete,ServiceDeleteURLComplete,ServiceName,StockNo,ServiceId,service,stock;
    TextView Shop_name, Mobile_number, Location, Rating;
    ImageView promotionDataImage_PSA;
    ArrayList<HashMap<String, String>> PetList;
    ListView serviceList,all_services_listview;
    HashMap<String, String> contact;
    SessionManagement session;
    LinearLayout mainData, disableData;
    RatingBar ratingBar_MPSA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__promotions_ad);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("My Promotions");
        }
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userId = user.get(SessionManagement.userId);
        PetList = new ArrayList<>();
        url = getString(R.string.BasicURL)+"/promotions?uid="+userId;
        DeleteUrl = getString(R.string.BasicURL)+"/promotions/";
        UpdateServiceURL = getString(R.string.BasicURL)+"/service/update/";
        ServiceDeleteURLComplete = getString(R.string.BasicURL)+"/service/";
        Shop_name = findViewById(R.id.shopName_MPSA);
        Mobile_number = findViewById(R.id.phone_MPSA);
        Location = findViewById(R.id.location_MPSA);
        Rating = findViewById(R.id.rating_MPSA);
        serviceList = findViewById(R.id.serviceList_MPSA);
        inset_Promotion_btn = findViewById(R.id.inset_Promotion_btn);
        deletePromotion = findViewById(R.id.deletePromotion);
        Edit_Promotions = findViewById(R.id.Edit_Promotions);
        Service_Add = findViewById(R.id.Service_Add);
        promotionDataImage_PSA = findViewById(R.id.promotionDataImage_MPSA);
        mainData = findViewById(R.id.mainData);
        disableData = findViewById(R.id.disablePart);
        ViewAllServices = findViewById(R.id.ViewAllServices);
        ratingBar_MPSA = findViewById(R.id.ratingBar_MPSA);
        requestQueue = Volley.newRequestQueue(this);
        //new ServiceList().execute();
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                String json_result = jsonObject.getString("status");
                if (json_result.equals("200"))
                {
                    if (jsonArray.length()==1) {
                        disableData.setVisibility(View.GONE);
                        mainData.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            JSONArray jsonArray1 = jsonobject.getJSONArray("services");
                            image = jsonobject.getString("image");
                            id_Delete = jsonobject.getString("id");
                            shop_name = jsonobject.getString("shop_name");
                            mobile_number = jsonobject.getString("mobile_number");
                            location = jsonobject.getString("location");
                            rating = jsonobject.getString("rating");
                            for (int j = 0; j < jsonArray1.length(); j++)
                            {
                                JSONObject jsonobject1 = jsonArray.getJSONObject(i);
                                JSONArray services = jsonobject1.getJSONArray("services");
                                JSONObject c = services.getJSONObject(j);
                                id = c.getString("id");
                                String shop_Name = c.getString("item_name");
                                String location = c.getString("stock");
                                contact = new HashMap<>();
                                contact.put("id", id);
                                contact.put("item_name", shop_Name);
                                contact.put("stock", location);
                                PetList.add(contact);
                            }
                            Glide.with(My_PromotionsAd.this).load(image)
                                    .thumbnail(0.1f)
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(promotionDataImage_PSA);
                            Shop_name.setText(shop_name);
                            Mobile_number.setText(mobile_number);
                            Location.setText(location);
                            Rating.setText(rating);
                            String RatingBarValue = Rating.getText().toString();
                            float rating123 = Float.parseFloat(RatingBarValue);
                            ratingBar_MPSA.setRating(rating123);

                            SimpleAdapter adapter = new SimpleAdapter(My_PromotionsAd.this, PetList, R.layout.service_list_data, new String[]{"id", "item_name", "stock"}, new int[]{R.id.ID_SL, R.id.service_SL, R.id.stock_SL});
                            serviceList.setAdapter(adapter);
                        }
                    }
                    else {
                        disableData.setVisibility(View.VISIBLE);
                        mainData.setVisibility(View.GONE);
                    }}
                else{
                    Toast.makeText(My_PromotionsAd.this, "Fails To get Data please Try after sometime", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(My_PromotionsAd.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
        serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(My_PromotionsAd.this, "Please Select ViewAll Button Top to see All service", Toast.LENGTH_SHORT).show();
            }
        });
        inset_Promotion_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(My_PromotionsAd.this,Adv_Promotion_Registration.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        ViewAllServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(My_PromotionsAd.this);
                dialog.setContentView(R.layout.all_services_listview);
                dialog.setTitle("All Services");
                all_services_listview = dialog.findViewById(R.id.all_services_listview);
                SimpleAdapter adapter = new SimpleAdapter(My_PromotionsAd.this, PetList, R.layout.service_list_data, new String[]{"id", "item_name", "stock"}, new int[]{R.id.ID_SL, R.id.service_SL, R.id.stock_SL});
                all_services_listview.setAdapter(adapter);

                all_services_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final TextView ID_SL = view.findViewById(R.id.ID_SL);
                    final TextView service_SL = view.findViewById(R.id.service_SL);
                    final TextView stock_SL = view.findViewById(R.id.stock_SL);
                    ServiceId = ID_SL.getText().toString();
                    service = service_SL.getText().toString();
                    stock = stock_SL.getText().toString();
                    Log.e("Selected Item : ",ServiceId);
                    dialog = new Dialog(My_PromotionsAd.this);
                    dialog.setContentView(R.layout.btn_service);
                    dialog.setTitle("Update Service");
                    Button UpdateService = dialog.findViewById(R.id.UpdateServiceOption);
                    Button DeleteService = dialog.findViewById(R.id.DeleteServiceOption);
                    dialog.show();
                    UpdateService.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        dialog.dismiss();
                        dialog = new Dialog(My_PromotionsAd.this);
                        dialog.setContentView(R.layout.activity_promotions__services);
                        ServiceNameEditText = dialog.findViewById(R.id.input_ServiceName);
                        StockEditText = dialog.findViewById(R.id.input_Stock);
                        Button SubmitBtn = dialog.findViewById(R.id.Submit_PS);
                        ServiceNameEditText.setText(service);
                        StockEditText.setText(stock);
                        dialog.show();
                        SubmitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            new UpdatePromotionService().execute();
                            }
                        });
                            }
                    });
                    DeleteService.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(My_PromotionsAd.this, "Position : "+ServiceId, Toast.LENGTH_SHORT).show();
                        }
                    });

                    }
                });
                dialog.show();
            }
        });

        deletePromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new AlertDialog.Builder(My_PromotionsAd.this)
                .setTitle("Delete Promotion")
                .setMessage("Do you really want to Delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        new DeletePromotion().execute();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
        }
        });
        Edit_Promotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(My_PromotionsAd.this);
                dialog.setContentView(R.layout.update_promotion);
                dialog.setTitle("Update Promotions");
                Button updateProfile = dialog.findViewById(R.id.btn_update_Promotion);
                Button cancel = dialog.findViewById(R.id.btn_Cancel_Update_Promotion);
                final EditText shopName = dialog.findViewById(R.id.input_name_UP);
                final EditText mobileno = dialog.findViewById(R.id.input_mobile_UP);
                final EditText Location = dialog.findViewById(R.id.input_location_UP);
                shopName.setText(shop_name);
                mobileno.setText(mobile_number);
                Location.setText(location);
                updateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Shopname = shopName.getText().toString();
                        mobile = mobileno.getText().toString();
                        loc = Location.getText().toString();

                        if (Shopname.isEmpty())
                        {
                            shopName.setError("Please Enter Shop Name");
                            shopName.requestFocus();
                        }
                        else
                        {
                            if (mobile.isEmpty())
                            {
                                mobileno.setError("Please Enter Shop Mobile No");
                                mobileno.requestFocus();
                            }
                            else
                            {
                                if (loc.isEmpty())
                                {
                                    Location.setError("Please Enter Location");
                                    Location.requestFocus();
                                }
                                else
                                {
                                    if (!(mobile.length() == 10))
                                    {
                                        mobileno.setError("Please Enter correct Mobile No");
                                        mobileno.requestFocus();
                                    }
                                    else
                                    {
                                        new UpdatePromotion().execute();

                                    }

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
        Service_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(My_PromotionsAd.this);
                dialog.setContentView(R.layout.activity_promotions__services);
                dialog.setTitle("Add Service");
                Button updateProfile = dialog.findViewById(R.id.Submit_PS);
                final EditText Servicename = dialog.findViewById(R.id.input_ServiceName);
                final EditText Stock = dialog.findViewById(R.id.input_Stock);
                updateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ServiceName = Servicename.getText().toString();
                        StockNo = Stock.getText().toString();

                        if (ServiceName.isEmpty())
                        {
                            Servicename.setError("Please Enter Name");
                            Servicename.requestFocus();
                        }
                        else
                        {
                            if (StockNo.isEmpty())
                            {
                                Stock.setError("Please Enter Shop Name");
                                Stock.requestFocus();
                            }
                            else
                            {
                                new InsertPromotion_Service().execute();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            My_PromotionsAd.this.finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("StaticFieldLeak")
    class DeletePromotion extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(My_PromotionsAd.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0)
        {
            DeleteURLComplete = DeleteUrl+id_Delete;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE,DeleteURLComplete , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        if (json_result.equals("200"))
                        {
                            finish();
                            pDialog.dismiss();
                            startActivity(new Intent(My_PromotionsAd.this,HomePage.class));
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(My_PromotionsAd.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
    }class DeleteService extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(My_PromotionsAd.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0)
        {
            DeleteURLComplete = DeleteUrl+id_Delete;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE,ServiceDeleteURLComplete+ServiceId , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        if (json_result.equals("200"))
                        {
                            finish();
                            pDialog.dismiss();
                            startActivity(new Intent(My_PromotionsAd.this,My_PromotionsAd.class));
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(My_PromotionsAd.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
    @SuppressLint("StaticFieldLeak")
    class UpdatePromotion extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(My_PromotionsAd.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            stringRequest = new StringRequest(Request.Method.POST, getString(R.string.BasicURL)+"/promotions/update/"+id_Delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        if (json_result.equals("200")) {
                            startActivity(new Intent(My_PromotionsAd.this,My_PromotionsAd.class));
                            finish();
                            pDialog.dismiss();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            Toast.makeText(My_PromotionsAd.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(My_PromotionsAd.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                    hashMap.put("user_id", userId);
                    hashMap.put("shop_name", Shopname);
                    hashMap.put("mobile_number", mobile);
                    hashMap.put("location", loc);
                    hashMap.put("rating", "0");
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

    private class InsertPromotion_Service extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(My_PromotionsAd.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            stringRequest = new StringRequest(Request.Method.POST, getString(R.string.BasicURL)+"/service/insert", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        if (json_result.equals("200")) {
                            dialog.dismiss();
                            finish();
                            pDialog.dismiss();
                            startActivity(new Intent(My_PromotionsAd.this,My_PromotionsAd.class));
                            Toast.makeText(My_PromotionsAd.this, "Stock Entered", Toast.LENGTH_SHORT).show();
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(My_PromotionsAd.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                    //Toast.makeText(Promotions_Services.this, service+"\n"+service, Toast.LENGTH_SHORT).show();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("promotion_id", id_Delete);
                    hashMap.put("item_name", ServiceName);
                    hashMap.put("stock", StockNo);
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
    class UpdatePromotionService extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(My_PromotionsAd.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            stringRequest = new StringRequest(Request.Method.POST, UpdateServiceURL+ServiceId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        if (json_result.equals("200")) {
                            startActivity(new Intent(My_PromotionsAd.this,My_PromotionsAd.class));
                            finish();
                            pDialog.dismiss();
                            dialog.dismiss();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            Toast.makeText(My_PromotionsAd.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(My_PromotionsAd.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    String ServiceNameText = ServiceNameEditText.getText().toString();
                    String StockDataEditText = StockEditText.getText().toString();

                    hashMap.put("promotion_id", id_Delete);
                    hashMap.put("item_name", ServiceNameText);
                    hashMap.put("stock", StockDataEditText);

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