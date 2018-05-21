package com.example.pet.humanco;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Promotions_Services extends AppCompatActivity {

    TextView name,phone,date;
    String service,stock,str;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    Button Submit,Complete;
    EditText Service,Stock;
    String url;
    String url1;
    TextInputLayout serviceErr,stockErr;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions__services);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add Services");
        }
        name = findViewById(R.id.Shop_Name_PS);
        phone = findViewById(R.id.Phone_PS);
        date = findViewById(R.id.Date_PS);
        Submit = findViewById(R.id.Submit_PS);
        Service = findViewById(R.id.input_ServiceName);
        Stock = findViewById(R.id.input_Stock);
        Complete = findViewById(R.id.Complete_PS);
        serviceErr =  findViewById(R.id.input_ServiceName_PS);
        stockErr =  findViewById(R.id.input_Stock_PS);
        url = getString(R.string.BasicURL)+"/promotions";
        url1 = getString(R.string.BasicURL)+"/service/insert";

        Bundle gt=getIntent().getExtras();
        assert gt != null;
        str=gt.getString("abc");
        requestQueue = Volley.newRequestQueue(this);
        /*stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("result");
                    Name = jsonObject2.getString("shop_name");
                    //Phone = jsonObject2.getString("phone");
                    Date = jsonObject2.getString("date");
                    name.setText(Name);
                    date.setText(Date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });
        requestQueue.add(stringRequest);*/
        Submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                service = Service.getText().toString();
                stock = Stock.getText().toString();
                if (service.isEmpty())
                {
                    serviceErr.setError("Please Enter Service Name");
                    Service.requestFocus();
                }
                else
                {
                    if (stock.isEmpty())
                    {
                        stockErr.setError("Please Enter Stock");
                        Stock.requestFocus();
                    }
                    else
                    {
                        new Promotion_Service().execute();
                    }
                }

            }
        });
        Complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Promotions_Services.this,HomePage.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    private class Promotion_Service extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Promotions_Services.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            stringRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        pDialog.dismiss();
                        if (json_result.equals("200"))
                        {
                            Service.setText("");
                            Stock.setText("");
                            Service.requestFocus();
                            Complete.setVisibility(View.VISIBLE);
                            Submit.setText("Add Service");
                        }
                        else
                        {
                            pDialog.dismiss();
                            Toast.makeText(Promotions_Services.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    //Toast.makeText(Promotions_Services.this, service+"\n"+service, Toast.LENGTH_SHORT).show();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("promotion_id", str);
                    hashMap.put("item_name", service);
                    hashMap.put("stock", stock);
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
            Promotions_Services.this.finish();
            startActivity(new Intent(Promotions_Services.this,HomePage.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onOptionsItemSelected(item);
    }
}
