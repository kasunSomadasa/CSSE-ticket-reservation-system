package com.sliit.csse.ticketscanner;

/**
 * Created by Kasun on 11/15/2017.
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
