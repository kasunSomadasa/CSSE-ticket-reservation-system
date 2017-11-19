package com.sliit.csse.ticketscanner;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EndRide extends AppCompatActivity {

    String userKey,amount,loan,to,price,from,travelKey,formattedDate,formattedTime;
    private DatabaseReference databaseUsers,databaseTravelInfo,databaseUsersUpdate,databaseUserStartPoint;
    Query searchTravelAmountQuary,searchTravelStartPointQuary;
    private Button endJourneyBtn;
    private Spinner endSpinner;
    private TextView endDateText;
    static final String[] endCitys=new String[]{"Choose your end point","Colombo","Malabe","Kaduwela","Kandy","Kurunegala"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_ride);

        userKey = getIntent().getExtras().getString("postId");
        //userKey = "12345678V";
        endJourneyBtn=(Button)findViewById(R.id.journeyEnd);
        databaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        databaseUsersUpdate= FirebaseDatabase.getInstance().getReference().child("Users").child(userKey);
        databaseUserStartPoint=FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Travel");
        databaseTravelInfo = FirebaseDatabase.getInstance().getReference().child("TravelInfo");


        databaseUsers.child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                amount = (String) dataSnapshot.child("amount").getValue().toString();
                loan = (String) dataSnapshot.child("loan").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        searchTravelStartPointQuary= databaseUserStartPoint.orderByChild("startRideFlag").equalTo("true");
        searchTravelStartPointQuary.addValueEventListener( new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    from = (String) postSnapshot.child("from").getValue().toString();
                    travelKey=postSnapshot.getKey();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        endSpinner=(Spinner)findViewById(R.id.endPoint);
        ArrayAdapter<String> endAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,endCitys);
        endSpinner.setAdapter(endAdapter);
        endDateText = (TextView)findViewById(R.id.endDate);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c.getTime());
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        formattedTime = tf.format(c.getTime());

        endDateText.setText("Current Date: "+formattedDate);


        endJourney();


    }

    private void endJourney() {
        endJourneyBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        searchTravelAmountQuary= databaseTravelInfo.orderByChild("from").equalTo(from);
                        searchTravelAmountQuary.addValueEventListener( new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                {

                                    to = (String) postSnapshot.child("to").getValue().toString();

                                    if(to.equals(endSpinner.getSelectedItem().toString())){

                                        price = (String) postSnapshot.child("price").getValue().toString();
                                    
                                        checkAmount(amount,price,loan);
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });


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
            Toast.makeText(EndRide.this,"We have deduct Rs."+price+" from your account.Have a safe journey...",Toast.LENGTH_LONG).show();
            double newAmount=amount-price;
            databaseUsersUpdate.child("amount").setValue(newAmount);
            databaseUsersUpdate.child("rideFlag").setValue("false");

            databaseUserStartPoint.child(travelKey).child("to").setValue(endSpinner.getSelectedItem().toString());
            databaseUserStartPoint.child(travelKey).child("startRideFlag").setValue("false");
            databaseUserStartPoint.child(travelKey).child("endDate").setValue(formattedDate);
            databaseUserStartPoint.child(travelKey).child("endTime").setValue(formattedTime);
            databaseUserStartPoint.child(travelKey).child("ticketPrice").setValue(price);

        }else{
            if(loan>0){
                double total = amount+loan;
                if(total >= price){
                    //ok
                    Toast.makeText(EndRide.this,"We have deduct Rs."+price+" from your account.Have a safe journey...",Toast.LENGTH_LONG).show();
                    databaseUsersUpdate.child("rideFlag").setValue("false");
                    databaseUserStartPoint.child(travelKey).child("startRideFlag").setValue("false");
                    databaseUserStartPoint.child(travelKey).child("to").setValue(endSpinner.getSelectedItem().toString());
                    databaseUserStartPoint.child(travelKey).child("endDate").setValue(formattedDate);
                    databaseUserStartPoint.child(travelKey).child("endTime").setValue(formattedTime);
                    databaseUserStartPoint.child(travelKey).child("ticketPrice").setValue(price);

                    if(loan>=price){
                        loan=loan-price;
                        databaseUsersUpdate.child("loan").setValue(loan);
                    }else{
                        price=price-loan;
                        loan=0;
                        amount=amount-price;
                        databaseUsersUpdate.child("amount").setValue(amount);
                        databaseUsersUpdate.child("loan").setValue(loan);
                    }


                }else{
                    //no
                    Toast.makeText(EndRide.this,"Sorry,We can't pay for your jouney.Your account and loan balance are too low...",Toast.LENGTH_LONG).show();
                }

            }else{
                //loan message
                showLoanDialog();
            }
        }


    }


    private void showLoanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Do you want a loan?");
        builder.setMessage("Your account blance is loo low,You can get a Rs 200.00 loan.");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                databaseUsersUpdate.child("loan").setValue("200");
                databaseUsersUpdate.child("loanFlag").setValue("true");

                Toast.makeText(EndRide.this,"Now,You have Rs.200.00 loan",Toast.LENGTH_LONG).show();
                /*
                Thread t = new Thread() {
                    @Override
                    public void run() {
                            try {
                                sleep(1000);

                                checkAmount(amount,price,loan);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                    }
                };
                t.start();
                */


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EndRide.this,"Sorry,We can't pay for your jouney.Your account and loan balance are too low...",Toast.LENGTH_LONG).show();
                //dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
