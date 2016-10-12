package tz.co.wadau.calenderapp.models;


public class MCCategory {
    int id;
    String name;

    public MCCategory(){

    }

    public MCCategory(String name){
        this.name = name;
    }

    public MCCategory(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
