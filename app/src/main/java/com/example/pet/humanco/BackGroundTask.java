package com.example.pet.humanco;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by NARAYANA on 27-02-2018.
 */

public class BackGroundTask extends AsyncTask<String,String,String> {
    ProgressDialog pDialog;
    @SuppressLint("StaticFieldLeak")
    private Context ctx;
    public BackGroundTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(ctx);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String reg_Url = "http://develecons.com/petsapi/userlogin/register";
        String login_Url = "http://develecons.com/petsapi/userlogin/login";
        String Method = strings[0];
        if (Method.equals("register"))
        {
            String U_name = strings[1];
            String email = strings[2];
            String mobile = strings[3];
            String password = strings[4];

            try {
                URL url1 = new URL(reg_Url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("name","UTF-8")+ "=" + URLEncoder.encode(U_name,"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+ "=" + URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("mobile_number","UTF-8")+ "=" + URLEncoder.encode(mobile,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+ "=" + URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Registration Successful...........";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pDialog.dismiss();
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }
}
