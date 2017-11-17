package com.sliit.csse.ticketscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewAccount extends AppCompatActivity {

    String userKey;
    private TextView amountText;
    private TextView loanText;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        amountText = (TextView) findViewById(R.id.amountView);
        loanText = (TextView) findViewById(R.id.loanView);

        //userKey = getIntent().getExtras().getString("post_id");
        userKey = "12345678V";

        databaseUsers= FirebaseDatabase.getInstance().getReference().child("USERS");
        databaseUsers.child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String amount = (String) dataSnapshot.child("AMOUNT").getValue().toString();
                String loan = (String) dataSnapshot.child("LOAN").getValue().toString();

                amountText.setText(amount);
                loanText.setText(loan);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
