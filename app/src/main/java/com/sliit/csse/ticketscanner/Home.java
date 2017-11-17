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
    private DatabaseReference mDatabaseUser,DatabaseUser,mDatabaseUserA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        DatabaseUser= FirebaseDatabase.getInstance().getReference().child("TRAVEL_INFO");
        mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("USERS").child("12345678V");
        mDatabaseUserA=mDatabaseUser.child("TRAVEL");
       // DatabaseReference db=mDatabaseUserA.push();
     //   db.child("FROM").setValue("hfjgj");
       // db.child("TO").setValue("hfjgj");
        //db.child("START_DATE").setValue("hfjgj");

        mDatabaseUser.child("FNAME").setValue("Kasun");
        mDatabaseUser.child("LNAME").setValue("Kasun");
        mDatabaseUser.child("CONTACT_NO").setValue("23");
        mDatabaseUser.child("NIC").setValue("23");
        mDatabaseUser.child("CARD_ID").setValue("23");
      //  mDatabaseUser.child("AMOUNT").setValue("230");
        mDatabaseUser.child("LOAN").setValue("0");

     //   DatabaseReference db1=DatabaseUser.push();
      //  db1.child("FROM").setValue("Colombo");
       /// db1.child("TO").setValue("Kurunegala");
        //db1.child("PRICE").setValue("20");
        //db1.child("CLASS").setValue("23");

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
