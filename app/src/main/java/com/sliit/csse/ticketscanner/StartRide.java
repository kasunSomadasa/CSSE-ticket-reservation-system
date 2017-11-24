package com.sliit.csse.ticketscanner;

import android.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Kasun
 * This is StartRide page which is use for start ride
 */
public class StartRide extends AppCompatActivity {

    private static final String TAG = "1";
    String userKey,amount,loan,loanFlag,formattedDate,formattedTime;
    String maxTicketPrice="200";
    private DatabaseReference databaseUsers,databaseUsersUpdate,databaseUserStartPoint;
    private Button startJourneyBtn;
    private Spinner startSpinner;
    private TextView startDateText;
    static final String[] startCitys=new String[]{"Choose your start point","Colombo","Malabe","Kaduwela","Kandy","Kurunegala"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);

        startJourneyBtn=(Button)findViewById(R.id.journey);
        //get user key from HomeScanner activity
        userKey = getIntent().getExtras().getString("postId");
        //create database references
        databaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        databaseUserStartPoint=FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Travel");
        databaseUsersUpdate= FirebaseDatabase.getInstance().getReference().child("Users").child(userKey);

        databaseUsers.child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get amount,loan,loanFlag value belongs to user
                amount = (String) dataSnapshot.child("amount").getValue().toString();
                loan = (String) dataSnapshot.child("loan").getValue().toString();
                loanFlag = (String) dataSnapshot.child("loanFlag").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //set startCitys array to spinner
        startSpinner=(Spinner)findViewById(R.id.startPoint);
        ArrayAdapter<String> startAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,startCitys);
        startSpinner.setAdapter(startAdapter);
        startDateText=(TextView)findViewById(R.id.startDate);

        //set current date
        formattedDate = getFormattedDate();
        //set current time
        formattedTime = getFormattedTime();
        startDateText.setText("Current Date: "+formattedDate);

        startJourney();
    }

    //get current date in "yyyy-MM-dd" format
    private String getFormattedDate(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    //set current time in "HH:mm:ss" format
    private String getFormattedTime(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c.getTime());

        return formattedDate;
    }


    private void startJourney() {
        startJourneyBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //check user enter the start point
                        if(startSpinner.getSelectedItem().toString().equals("Choose your start point")){
                            Toast.makeText(StartRide.this,"Please mention your start point",Toast.LENGTH_LONG).show();
                        }else {
                            checkAmount(amount, maxTicketPrice, loan);
                        }
                    }
                }

        );
    }

    /**
      * check user current amount is enough for payment
      */
    public void checkAmount(String dbAmount,String dbPrice,String dbLoan){

        double amount = Double.parseDouble(dbAmount);
        double price = Double.parseDouble(dbPrice);
        double loan = Double.parseDouble(dbLoan);

        if (amount >= price) {
            //give success msg
            databaseUsersUpdate.child("rideFlag").setValue("true");

            DatabaseReference db = databaseUserStartPoint.push();
            db.child("from").setValue(startSpinner.getSelectedItem().toString());
            db.child("to").setValue("");
            db.child("startDate").setValue(formattedDate);
            db.child("startTime").setValue(formattedTime);
            db.child("startRideFlag").setValue("true");

            Log.i(TAG,"start ride");
            Toast.makeText(StartRide.this, "Have a safe journey...", Toast.LENGTH_LONG).show();
            showNotification(startSpinner.getSelectedItem().toString());
        } else {
            if (loanFlag.equals("true")) {

                if (loan > 0) {
                    double total = amount + loan;
                    if (total >= price) {
                        //give success msg
                        databaseUsersUpdate.child("rideFlag").setValue("true");
                        DatabaseReference db = databaseUserStartPoint.push();
                        db.child("from").setValue(startSpinner.getSelectedItem().toString());
                        db.child("to").setValue("");
                        db.child("startDate").setValue(formattedDate);
                        db.child("startTime").setValue(formattedTime);
                        db.child("startRideFlag").setValue("true");

                        Log.i(TAG,"start ride");
                        Toast.makeText(StartRide.this, "Have a safe journey...", Toast.LENGTH_LONG).show();
                        showNotification(startSpinner.getSelectedItem().toString());
                    } else {
                        Log.i(TAG,"can't ride,balance too low");
                        //give error msg
                        Toast.makeText(StartRide.this, "Sorry,We can't pay for your jouney.Your account and loan balance are too low...", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Log.i(TAG,"can't ride,balance too low");
                    //give error msg
                    Toast.makeText(StartRide.this, "Sorry,We can't pay for your jouney.Your account and loan balance are too low...", Toast.LENGTH_LONG).show();
                }

            } else {
                //give success msg
                databaseUsersUpdate.child("rideFlag").setValue("true");
                DatabaseReference db = databaseUserStartPoint.push();
                db.child("from").setValue(startSpinner.getSelectedItem().toString());
                db.child("to").setValue("");
                db.child("startDate").setValue(formattedDate);
                db.child("startTime").setValue(formattedTime);
                db.child("startRideFlag").setValue("true");
                Toast.makeText(StartRide.this, "Have a safe journey...", Toast.LENGTH_LONG).show();
                showNotification(startSpinner.getSelectedItem().toString());
            }

        }

    }
    public void showNotification(String from){
        /*
         * show notification with app icon on mobile notification bar
         */
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_directions_bus_white_24dp)//R.mipmap.c_directions_bus_white_24dp-->for app icon
                .setContentTitle("You have started a journey from "+from);
        Intent resultIntent = new Intent(this, HomeScanner.class); //when user click on notification then directly comes to HomeScanner activity
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, notification);
    }

}
