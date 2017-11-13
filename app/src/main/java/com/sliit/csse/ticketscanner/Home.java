package com.sliit.csse.ticketscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {

    Button scannerBtn,homeBtn;
    private DatabaseReference mDatabaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseUser.child("Name").setValue("Kasun");
        mDatabaseUser.child("Age").setValue("23");

        homeBtn=(Button) findViewById(R.id.homeButton);
        scannerBtn=(Button) findViewById(R.id.scannerButton);
        openScanner();
        openHome();

    }

    public void openScanner() {
        scannerBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Home.this, Scanner.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void openHome() {
        homeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Home.this, UserHome.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
