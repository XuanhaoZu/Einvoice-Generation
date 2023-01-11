package com.example.invoicingapplication;


import java.util.ArrayList;


public class Invoice {

    public Invoice() {
    }

    public Invoice(String invoiceNo, String date, String sender, String amount, String status, String email) {
        this.invoiceNo = invoiceNo;
        this.date = date;
        this.sender = sender;
        this.amount = amount;
        this.status = status;
        this.email = email;
    }


    private String invoiceNo;

    private String date;


    private String sender;

    private String amount;


    private String status;


    private String email;


    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static ArrayList<Invoice> getInvoices() {
        ArrayList<Invoice> mInvoices = new ArrayList<>();
        mInvoices.add(new Invoice("1000001", "18/07/2022", "AB Construction", "500", "paid", "ABConstruction@gmail.com"));
        mInvoices.add(new Invoice("1000002", "02/07/2021", "CAT Timbers", "1000", "paid", "CATTimbers@gmail.com"));
        mInvoices.add(new Invoice("1000003", "05/07/2021", "Jim's Plumbing", "2000", "unpaid", "JimPlumbing@gmail.com"));
        mInvoices.add(new Invoice("1000004", "06/07/2021", "Flawless Carpentry", "3000", "unpaid", "FlawlessCarpentry.com"));
        return mInvoices;
    }


}
