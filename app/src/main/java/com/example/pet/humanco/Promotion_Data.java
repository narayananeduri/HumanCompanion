package com.example.pet.humanco;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

public class Promotion_Data extends AppCompatActivity {
    RequestQueue requestQueue;
    StringRequest stringRequest;
    Button makeCall_promotion,ViewAllServices;
    String id, shop_name, mobile_number, location, image, rating, str, url;
    TextView Shop_name, Mobile_number, Location, Rating;
    ImageView promotionDataImage_PSA;
    ArrayList<HashMap<String, String>> PetList;
    ListView serviceList,all_services_listview;
    Dialog dialog;
    HashMap<String, String> contact;
    RatingBar ratingBar_PSA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion__data);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        Bundle gt = getIntent().getExtras();

        assert gt != null;
        str = gt.getString("abc");
        int str1 = Integer.parseInt(str);
        PetList = new ArrayList<>();
        url = getString(R.string.BasicURL)+"/promotions/id/" + str1;
        Shop_name = findViewById(R.id.shopName_PSA);
        Mobile_number = findViewById(R.id.phone_PSA);
        Location = findViewById(R.id.location_PSA);
        Rating = findViewById(R.id.rating_PSA);
        serviceList = findViewById(R.id.serviceList);
        makeCall_promotion = findViewById(R.id.makeCall_promotion);
        ViewAllServices = findViewById(R.id.ViewAllServices);
        promotionDataImage_PSA = findViewById(R.id.promotionDataImage_PSA);
        ratingBar_PSA = findViewById(R.id.ratingBar_PSA);
        requestQueue = Volley.newRequestQueue(this);
        //new ServiceList().execute();
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        JSONArray jsonArray1 = jsonobject.getJSONArray("services");
                        //Toast.makeText(Promotion_Data.this, "" + jsonArray1, Toast.LENGTH_SHORT).show();
                        image = jsonobject.getString("image");
                        id = jsonobject.getString("id");
                        shop_name = jsonobject.getString("shop_name");
                        mobile_number = jsonobject.getString("mobile_number");
                        location = jsonobject.getString("location");
                        rating = jsonobject.getString("rating");
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            JSONObject jsonobject1 = jsonArray.getJSONObject(i);
                            JSONArray services = jsonobject1.getJSONArray("services");
                            //Toast.makeText(Promotion_Data.this, ""+services, Toast.LENGTH_SHORT).show();
                                JSONObject c = services.getJSONObject(j);
                                String id = c.getString("id");
                                String shop_Name = c.getString("item_name");
                                String location = c.getString("stock");
                                contact = new HashMap<>();
                                contact.put("id", id);
                                contact.put("item_name", shop_Name);
                                contact.put("stock", location);
                                PetList.add(contact);
                        }
                        Glide.with(Promotion_Data.this).load(image)
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
                        ratingBar_PSA.setRating(rating123);
                        SimpleAdapter adapter = new SimpleAdapter(Promotion_Data.this, PetList, R.layout.service_list_data, new String[]{"id","item_name","stock"}, new int[]{R.id.ID_SL,R.id.service_SL, R.id.stock_SL});
                        serviceList.setAdapter(adapter);

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

        makeCall_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted()) {
                    call_action();
                }
            }
        });
        ViewAllServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Promotion_Data.this);
                dialog.setContentView(R.layout.all_services_listview);
                dialog.setTitle("All Services");
                all_services_listview = dialog.findViewById(R.id.all_services_listview);
                SimpleAdapter adapter = new SimpleAdapter(Promotion_Data.this, PetList, R.layout.service_list_data, new String[]{"id", "item_name", "stock"}, new int[]{R.id.ID_SL, R.id.service_SL, R.id.stock_SL});
                all_services_listview.setAdapter(adapter);
                dialog.show();
            }
        });
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

    public void call_action() {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobile_number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            Promotion_Data.this.finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onOptionsItemSelected(item);
    }
}
