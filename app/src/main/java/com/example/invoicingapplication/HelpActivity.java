package com.example.invoicingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle("Help");

        //bottomNavigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent = null;
                switch (menuItem.getItemId()) {
                    case R.id.library:
                        intent = new Intent(HelpActivity.this, InvoiceLibraryActivity.class);
                        break;
                    case R.id.newInvoice:
                        intent = new Intent(HelpActivity.this, ChoiceActivity.class);
                        break;
                    case R.id.dashboard:
                        intent = new Intent(HelpActivity.this, DashboardActivity.class);
                        break;
                    case R.id.help:
                        intent = new Intent(HelpActivity.this, HelpActivity.class);
                        break;
                }
                startActivity(intent);
                return true;
            }
        });


    }
}