package com.sliit.csse.ticketscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Kasun
 * This is SingleRide page which is use for display single ride details in history
 */
public class SingleRide extends AppCompatActivity {

    String rideKey,userKey;
    private TextView fromText,startTimeText,endDateText,endTimeText,amountText;
    private TextView toText;
    private TextView startDateText;
    private DatabaseReference databaseTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_ride);

        fromText = (TextView) findViewById(R.id.fromView);
        toText = (TextView) findViewById(R.id.toView);
        startDateText= (TextView) findViewById(R.id.startDateView);
        endDateText = (TextView) findViewById(R.id.endDateView);
        startTimeText = (TextView) findViewById(R.id.startTimeView);
        endTimeText= (TextView) findViewById(R.id.endTimeView);
        amountText = (TextView) findViewById(R.id.amountView);

        //get user key and single ride key from RideList activity
        rideKey = getIntent().getExtras().getString("singlePostId");
        userKey = getIntent().getExtras().getString("postId");
        //create database references
        databaseTravel= FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Travel");
        databaseTravel.child(rideKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all information in Travel branch
                String from = (String) dataSnapshot.child("from").getValue();
                String to = (String) dataSnapshot.child("to").getValue();
                String startDate = (String) dataSnapshot.child("startDate").getValue();
                String endDate = (String) dataSnapshot.child("endDate").getValue();
                String startTime = (String) dataSnapshot.child("startTime").getValue();
                String endTime = (String) dataSnapshot.child("endTime").getValue();
                String price = (String) dataSnapshot.child("ticketPrice").getValue().toString();
                //set above data to relevant text fields
                fromText.setText(from);
                toText.setText(to);
                startDateText.setText(startDate);
                endDateText.setText(endDate);
                startTimeText.setText(startTime);
                endTimeText.setText(endTime);
                amountText.setText("Rs."+price+".00");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
