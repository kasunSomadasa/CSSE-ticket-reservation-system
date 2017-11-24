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
 * This is ViewAccount page which is use for display user account details
 */
public class ViewAccount extends AppCompatActivity {

    String userKey;
    private TextView amountText;
    private TextView loanText,detailText;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        amountText = (TextView) findViewById(R.id.amountView);
        loanText = (TextView) findViewById(R.id.loanView);
        detailText = (TextView) findViewById(R.id.details);
        //get user key from HomeScanner activity
        userKey = getIntent().getExtras().getString("postId");
        //create database references
        databaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");

        databaseUsers.child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get amount,loan value belongs to user and set into relevant text fields
                String amount = (String) dataSnapshot.child("amount").getValue().toString();
                String loan = (String) dataSnapshot.child("loan").getValue().toString();

                amountText.setText(amount+".00");
                loanText.setText(loan+".00");
                if(loan.equals("0")){
                    detailText.setText("Now your account have Rs."+amount+".00 and you have not any loan");
                }else {
                    detailText.setText("Now your account have Rs."+amount+".00 and Rs."+loan+".00 in your loan");
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
