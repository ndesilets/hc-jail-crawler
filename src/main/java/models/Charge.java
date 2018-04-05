package models;

import java.util.Date;

public class Charge {
    private int statuteChapter;
    private int statuteCharge;
    private String name;
    private Date date;
    private Date booking;
    private Float bailAmount;
    private String incidentTag;

    public Charge() {

    }

    public Charge(int statuteChapter, int statuteCharge, String name, Date date, Date booking, Float bailAmount, String incidentTag) {
        this.statuteChapter = statuteChapter;
        this.statuteCharge = statuteCharge;
        this.name = name;
        this.date = date;
        this.booking = booking;
        this.bailAmount = bailAmount;
        this.incidentTag = incidentTag;
    }

    public int getStatuteChapter() {
        return statuteChapter;
    }

    public void setStatuteChapter(int statuteChapter) {
        this.statuteChapter = statuteChapter;
    }

    public int getStatuteCharge() {
        return statuteCharge;
    }

    public void setStatuteCharge(int statuteCharge) {
        this.statuteCharge = statuteCharge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getBooking() {
        return booking;
    }

    public void setBooking(Date booking) {
        this.booking = booking;
    }

    public Float getBailAmount() {
        return bailAmount;
    }

    public void setBailAmount(Float bailAmount) {
        this.bailAmount = bailAmount;
    }

    public String getIncidentTag() {
        return incidentTag;
    }

    public void setIncidentTag(String incidentTag) {
        this.incidentTag = incidentTag;
    }
}
