package com.example.pet.humanco;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pet.humanco.model.ImageList;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearchPet extends Fragment {
    ListView Pet_List;
    String url;
    String searchListURL;
    EditText Search_Pet;
    String SearchData;
    ProgressDialog pDialog;
    ArrayList<ImageList> arrayList;
    AdView mAdView;

    public FragmentSearchPet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_search__pet, container, false);
        Pet_List = v.findViewById(R.id.ListView);
        arrayList = new ArrayList<>();
        Search_Pet = v.findViewById(R.id.search_View_SearchPet);
        mAdView = v.findViewById(R.id.adView);
        searchListURL = getString(R.string.BasicURL)+"/pets/search?term=";
        url = getString(R.string.BasicURL)+"/pets";
        IsPermission_Available();
        Pet_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv4 = (TextView) view.findViewById(R.id.Id);
                String tvv = tv4.getText().toString();
                Bundle basket = new Bundle();
                basket.putString("abc", tvv);
                Intent intent = new Intent(getContext(), Item_Data.class);
                intent.putExtras(basket);
                startActivity(intent);
            }
        });
        Search_Pet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                SearchData = Search_Pet.getText().toString();
                if (SearchData.equals("")) {
                    new List().execute(url);
                } else {
                    new List().execute(searchListURL + SearchData);
                    Log.e("Search URL",searchListURL + SearchData);
                }
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Resume","On resume Called");
        new List().execute(url);
    }

    @SuppressLint("StaticFieldLeak")
    class List extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            //pDialog.dismiss();
           /* if (pDialog != null) {
                pDialog.dismiss();
            }*/
            try {
                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                arrayList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject productObject = jsonArray.getJSONObject(i);
                    arrayList.add(new ImageList(
                            productObject.getString("id"),
                            productObject.getString("images"),
                            productObject.getString("type_name"),
                            productObject.getString("breend_name"),
                            productObject.getString("age"),
                            productObject.getString("location")

                    ));
                    //pDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomListAdapter adapter = new CustomListAdapter(getContext(), R.layout.model_list, arrayList);
            Pet_List.setAdapter(adapter);
        }
    }


    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public void IsPermission_Available() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new List().execute(url);
        } else {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
