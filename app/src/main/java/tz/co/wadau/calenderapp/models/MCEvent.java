package tz.co.wadau.calenderapp.models;

public class MCEvent {
    int id;
    String date;
    String color;
    String firstPeriodDate;

    public MCEvent(){

    }

    public MCEvent(String date, String color, String firstPeriodDate){
        this.date = date;
        this.color = color;
        this.firstPeriodDate = firstPeriodDate;
    }

    public MCEvent(int id, String date, String color, String firstPeriodDate){
        this.id = id;
        this.date = date;
        this.color = color;
        this.firstPeriodDate = firstPeriodDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFirstPeriodDate() {
        return firstPeriodDate;
    }

    public void setFirstPeriodDate(String firstPeriodDate) {
        this.firstPeriodDate = firstPeriodDate;
    }
}
