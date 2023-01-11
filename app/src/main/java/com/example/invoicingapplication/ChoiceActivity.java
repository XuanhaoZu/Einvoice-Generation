package com.example.invoicingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChoiceActivity extends AppCompatActivity {

    private Button btnCancel, btnInsert, btnUpload, btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        setTitle("Create New Invoice");

        btnCancel = findViewById(R.id.ch_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoiceActivity.this, InvoiceLibraryActivity.class);
                startActivity(intent);
            }
        });

        btnScan = findViewById(R.id.ch_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ChoiceActivity.this, ScanActivityNew.class);
                startActivity(intent2);
            }
        });


        btnInsert = findViewById(R.id.ch_insert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(ChoiceActivity.this, InvoiceInformationActivity.class);
                startActivity(intent4);
            }
        });
    }
}
