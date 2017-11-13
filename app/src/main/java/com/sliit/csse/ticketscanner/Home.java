package com.sliit.csse.ticketscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {

    private DatabaseReference mDatabaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseUser.child("Name").setValue("Kasun");
        mDatabaseUser.child("Age").setValue("23");
    }
}
