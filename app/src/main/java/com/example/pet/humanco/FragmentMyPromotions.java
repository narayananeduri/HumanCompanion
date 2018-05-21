package com.example.pet.humanco;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pet.humanco.model.PromotionList;
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
public class FragmentMyPromotions extends Fragment
{

    FloatingActionButton adv_Promotions_Click;
    ListView Pet_List;
    ArrayList<PromotionList> arrayList;
    private ProgressDialog pDialog;
    // URL to get contacts JSON
    String url;
    AdView mAdView;
    private InterstitialAd mInterstitialAd;
    public FragmentMyPromotions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_my__promotions, container, false);
        Pet_List = (ListView) v.findViewById(R.id.ListView);
        adv_Promotions_Click= v.findViewById(R.id.adv_Promotions_Click);
        mAdView = v.findViewById(R.id.adView);
        arrayList = new ArrayList<>();
        url = getString(R.string.BasicURL)+"/promotions/list";

        IsPermission_Available();
        Pet_List.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView tv4 = (TextView)view.findViewById(R.id.Id_PM);
                String tvv = tv4.getText().toString();
                //Toast.makeText(getContext(), "this: "+tvv, Toast.LENGTH_SHORT).show();
                Bundle basket = new Bundle();
                basket.putString("abc", tvv);
                Intent intent = new Intent(getContext(),Promotion_Data.class);
                intent.putExtras(basket);
                startActivity(intent);
            }
        });
        adv_Promotions_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Adv_Promotion_Registration.class));
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return v;
    }
    class PromotionsList extends AsyncTask<String, Integer, String> {
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
            try {

                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray =  jsonObject.getJSONArray("result");
                arrayList.clear();

                for(int i =0;i<jsonArray.length(); i++){
                    JSONObject productObject = jsonArray.getJSONObject(i);
                    arrayList.add(new PromotionList(
                            productObject.getString("id"),
                            productObject.getString("shop_name"),
                            productObject.getString("mobile_number"),
                            productObject.getString("location"),
                            productObject.getString("image"),
                            productObject.getString("rating")

                    ));
                    //Toast.makeText(getContext(), ""+arrayList, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PromotionCustomListAdapter adapter = new PromotionCustomListAdapter(getContext(), R.layout.promotion_list, arrayList);
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
    public void IsPermission_Available()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new PromotionsList().execute(url);
        } else {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
