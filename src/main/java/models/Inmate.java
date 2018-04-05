package models;

import java.util.ArrayList;

public class Inmate {
    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }
    public enum Race {
        WHITE,
        BLACK,
        HISPANIC,
        ASIAN,
        NATIVE,
        OTHER
    }
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String dob;
    private Gender gender;
    private Race race;
    private String date;
    private String bookingNumber;
    private String incidentTag;
    private ArrayList<Charge> charges;

    public Inmate() {

    }

    public Inmate(String firstName, String middleName, String lastName, String suffix, String dob, Gender gender, Race race, String date, String bookingNumber, String incidentTag) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.suffix = suffix;
        this.dob = dob;
        this.gender = gender;
        this.race = race;
        this.date = date;
        this.bookingNumber = bookingNumber;
        this.incidentTag = incidentTag;
        this.charges = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getIncidentTag() {
        return incidentTag;
    }

    public void setIncidentTag(String incidentTag) {
        this.incidentTag = incidentTag;
    }

    public ArrayList<Charge> getCharges() {
        return this.charges;
    }

    public void addCharge(Charge charge) {
        this.charges.add(charge);
    }

    @Override
    public String toString() {
        return "Inmate{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", suffix='" + suffix + '\'' +
                ", dob='" + dob + '\'' +
                ", gender=" + gender +
                ", race=" + race +
                ", date='" + date + '\'' +
                ", bookingNumber='" + bookingNumber + '\'' +
                ", incidentTag='" + incidentTag + '\'' +
                ", charges=" + charges +
                '}';
    }
}
