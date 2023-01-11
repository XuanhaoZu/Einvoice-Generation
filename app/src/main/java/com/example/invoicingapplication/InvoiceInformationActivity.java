package com.example.invoicingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InvoiceInformationActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private EditText invNo, invDate, invSender, invAmount, invEmail, invStatus;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        setTitle("Invoice Information");

        EditText invNo = (EditText) findViewById(R.id.inv_no);
        EditText invDate = (EditText) findViewById(R.id.inv_date);
        EditText invSender = (EditText) findViewById(R.id.inv_sender);
        EditText invAmount = (EditText) findViewById(R.id.inv_amount);
        EditText invEmail = (EditText) findViewById(R.id.inv_email);
        EditText invStatus = (EditText) findViewById(R.id.inv_status);


        //check whether intent exist
        try {
            Intent intent = getIntent();
            Bundle args = intent.getBundleExtra("BUNDLE");
            ArrayList<String> invoiceInformation = (ArrayList<String>) args.getSerializable("ARRAYLIST");
            invNo.setText(invoiceInformation.get(0));
            invDate.setText(invoiceInformation.get(1));
            invSender.setText(invoiceInformation.get(2));
            invAmount.setText(invoiceInformation.get(3));
            invStatus.setText(invoiceInformation.get(4));
            invEmail.setText(invoiceInformation.get(5));


        } catch (Exception e) {
        }

        btnSave = findViewById(R.id.inv_save);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Invoice Number

                String invoiceNumber = invNo.getText().toString();
                // Get Invoice Date
                String invoiceDate = invDate.getText().toString();
                // Get Invoice Sender
                String invoiceSender = invSender.getText().toString();
                // Get Invoice Amount
                String invoiceAmount = invAmount.getText().toString();
                // Get Invoice Status
                String invoiceStatus = invStatus.getText().toString();
                // Get Invoice Email
                String invoiceEmail = invEmail.getText().toString();
                Map<String, Object> invoice = new HashMap<>();
                invoice.put("invoiceNo", invoiceNumber);
                invoice.put("date", invoiceDate);
                invoice.put("sender", invoiceSender);
                invoice.put("amount", invoiceAmount);
                invoice.put("status", invoiceStatus);
                invoice.put("email", invoiceEmail);


                db.collection("z912964302@gmail.com").document(invoiceNumber)
                        .set(invoice, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("add invoice", "DocumentSnapshot successfully written!");
                                Toast.makeText(InvoiceInformationActivity.this, "Save successfully!", Toast.LENGTH_SHORT);
                                invNo.setText("");
                                invDate.setText("");
                                invSender.setText("");
                                invAmount.setText("");
                                invStatus.setText("");
                                invEmail.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("add invoice", "Error writing document", e);
                            }
                        });

                Intent intent = new Intent(InvoiceInformationActivity.this, InvoiceLibraryActivity.class);
                startActivity(intent);

            }
        });

        btnCancel = findViewById(R.id.inv_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvoiceInformationActivity.this, InvoiceLibraryActivity.class);
                startActivity(intent);
            }
        });
    }
}