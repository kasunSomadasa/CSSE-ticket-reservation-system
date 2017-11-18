package com.sliit.csse.ticketscanner;

/**
 * Created by Kasun on 11/15/2017.
 */

public class Travel {
    private String From,To,StartDate;

    public Travel() {}

    public Travel(String From, String To, String START_DATE) {
        this.From = From;
        this.To = To;
        this.StartDate = START_DATE;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String From) {
        this.From = From;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String To) {
        this.To = To;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }
}
