package com.example.invoicingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ScanActivityNew extends AppCompatActivity {
    Button button_capture, button_copy, button_save;
    TextView textView_data;
    Bitmap bitmap;
    ArrayList<String> invoiceInformation = new ArrayList<String>();

    private static final int REQUEST_CAMERA_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_new);


        button_capture = findViewById(R.id.button_capture);
        button_copy = findViewById(R.id.button_copy);
        button_save = findViewById(R.id.button_save);
        textView_data = findViewById(R.id.text_data);


        if (ContextCompat.checkSelfPermission(ScanActivityNew.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanActivityNew.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ScanActivityNew.this);
            }
        });


        //save image
        button_save.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {

                                               ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                               bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                               byte[] data = baos.toByteArray();
                                               StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                               try {
                                                   StorageReference invoiceRef = storageRef.child("z912964302@gmail.com" + "/" + "#" + invoiceInformation.get(0));
                                                   UploadTask uploadTask = invoiceRef.putBytes(data);
                                                   uploadTask.addOnFailureListener(new OnFailureListener() {
                                                       @Override
                                                       public void onFailure(@NonNull Exception exception) {
                                                           // Handle unsuccessful uploads
                                                       }
                                                   }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                       @Override
                                                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                           Toast.makeText(ScanActivityNew.this, "Save image successfully", Toast.LENGTH_LONG);
                                                       }
                                                   });
                                               } catch (Exception e) {
                                                   DateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
                                                   Date date = new Date();
                                                   String datey = dateformat.format(date);
                                                   System.out.println(datey);

                                                   StorageReference invoiceRef = storageRef.child("z912964302@gmail.com" + "/" + datey);
                                                   UploadTask uploadTask = invoiceRef.putBytes(data);
                                                   uploadTask.addOnFailureListener(new OnFailureListener() {
                                                       @Override
                                                       public void onFailure(@NonNull Exception exception) {
                                                           // Handle unsuccessful uploads
                                                       }
                                                   }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                       @Override
                                                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                           Toast.makeText(ScanActivityNew.this, "Save image successfully", Toast.LENGTH_LONG);
                                                       }
                                                   });

                                               }


                                           }

                                       }
        );


        //edit information(to invoiceInformation Activity)
        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String scanned_text = textView_data.getText().toString();
                copyToClipBoard(scanned_text);


                Intent intent = new Intent(ScanActivityNew.this, InvoiceInformationActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) invoiceInformation);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTextFromImage(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void getTextFromImage(Bitmap bitmap) {
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()) {
            Toast.makeText(ScanActivityNew.this, "Error Occurred!", Toast.LENGTH_SHORT);
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }

//            try{
            ArrayList<String> invoiceInformation = getInvoiceInformation(stringBuilder);
            textView_data.setText(invoiceInformation.get(invoiceInformation.size() - 1));
//            }
//            catch (Exception e){
//                textView_data.setText(stringBuilder);
//
//            }
//            Log.d("???????????", "getTextFromImage: "+ invoiceInformation.get(invoiceInformation.size() - 1));


//            textView_data.setText(invoiceInformation.get(invoiceInformation.size() - 1));
            button_capture.setText("Retake");
            button_copy.setVisibility(View.VISIBLE);
            button_save.setVisibility(View.VISIBLE);
        }
    }

    private void copyToClipBoard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied data", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(ScanActivityNew.this, "Copied to clipboard!", Toast.LENGTH_SHORT);
    }


    private ArrayList<String> getInvoiceInformation(StringBuilder stringBuilder) {
        ///////string cut////////////

        String[] parts = null;
        String string = stringBuilder.toString();
        String mInvoiceNo = null;

        String mDate = null;
        String mSender = null;
        String mAmount = null;
        String mStatus = null;
        String mEmail = null;

        //invoice number
        try {
            parts = string.split("(?<=INVOICE NO)");
            parts = string.split("(?<=INVOICE N0)");
            mInvoiceNo = parts[1];
            parts = mInvoiceNo.split("\n");
            mInvoiceNo = parts[0];
            mInvoiceNo = mInvoiceNo.replaceAll("[^0-9]", "");
        } catch (Exception e) {
        }

        //date
        try {
            parts = string.split("(?<=DUE DATE )");
            mDate = parts[1];
            parts = mDate.split("\n");
            mDate = parts[0];
        } catch (Exception e) {
        }


        //sender
        try {
            parts = string.split("(?<=Corporation)");
            mSender = parts[0];
            parts = mSender.split("\n");
            mSender = parts[1];
        } catch (Exception e) {
        }


        //amount
        try {
            mAmount = string.substring(string.lastIndexOf("$") + 1);
            parts = mAmount.split("\n");
            mAmount = parts[0];
        } catch (Exception e) {
        }


        //email
        try {
            mEmail = mSender.replaceAll("\\s+", "").toLowerCase() + "@gmail.com";
        } catch (Exception e) {
        }

        String allInformation =
                "Invoice number: " + mInvoiceNo + "\n" +
                        "Due date: " + mDate + "\n" +
                        "Sender: " + mSender + "\n" +
                        "Amount: " + mAmount + "\n" +
                        "Email:" + mEmail;


        List list = Arrays.asList(mInvoiceNo, mDate, mSender, mAmount, "unpaid", mEmail, allInformation);
        invoiceInformation.addAll(list);


        return invoiceInformation;
    }
}