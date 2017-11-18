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

public class RideList extends AppCompatActivity {

    private RecyclerView rideList;
    private DatabaseReference databaseTravel;
    String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_list);

        userKey = getIntent().getExtras().getString("postId");
        databaseTravel= FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Travel");
       // databaseTravel= FirebaseDatabase.getInstance().getReference().child("TRAVEL_INFO");

        rideList=(RecyclerView)findViewById(R.id.listOfRides);
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rideList.setHasFixedSize(true);
        rideList.setLayoutManager(mManager);
    }

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

                viewHolder.setFROM(model.getFrom());
                viewHolder.setTO(model.getTo());
                viewHolder.setSTART_DATE(model.getStartDate());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent OnePost=new Intent(RideList.this, SingleRide.class);
                        OnePost.putExtra("singlePostId",rideKey);
                        OnePost.putExtra("postId",userKey);
                        startActivity(OnePost);
                    }
                });
            }
            };



            rideList.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.notifyDataSetChanged();

    }

    public  static  class rideViewHolder extends  RecyclerView.ViewHolder{
        View view;
        public  rideViewHolder(View itemView){
            super(itemView);
            view=itemView;
        }
        public void setFROM(String from){
            TextView ride_from=(TextView)view.findViewById(R.id.fromText);
            ride_from.setText(from);
        }
        public void setTO(String to){
            TextView ride_to=(TextView)view.findViewById(R.id.toText);
            ride_to.setText(to);
        }
        public void setSTART_DATE(String date){
            TextView ride_startDate=(TextView)view.findViewById(R.id.dateText);
            ride_startDate.setText(date);
        }
    }
}
