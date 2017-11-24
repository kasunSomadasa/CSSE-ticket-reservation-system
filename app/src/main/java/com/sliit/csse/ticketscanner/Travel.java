package com.sliit.csse.ticketscanner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Kasun
 * This is Travel class which is use for display ride details in list view
 */

public class Travel {
    private String from,to,startDate;


    public Travel() {}

    public Travel(String from, String to, String startDate) {
        this.from = from;
        this.to = to;
        this.startDate = startDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
