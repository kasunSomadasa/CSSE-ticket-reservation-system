package com.sliit.csse.ticketscanner;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StartRide extends AppCompatActivity {

    String userKey,amount,loan,loanFlag;
    private DatabaseReference databaseUsers,databaseUsersUpdate,databaseUserStartPoint;
    private Button startJourneyBtn;
    private Spinner startSpinner;
    static final String[] startCitys=new String[]{"Choose your start point","Colombo","Malabe","Kaduwela","Kandy","Kurunegala"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);

        //userKey = getIntent().getExtras().getString("post_id");
        userKey = "12345678V";
        startJourneyBtn=(Button)findViewById(R.id.journey);
        databaseUsers= FirebaseDatabase.getInstance().getReference().child("USERS");
        databaseUserStartPoint=FirebaseDatabase.getInstance().getReference().child("USERS").child(userKey).child("TRAVEL");
        databaseUsersUpdate= FirebaseDatabase.getInstance().getReference().child("USERS").child(userKey);




        databaseUsers.child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                amount = (String) dataSnapshot.child("AMOUNT").getValue().toString();
                loan = (String) dataSnapshot.child("LOAN").getValue().toString();
                loanFlag = (String) dataSnapshot.child("loanFlag").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        startSpinner=(Spinner)findViewById(R.id.startPoint);
        ArrayAdapter<String> startAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,startCitys);
        startSpinner.setAdapter(startAdapter);


        startJourney();
        //Toast.makeText(StartRide.this,startSpinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
    }

    private void startJourney() {
        startJourneyBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        checkAmount(amount,"200",loan);


                    }
                }

        );
    }

    public void checkAmount(String dbAmount,String dbPrice,String dbLoan){

        double amount = Double.parseDouble(dbAmount);
        double price = Double.parseDouble(dbPrice);
        double loan = Double.parseDouble(dbLoan);


        if(amount >= price){
            //ok
            databaseUsersUpdate.child("rideFlag").setValue("true");

            DatabaseReference db=databaseUserStartPoint.push();
            db.child("FROM").setValue(startSpinner.getSelectedItem().toString());
            db.child("TO").setValue("");
            db.child("START_DATE").setValue("hfjgj");
            db.child("StartRideFlag").setValue("true");

            Toast.makeText(StartRide.this,"1 Have a safe journey...",Toast.LENGTH_LONG).show();

        }else{
            if(loanFlag.equals("true")){

                if(loan>0){
                    double total = amount+loan;
                    if(total >= price){
                        //ok
                        databaseUsersUpdate.child("rideFlag").setValue("true");
                        DatabaseReference db=databaseUserStartPoint.push();
                        db.child("FROM").setValue(startSpinner.getSelectedItem().toString());
                        db.child("TO").setValue("");
                        db.child("START_DATE").setValue("hfjgj");
                        db.child("StartRideFlag").setValue("true");

                        Toast.makeText(StartRide.this," 2Have a safe journey...",Toast.LENGTH_LONG).show();

                    }else{
                        //no
                        Toast.makeText(StartRide.this,"1Sorry,We can't pay for your jouney.Your account and loan balance are too low...",Toast.LENGTH_LONG).show();
                    }

                }else{
                    //loan message
                    Toast.makeText(StartRide.this,"2Sorry,We can't pay for your jouney.Your account and loan balance are too low...",Toast.LENGTH_LONG).show();
                }

            }else{
                databaseUsersUpdate.child("rideFlag").setValue("true");
                DatabaseReference db=databaseUserStartPoint.push();
                db.child("FROM").setValue(startSpinner.getSelectedItem().toString());
                db.child("TO").setValue("");
                db.child("START_DATE").setValue("hfjgj");
                db.child("StartRideFlag").setValue("true");
                Toast.makeText(StartRide.this," 3Have a safe journey...",Toast.LENGTH_LONG).show();
            }

        }


    }


}
