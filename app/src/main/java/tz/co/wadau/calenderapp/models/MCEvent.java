package tz.co.wadau.calenderapp.models;

public class MCEvent {
    int id;
    String date;
    String color;

    public MCEvent(){

    }

    public MCEvent(String date, String color){
        this.date = date;
        this.color = color;
    }

    public MCEvent(int id, String date, String color){
        this.id = id;
        this.date = date;
        this.color = color;
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

}