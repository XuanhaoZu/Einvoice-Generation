package com.example.invoicingapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;


import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    PieChart pieChart;
    TextView expense;
    TextView unpaid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setTitle("Dashboard");


        //prepare data
        float totalExpense = 0;
        float totalUnpaid = 0;
        List<Invoice> invoice = InvoiceLibraryActivity.mInvoices;
        for (int i = 0; i < invoice.size(); i++) {
            if (invoice.get(i).getDate().contains("2022")) {
                if (invoice.get(i).getStatus().equals("paid")) {
                    totalExpense = totalExpense + Float.parseFloat(invoice.get(i).getAmount().replaceAll("[^\\d.]", ""));
                } else {
                    totalUnpaid = totalUnpaid + Float.parseFloat(invoice.get(i).getAmount().replaceAll("[^\\d.]", ""));
                }

            }

        }

        //Pie chart
        pieChart = findViewById(R.id.pieChart);
        paintPieChart(totalExpense, totalUnpaid);

        //textview
        expense = findViewById(R.id.tvExpense);
        unpaid = findViewById(R.id.tvUnpaid);
        expense.setText(totalExpense + "$");
        unpaid.setText(totalUnpaid + "$");


        //bottomNavigation bar
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent = null;
                switch (menuItem.getItemId()) {
                    case R.id.library:
                        intent = new Intent(DashboardActivity.this, InvoiceLibraryActivity.class);
                        break;
                    case R.id.newInvoice:
                        intent = new Intent(DashboardActivity.this, ChoiceActivity.class);
                        break;
                    case R.id.dashboard:
                        intent = new Intent(DashboardActivity.this, DashboardActivity.class);
                        break;
                    case R.id.help:
                        intent = new Intent(DashboardActivity.this, HelpActivity.class);
                        break;
                }
                startActivity(intent);
                return true;
            }
        });
    }

    //pie chart method
    private void paintPieChart(float d1, float d2) {
        List<PieEntry> values = new ArrayList<>();

        values.add(new PieEntry(d1, "Expense"));
        values.add(new PieEntry(d2, "Unpaid"));
        pieChart.setUsePercentValues(true);

        PieDataSet pieDataSet = new PieDataSet(values, " ");

        //set color
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(182, 203, 237));
        colors.add(Color.rgb(220, 220, 220));
        pieDataSet.setColors(colors);

        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(Color.BLACK);

        //set description
        Description description = new Description();
        description.setEnabled(false);
        pieChart.setDescription(description);

        //set text
        pieDataSet.setValueTextSize(15f);
        pieDataSet.setValueTextColor(Color.rgb(30, 30, 30));
        pieDataSet.setSliceSpace(1f);

        //other settings
        pieChart.setRotationEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
        l.setTextSize(13f);
        l.setTextColor(Color.BLACK);
        l.setForm(Legend.LegendForm.CIRCLE);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextColor(Color.BLACK);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);
    }


}
