package com.example.invoicingapplication;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> implements Filterable {

    private List<Invoice> mInvoices;
    private List<Invoice> mInvoicesFiltered;
    //    private ClickListener mListener;
    public Context mContext;
    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;


    public static String checkInvoiceNo;


    // A constructor method for the adapter class
    InvoiceAdapter(List<Invoice> invoices) {
        this.mInvoicesFiltered = invoices;
        this.mInvoices = invoices;
    }

    @NonNull
    @Override
    public InvoiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull InvoiceAdapter.MyViewHolder holder, int position) {
        final Invoice invoice = mInvoicesFiltered.get(position);
        int id = position;
        holder.name.setText(invoice.getSender());
        holder.amount.setText("$" + String.valueOf(invoice.getAmount()));
        holder.date.setText(invoice.getDate());
        holder.itemView.setTag(id);

        // payment tracking feature
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dueDate = sdf.parse(invoice.getDate());
            System.out.println("Due date is " + String.valueOf(dueDate));

            LocalDate dateObj = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String currentStrDate = dateObj.format(formatter);
            Date currentDate = sdf.parse(currentStrDate);

            long timeDiff = Math.abs(currentDate.getTime() - dueDate.getTime());
            System.out.println("Timediff is " + timeDiff);
            long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
            System.out.println("Days in between dates is " + String.valueOf(daysDiff));
            if (currentDate.before(dueDate)) {
                if (daysDiff <= 3 && daysDiff <= 7) {
                    holder.warning.setImageResource(R.drawable.warning);
                    holder.warning.setVisibility(View.VISIBLE);
                } else if (daysDiff < 0 && daysDiff <= 3) {
                    holder.warning.setImageResource(R.drawable.alert);
                    holder.warning.setVisibility(View.VISIBLE);
                }
            } else {
                holder.warning.setImageResource(R.drawable.overdue);
                holder.warning.setVisibility(View.VISIBLE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        //checkbox
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.checkBox.isChecked()) {
                    map.clear();
                    map.put(position, true);
                    checkInvoiceNo = invoice.getInvoiceNo();
                } else {
                    map.remove(position);
                }
            }
        });
        onBind = true;
        if (map != null && map.containsKey(position)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        onBind = false;
    }

    @Override
    public int getItemCount() {
        return mInvoicesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mInvoicesFiltered = mInvoices;
                } else {
                    ArrayList<Invoice> filteredList = new ArrayList<>();
                    for (Invoice invoice : mInvoices) {
                        if (invoice.getSender().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(invoice);
                        } else if (invoice.getAmount().contains(charString.toLowerCase())) {
                            filteredList.add(invoice);
                        } else if (invoice.getDate().contains(charString.toLowerCase())) {
                            filteredList.add(invoice);
                        }
                    }
                    mInvoicesFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mInvoicesFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mInvoicesFiltered = (ArrayList<Invoice>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, amount, date;
        public ImageView warning;
        public CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            amount = itemView.findViewById(R.id.tvAmount);
            date = itemView.findViewById((R.id.tvDate));
            checkBox = itemView.findViewById(R.id.checkBox);
            warning = itemView.findViewById(R.id.ivWarning);
        }
    }

    // Sort method
    public void sort(final int sortMethod) {
        if (mInvoicesFiltered.size() > 0) {
            Collections.sort(mInvoicesFiltered, new Comparator<Invoice>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public int compare(Invoice o1, Invoice o2) {
                    if (sortMethod == 1) {

                        Date date1 = null;
                        try {
                            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(o1.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Date date2 = null;
                        try {
                            date2 = new SimpleDateFormat("dd/MM/yyyy").parse(o2.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return date1.compareTo(date2);

                    } else if (sortMethod == 2) {
                        int number1 = Integer.parseInt(o1.getAmount());
                        int number2 = Integer.parseInt(o2.getAmount());

                        return number1 - number2;
                    }
                    return o1.getSender().compareTo(o2.getSender());
                }
            });
        }
        notifyDataSetChanged();
    }
}