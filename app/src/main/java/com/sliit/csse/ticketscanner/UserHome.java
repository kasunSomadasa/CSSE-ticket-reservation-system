package com.sliit.csse.ticketscanner;

import android.app.ProgressDialog;
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

import java.util.Timer;
import java.util.TimerTask;

import br.com.bloder.magic.Magic;
import br.com.bloder.magic.view.MagicButton;

public class UserHome extends AppCompatActivity {

    MagicButton startRideBtn,rideListBtn,accountBtn;
    String userKey,rideFlag;
    private DatabaseReference databaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        userKey = getIntent().getExtras().getString("postId");
        //userKey = "12345678V";
        databaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        databaseUsers.child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                rideFlag = (String) dataSnapshot.child("RideFlag").getValue().toString();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        startRideBtn=(MagicButton) findViewById(R.id.startRide);
        rideListBtn=(MagicButton) findViewById(R.id.rideList);
        accountBtn=(MagicButton) findViewById(R.id.account);


        final ProgressDialog dialog = new ProgressDialog(UserHome.this);
        dialog.setTitle("Loading...");
        dialog.setMessage("Please wait");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        long delayInMillis = 4000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delayInMillis);

        openRideList();
        openAccount();
        openRide();


    }

    private void openAccount() {
        accountBtn.setMagicButtonClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserHome.this, ViewAccount.class);
                        intent.putExtra("postId",userKey);
                        startActivity(intent);
                    }
                }
        );
    }

    private void openRideList() {
        rideListBtn.setMagicButtonClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserHome.this, RideList.class);
                        intent.putExtra("postId",userKey);
                        startActivity(intent);
                    }
                }
        );
    }

    private void openRide() {
        startRideBtn.setMagicButtonClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(rideFlag.equals("true")){
                            Intent intent = new Intent(UserHome.this, EndRide.class);
                            intent.putExtra("postId",userKey);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(UserHome.this, StartRide.class);
                            intent.putExtra("postId",userKey);
                            startActivity(intent);
                        }

                    }
                }
        );
    }
}
