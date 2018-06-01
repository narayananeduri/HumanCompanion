package com.example.pet.humanco;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
TextView U_web_A,D_web_A;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("About Us");
        }
        U_web_A = findViewById(R.id.U_web_A);
        D_web_A = findViewById(R.id.D_Web_A);
        D_web_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeveloperWebAddressNavigation();
            }
        });
        U_web_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserWebAddressNavigation();
            }
        });
    }

    private void UserWebAddressNavigation() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://humancompanion.com"));
        startActivity(browserIntent);
    }

    private void DeveloperWebAddressNavigation() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://macsof.com/"));
        startActivity(browserIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            AboutActivity.this.finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onOptionsItemSelected(item);
    }
}
