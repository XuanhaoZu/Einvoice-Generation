package com.example.invoicingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ShareActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private EditText email, topic, content;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setTitle("Share Invoice");

        email = findViewById(R.id.etEmail);
        topic = findViewById(R.id.etTopic);
        content = findViewById(R.id.etEmailContent);

        Intent intent = getIntent();
        String invoiceNo = intent.getStringExtra(InvoiceLibraryActivity.TO_SHARE);
        List<Invoice> invoice = InvoiceLibraryActivity.mInvoices;
        for (int i = 0; i < invoice.size(); i++) {
            if (invoice.get(i).getInvoiceNo().equals(invoiceNo)) {
                email.setText(invoice.get(i).getEmail());
                topic.setText("Invoice information share");
                content.setText("Dear " + invoice.get(i).getSender() + " " + "\n"
                        + "Here is the detailed information of invoice. Please check." + "\n"
                        + "Invoice Number: " + invoiceNo + "\n"
                        + "Date: " + invoice.get(i).getDate() + "\n"
                        + "Sender: " + invoice.get(i).getSender() + "\n"
                        + "Amount: " + invoice.get(i).getAmount() + "\n"
                        + "Status: " + invoice.get(i).getStatus() + "\n"
                        + "Email address: " + invoice.get(i).getEmail());
            }
        }


        //get email address
        String emailAddress = String.valueOf(email.getText());
        //get email topic
        String emailTopic = String.valueOf(topic.getText());
        //get email content
        String emailContent = String.valueOf(content.getText());

        //button share - share invoice
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
                intent.putExtra(Intent.EXTRA_SUBJECT, emailTopic);
                intent.putExtra(Intent.EXTRA_TEXT, emailContent);


                startActivity(intent);
            }
        });

        //navigation bar(can copy to other page)
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent = null;
                switch (menuItem.getItemId()) {
                    case R.id.library:
                        intent = new Intent(ShareActivity.this, InvoiceLibraryActivity.class);
                        break;
                    case R.id.newInvoice:
                        intent = new Intent(ShareActivity.this, InvoiceInformationActivity.class);
                        break;
                    case R.id.dashboard:
                        intent = new Intent(ShareActivity.this, DashboardActivity.class);
                        break;
                }
                startActivity(intent);
                return true;
            }
        });


    }


}