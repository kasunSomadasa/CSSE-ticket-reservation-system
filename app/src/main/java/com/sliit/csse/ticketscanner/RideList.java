package com.sliit.csse.ticketscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Kasun
 * This is RideList page which is use for display ride history
 */
public class RideList extends AppCompatActivity {

    private RecyclerView rideList;
    private DatabaseReference databaseTravel;
    String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_list);

        //get user key from UserHome activity
        userKey = getIntent().getExtras().getString("postId");
        ////create database references
        databaseTravel= FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Travel");

        rideList=(RecyclerView)findViewById(R.id.listOfRides);
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rideList.setHasFixedSize(true);
        rideList.setLayoutManager(mManager);
    }

    /**
     * load all ride details to list view when activity open
     */
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Travel,rideViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Travel,rideViewHolder>(
                Travel.class,
                R.layout.list,
                rideViewHolder.class,
                databaseTravel
        ){
            @Override
            protected void populateViewHolder(rideViewHolder viewHolder, Travel model, int position) {

                final String rideKey=getRef(position).getKey();
                //display from,to,start date value in list view items
                viewHolder.setFrom(model.getFrom());
                viewHolder.setTo(model.getTo());
                viewHolder.setStartDate(model.getStartDate());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //send user key and single ride key to SingleRide activity when user click on list view item
                        Intent OnePost=new Intent(RideList.this, SingleRide.class);
                        OnePost.putExtra("singlePostId",rideKey);
                        OnePost.putExtra("postId",userKey);
                        startActivity(OnePost);
                    }
                });
            }
            };
            //set adapter to list view
            rideList.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.notifyDataSetChanged();

    }

    public  static  class rideViewHolder extends  RecyclerView.ViewHolder{
        View view;
        public  rideViewHolder(View itemView){
            super(itemView);
            view=itemView;
        }
        public void setFrom(String from){
            TextView ride_from=(TextView)view.findViewById(R.id.fromText);
            ride_from.setText(from);
        }
        public void setTo(String to){
            TextView ride_to=(TextView)view.findViewById(R.id.toText);
            ride_to.setText(to);
        }
        public void setStartDate(String date){
            TextView ride_startDate=(TextView)view.findViewById(R.id.dateText);
            ride_startDate.setText(date);
        }
    }
}
