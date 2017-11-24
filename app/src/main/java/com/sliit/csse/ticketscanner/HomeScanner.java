package com.sliit.csse.ticketscanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import java.util.ArrayList;
import java.util.List;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Monaridu
 * This is Scanner page which is use for scan QR code
 */
public class HomeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    Button scannerBtn,homeBtn;
    private  DatabaseReference databaseUsers;
    private static final String TAG = "1";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private ZXingScannerView scannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //create database reference from 'Users' branch
        databaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        //request permission to read camera
        checkAndRequestPermissions();

        homeBtn = (Button) findViewById(R.id.homeButton);
        scannerBtn = (Button) findViewById(R.id.scannerButton);

        openHome();
        //open QR scanner
        openScanner();
    }

    public void openHome() {
        homeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(HomeScanner.this, UserHome.class);
                        startActivity(intent);
                    }
                }
        );
    }


    /**
     * below methods use for request permission for camera
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

    //open scanner
    public void openScanner() {
        scannerView =new ZXingScannerView(HomeScanner.this);
        setContentView(scannerView);
        scannerView.setResultHandler(HomeScanner.this);
        scannerView.startCamera();
    }

    //pause scanner
    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    //read QR code
    @Override
    public void handleResult(Result result) {

        //check that QR code exist in firebase
        final String userId=result.getText();
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChild(userId)) {
                    //give success msg
                    successAlert(userId);
                }else{
                    //if not error msg
                    errorAlert();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void successAlert(final String userId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Welcome to the Ticket system.");
        builder.setMessage("We detected your card number.Do you want to start your journey?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Log.i(TAG,"Entering to the system");
                //go to the user home page
                Intent intent = new Intent(HomeScanner.this, UserHome.class);
                intent.putExtra("postId",userId);
                startActivity(intent);

            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }


    public void errorAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Card is invalid");
        builder.setMessage("We can't detected your card number.Do you want to check again?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //reopen camera
                scannerView.resumeCameraPreview(HomeScanner.this);
                Log.i(TAG,"Invalid Number");
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG,"Exit from the system");
                //exit from application
                System.exit(0);

            }
        });


        AlertDialog alert = builder.create();
        alert.show();

    }
}
