package com.sliit.csse.ticketscanner;

/**
 * Created by Kasun on 11/15/2017.
 */

public class Travel {
    private String FROM,TO,START_DATE;

    public Travel() {}

    public Travel(String FROM, String TO, String START_DATE) {
        this.FROM = FROM;
        this.TO = TO;
        this.START_DATE = START_DATE;
    }

    public String getFROM() {
        return FROM;
    }

    public void setFROM(String FROM) {
        this.FROM = FROM;
    }

    public String getTO() {
        return TO;
    }

    public void setTO(String TO) {
        this.TO = TO;
    }

    public String getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(String START_DATE) {
        this.START_DATE = START_DATE;
    }
}
