package com.sliit.csse.ticketscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserHome extends AppCompatActivity {

    Button startRideBtn,rideListBtn,accountBtn;
    String userKey,rideFlag;
    private DatabaseReference databaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        //userKey = getIntent().getExtras().getString("post_id");
        userKey = "12345678V";
        databaseUsers= FirebaseDatabase.getInstance().getReference().child("USERS");
        databaseUsers.child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                rideFlag = (String) dataSnapshot.child("rideFlag").getValue().toString();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        startRideBtn=(Button) findViewById(R.id.startRide);
        rideListBtn=(Button) findViewById(R.id.rideList);
        accountBtn=(Button) findViewById(R.id.account);

        openRideList();
        openAccount();
        openRide();


    }

    private void openAccount() {
        accountBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserHome.this, ViewAccount.class);
                        startActivity(intent);
                    }
                }
        );
    }

    private void openRideList() {
        rideListBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserHome.this, RideList.class);
                        startActivity(intent);
                    }
                }
        );
    }

    private void openRide() {
        startRideBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(rideFlag.equals("true")){
                            Intent intent = new Intent(UserHome.this, EndRide.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(UserHome.this, StartRide.class);
                            startActivity(intent);
                        }

                    }
                }
        );
    }
}
