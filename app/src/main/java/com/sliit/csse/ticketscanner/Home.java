package com.sliit.csse.ticketscanner;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Home extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    Button scannerBtn, homeBtn;
    private DatabaseReference mDatabaseUser;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        //request permission to read camera
        checkAndRequestPermissions();

        mDatabaseUser.child("Name").setValue("Kasun");
        mDatabaseUser.child("Age").setValue("23");

        homeBtn = (Button) findViewById(R.id.homeButton);
        scannerBtn = (Button) findViewById(R.id.scannerButton);
        //openScanner();
        openHome();

    }

    public void openHome() {
        homeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Home.this, UserHome.class);
                        startActivity(intent);
                    }
                }
        );
    }


    /**
     *
     * below methods use for qr code reading
     *
     */
    private  boolean checkAndRequestPermissions() {

        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;

    }

    public void onClick(View v){

        scannerView =new ZXingScannerView(this);
        setContentView(scannerView);
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
/*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan result");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        */
        openAddNumberPopUp(result.getText());

    }

    public void openAddNumberPopUp(String value) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Home.this);
        final View mView = getLayoutInflater().inflate(R.layout.popup_model, null);

        Button addBtn = mView.findViewById(R.id.AddBtn);
        TextView txtView = mView.findViewById(R.id.valueTxtView);

        txtView.setText(value);

        mBuilder.setView(mView);
        final AlertDialog alertDialog = mBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        alertDialog.show();

        scannerView.resumeCameraPreview(this);
    }
}
