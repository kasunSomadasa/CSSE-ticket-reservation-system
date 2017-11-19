package com.sliit.csse.ticketscanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;


import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class Home extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    Button scannerBtn,homeBtn;
    //private DatabaseReference mDatabaseUser,DatabaseUser,mDatabaseUserA;
    private  DatabaseReference databaseUsers;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private ZXingScannerView scannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");

       // mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        //request permission to read camera
        checkAndRequestPermissions();

/*
        DatabaseUser= FirebaseDatabase.getInstance().getReference().child("TravelInfo");
        mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("Users").child("12345678V");
        mDatabaseUserA=mDatabaseUser.child("Travel");
        DatabaseReference db=mDatabaseUserA.push();
        db.child("From").setValue("hfjgj");
        db.child("To").setValue("hfjgj");
        db.child("StartDate").setValue("hfjgj");

        mDatabaseUser.child("Fname").setValue("Kasun");
        mDatabaseUser.child("Lname").setValue("Kasun");
        mDatabaseUser.child("ContactNo").setValue("23");
        mDatabaseUser.child("NIC").setValue("23");
        mDatabaseUser.child("CardId").setValue("23");
        mDatabaseUser.child("Amount").setValue("230");
        mDatabaseUser.child("Loan").setValue("0");
        mDatabaseUser.child("RideFlag").setValue("false");
        mDatabaseUser.child("LoanFlag").setValue("false");

     DatabaseReference db1=DatabaseUser.push();
       db1.child("From").setValue("Colombo");
        db1.child("To").setValue("Kurunegala");
        db1.child("Price").setValue("150");
        db1.child("Class").setValue("3");
*/
        homeBtn = (Button) findViewById(R.id.homeButton);
        scannerBtn = (Button) findViewById(R.id.scannerButton);
        //openScanner();
        openHome();

        openScanner();
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


    public void openScanner() {
        scannerView =new ZXingScannerView(Home.this);
        setContentView(scannerView);
        scannerView.setResultHandler(Home.this);
        scannerView.startCamera();
    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        final String userId=result.getText();
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChild(userId)) {
                    succesAlert(userId);
                }else{
                    errorAlert();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void succesAlert(final String value) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Welcome to the Ticket system.");
        builder.setMessage("We detected your card number.Do you want to start your journey?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(Home.this,value,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Home.this, UserHome.class);
                intent.putExtra("postId",value);
                startActivity(intent);

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
      //  scannerView.resumeCameraPreview(this);
    }


    public void errorAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Card is invalid");
        builder.setMessage("We can't detected your card number.Do you want to check again?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                scannerView.resumeCameraPreview(Home.this);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                System.exit(0);

            }
        });




        AlertDialog alert = builder.create();
        alert.show();

    }
}
