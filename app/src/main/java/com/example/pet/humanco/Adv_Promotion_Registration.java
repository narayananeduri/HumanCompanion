package com.example.pet.humanco;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adv_Promotion_Registration extends AppCompatActivity {
    EditText ShopName,Phone,Location;
    String shopName,phone,location,name,formattedDate,js1;
    Button Submit;
    StringRequest stringRequest;
    String imageData = "0";
    boolean flag = false;
    RequestQueue requestQueue;
    SessionManagement session;
    TextInputLayout shopnameErr,phoneErr,locationErr;
    private ProgressDialog pDialog;
    Button captureImage;
    ImageView circleImageView;
    private static final int STORAGE_PERMISSION_CODE = 2342;
    private static final int PICK_IMAGE = 22;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv__promotion__registration);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Promotions");
        }
        Log.e("imageData",imageData);
        requestQueue = Volley.newRequestQueue(this);
        ShopName = findViewById(R.id.input_ShopName);
        Phone = findViewById(R.id.input_phone);
        Location = findViewById(R.id.input_Location);
        Submit = findViewById(R.id.Submit);
        shopnameErr = findViewById(R.id.input_ShopNameErr);
        phoneErr = findViewById(R.id.input_PhoneErr);
        locationErr = findViewById(R.id.input_LocationErr);
        circleImageView = findViewById(R.id.promotionsAdImageView);
        captureImage = findViewById(R.id.capturedImage_Btn_PAD);
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManagement.userId);
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(date);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            shopName = ShopName.getText().toString();
            phone = Phone.getText().toString();
            location = Location.getText().toString();
            if (shopName.isEmpty())
            {
                shopnameErr.setError("Please Enter Shop Name");
                ShopName.requestFocus();
            }
            else
            {
//                    shopnameErr.setError("");
                if (phone.isEmpty())
                {
                    phoneErr.setError("Please Enter Phone Number");
                    Phone.requestFocus();
                }
                else
                {
                    phoneErr.setError("");
                    if (!(phone.length() == 10))
                    {
                        phoneErr.setError("Please Enter Phone Number");
                        Phone.requestFocus();
                    }
                    else {
                        phoneErr.setError("");
                        if (location.isEmpty()) {
                            locationErr.setError("Please Enter Location");
                            Location.requestFocus();
                        } else {
                            locationErr.setError("");
                            if (!flag){
                                Toast.makeText(Adv_Promotion_Registration.this, "Please Select Image", Toast.LENGTH_SHORT).show();

                            }else  IsPermission_Available();
                        }
                    }
                }
            }
            }
        });
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CaptureImage();
                chooseFile();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri fileUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                circleImageView.setImageBitmap(bitmap);
                imageData = "1";
                flag = true;
                //Log.e("imageData",imageData);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void chooseFile()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
    }

    private void requestStoragePermissions()
    {
        if (ContextCompat.checkSelfPermission(Adv_Promotion_Registration.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(Adv_Promotion_Registration.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }

    }
    private String getPath(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);



    }
    @SuppressLint("StaticFieldLeak")
    private class Adv_Promotion extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Adv_Promotion_Registration.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            stringRequest = new StringRequest(Request.Method.POST, getString(R.string.BasicURL)+"/promotions/insert", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String js = jsonObject.getString("status");
                        if (js.equals("200"))
                        {
                            js1 = jsonObject.getString("promotion");
                            //Toast.makeText(Adv_Promotion_Registration.this, ""+js1, Toast.LENGTH_SHORT).show();
                            Bundle basket= new Bundle();
                            basket.putString("abc", js1);
                            Intent intent = new Intent(Adv_Promotion_Registration.this,Promotions_Services.class);
                            intent.putExtras(basket);
                            startActivity(intent);
                            finish();
                            pDialog.dismiss();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                        else
                        {
                            pDialog.dismiss();
                            Toast.makeText(Adv_Promotion_Registration.this, "Promotion Already Exits", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("user_id",name);
                    hashMap.put("shop_name",shopName);
                    hashMap.put("mobile_number",phone);
                    hashMap.put("location",location);
                    hashMap.put("date",formattedDate);
                    hashMap.put("image",getPath(bitmap));
                    hashMap.put("rating","0");
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        /*if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            Adv_Promotion_Registration.this.finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onOptionsItemSelected(item);
    }
    public void IsPermission_Available()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new Adv_Promotion().execute();
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
