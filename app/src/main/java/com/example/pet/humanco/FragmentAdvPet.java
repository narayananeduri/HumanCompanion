package com.example.pet.humanco;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pet.humanco.model.PetImage;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/*
 * A simple {@link Fragment} subclass.
 */
public class FragmentAdvPet extends Fragment {
    EditText Type, Breed, age, Description, location, price;
    RadioGroup Adoption;
    Button captureImage, SubmitBtn;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    JSONObject jsonObject;
    Spinner genderSpinner;
    private static final int STORAGE_PERMISSION_CODE = 2342;
    private static final int PICK_IMAGE = 22;
    String gender[] = {"Gender", "Male", "Female"};
    private ProgressDialog pDialog;
    String radioGender = "1", radioAdoption = "Free", genderSelected;
    String Type_String, Breed_String, Age_String, Description_String, location_String, price_String, name;
    SessionManagement session;
    ArrayList<PetImage> ImagelistItems = new ArrayList<PetImage>();
    ArrayList<String> stringlistItems = new ArrayList<String>();
    LinearLayout imageListView;
    // url to create new product
    String String_url;
    PetImagesAdapter adapter = null;
    private String stringImages = "";
    AdView mAdView,mAdView1;
    //private InterstitialAd mInterstitialAd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_adv__pet, container, false);
        Type = view.findViewById(R.id.type_AD);
        Breed = view.findViewById(R.id.breed_AD);
        age = view.findViewById(R.id.age_AD);
        Description = view.findViewById(R.id.description_AD);
        location = view.findViewById(R.id.location_AD);
        price = view.findViewById(R.id.paymentFare_AD);
        genderSpinner = view.findViewById(R.id.genderSpinner);
        Adoption = view.findViewById(R.id.Adoption_AD);
        imageListView = view.findViewById(R.id.imageListView);
        captureImage = view.findViewById(R.id.capturedImage_Btn_AD);
        SubmitBtn = view.findViewById(R.id.Post_Ad);
        mAdView = view.findViewById(R.id.adView);
        mAdView1 = view.findViewById(R.id.adView1);
        session = new SessionManagement(getContext());
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManagement.userId);
        String_url = getString(R.string.BasicURL)+"/pets/save";
        /*if (ImagelistItems.size() == 0) {
            Log.e("ImagelistItems",""+ImagelistItems);
            SubmitBtn.setVisibility(View.GONE);
        } else {
            Log.e("ImagelistItems!Empty",""+ImagelistItems);
            SubmitBtn.setVisibility(View.VISIBLE);
        }*/
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, gender);
        genderSpinner.setAdapter(arrayAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: {
                        genderSelected = null;
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
        requestStoragePermissions();// used to request permission for media storage
        requestQueue = Volley.newRequestQueue(getContext());
        Adoption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Free:
                        radioAdoption = "Free";
                        price.setVisibility(View.GONE);
                        break;
                    case R.id.Payment:
                        radioAdoption = "Payment";
                        price.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    price.setError("Please Enter Details");
                    SubmitBtn.setVisibility(View.GONE);
                } else {
                    SubmitBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        SubmitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Type_String = Type.getText().toString();
                Breed_String = Breed.getText().toString();
                Age_String = age.getText().toString();
                Description_String = Description.getText().toString();
                location_String = location.getText().toString();
                price_String = price.getText().toString();

                if (Type_String.isEmpty()) {
                    Type.requestFocus();
                    Type.setError("Please Enter Values");
                } else {
                    if (Breed_String.isEmpty()) {
                        Breed.requestFocus();
                        Breed.setError("Please Enter Values");
                    } else {
                        if (Age_String.isEmpty()) {
                            age.requestFocus();
                            age.setError("Please Enter Values");
                        } else {
                            if( genderSelected==null) {
                                Log.e("GenderSelected1",""+genderSelected);
                                Toast.makeText(getContext(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Log.e("GenderSelected",""+genderSelected);
                                IsPermission_Available();
                                //SubmitBtn.setVisibility(View.GONE);
                            }

                        }
                    }
                }
            }
        });
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView1.loadAd(adRequest);
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), fileUri);
                PetImage petImage = new PetImage();
                petImage.setBitimage(bitmap);
                ImagelistItems.add(petImage);
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View myView = inflater.inflate(R.layout.image_list_items, null);
                ImageView img = myView.findViewById(R.id.SelectedImages1);
                img.setImageBitmap(bitmap);
                imageListView.addView(myView);
                if (ImagelistItems.size() == 4) {
                    captureImage.setVisibility(View.GONE);
                } else {
                    captureImage.setVisibility(View.VISIBLE);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    private void requestStoragePermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

    }

    private String getPath(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    @SuppressLint("StaticFieldLeak")
    private class Ad_Pet extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.e("Result","This module caled");
            stringRequest = new StringRequest(Request.Method.POST,
                    getString(R.string.BasicURL)+"/pets/save",
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        jsonObject = new JSONObject(response);
                        String json_result = jsonObject.getString("status");
                        if (json_result.equals("200")) {
                            SubmitBtn.setVisibility(View.VISIBLE);
                            genderSpinner.setSelection(0);
                            Type.setText("");
                            Breed.setText("");
                            age.setText("");
                            Description.setText("");
                            location .setText("");
                            price.setText("");
                            ImagelistItems.clear();
                            imageListView.removeAllViews();
                            pDialog.dismiss();
                            ((HomePage)getActivity()).navigatePage(0);
                            Toast.makeText(getContext(), "Thakyou for your intrest\nWe will update your post after verification", Toast.LENGTH_SHORT).show();
                            /*getActivity().getFragmentManager().popBackStack();
                            startActivity(new Intent(getContext(),HomePage.class));*/

                        } else {
                            pDialog.dismiss();
                            SubmitBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    JSONArray jsonArray = new JSONArray();
                    hashMap.put("user_id", name);
                    hashMap.put("images", stringImages.substring(1, stringImages.length()));
                    hashMap.put("type_name", Type_String);
                    hashMap.put("breend_name", Breed_String);
                    hashMap.put("age", Age_String);
                    hashMap.put("gender", genderSelected); //0: male, 1: female
                    hashMap.put("description", Description_String);
                    hashMap.put("adopte", radioAdoption);
                    hashMap.put("amount", price_String);
                    hashMap.put("location", location_String);
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

    public void IsPermission_Available() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (ImagelistItems.size() > 0) {
                for (int i = 0; i < ImagelistItems.size(); i++) {
                    String str = getPath(ImagelistItems.get(i).getBitimage());
                    stringImages = stringImages + "," + str;
                    if (i == ImagelistItems.size() - 1) {
                        Log.e("image size", ImagelistItems.size() + "");
                        new Ad_Pet().execute();
                    }
                }
            } else {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
        class PetImagesAdapter extends ArrayAdapter<PetImage> {
            ArrayList<PetImage> imageLists;
            Context context;
            int resource;

            public PetImagesAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PetImage> imageLists) {
                super(context, resource, imageLists);
                // this.imageLists = imageLists;
                this.context = context;
                //this.resource = resource;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.image_list_items, null, true);
                }
                ImageView imageView = convertView.findViewById(R.id.SelectedImages1);
                imageView.setImageBitmap(getItem(position).getBitimage());
                return convertView;
            }


        }


}

