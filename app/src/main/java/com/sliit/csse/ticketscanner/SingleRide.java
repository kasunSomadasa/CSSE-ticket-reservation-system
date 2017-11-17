package com.sliit.csse.ticketscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleRide extends AppCompatActivity {

    String rideKey;
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


        rideKey = getIntent().getExtras().getString("post_id");

        databaseTravel= FirebaseDatabase.getInstance().getReference().child("USERS").child("12345678V").child("TRAVEL");
        databaseTravel.child(rideKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String from = (String) dataSnapshot.child("FROM").getValue();
                String to = (String) dataSnapshot.child("TO").getValue();
                String startDate = (String) dataSnapshot.child("START_DATE").getValue();
                String endDate = (String) dataSnapshot.child("EndDate").getValue();
                String startTime = (String) dataSnapshot.child("StartTime").getValue();
                String endTime = (String) dataSnapshot.child("EndTime").getValue();
                String price = (String) dataSnapshot.child("TicketPrice").getValue().toString();

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
