package com.example.invoicingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InvoiceLibraryActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private static final String TAG = "InvoiceLibraryActivity";
    public static List<Invoice> mInvoices;
    private InvoiceAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Button btnShare, btnPay;
    public static String TO_SHARE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_library);
        setTitle("Invoice Library");


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);


        //firebase
        getFirebaseData();
        update();
        Log.d(TAG, LoginActivity.currentUser.getEmail());


        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = InvoiceAdapter.checkInvoiceNo;
                Intent intentShare = new Intent(InvoiceLibraryActivity.this, ShareActivity.class);
                intentShare.putExtra(TO_SHARE, InvoiceAdapter.checkInvoiceNo);
                startActivity(intentShare);
            }
        });

        btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvoiceLibraryActivity.this, ConfirmActivity.class);
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
                        intent = new Intent(InvoiceLibraryActivity.this, InvoiceLibraryActivity.class);
                        break;
                    case R.id.newInvoice:
                        intent = new Intent(InvoiceLibraryActivity.this, ChoiceActivity.class);
                        break;
                    case R.id.dashboard:
                        intent = new Intent(InvoiceLibraryActivity.this, DashboardActivity.class);
                        break;
                    case R.id.help:
                        intent = new Intent(InvoiceLibraryActivity.this, HelpActivity.class);
                        break;
                }
                startActivity(intent);
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortAmount:
                // sort by amount
                mAdapter.sort(2);
                return true;
            case R.id.sortDate:
                // sort by date
                mAdapter.sort(1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //get firebase data
    public List<Invoice> getFirebaseData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mInvoices = new ArrayList<>();

//        db.collection(LoginActivity.currentUser.getEmail())
        db.collection("z912964302@gmail.com")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Invoice invoice = document.toObject(Invoice.class);
                                mInvoices.add(invoice);
                            }
                            mAdapter = new InvoiceAdapter(mInvoices);
                            mRecyclerView.setAdapter(mAdapter);

                        }
                    }
                });

        return mInvoices;
    }

    public void update() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("cities").document("SF");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }


}