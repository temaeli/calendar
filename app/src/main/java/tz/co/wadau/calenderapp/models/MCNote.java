package tz.co.wadau.calenderapp.models;

public class MCNote {
    private long id;
    private String createdAt;
    private String text;

    public MCNote(String createdAt, String text){
        this.createdAt = createdAt;
        this.text = text;
    }

    public MCNote(long id, String createdAt, String text){
        this.id = id;
        this.createdAt = createdAt;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setText(String text) {
        this.text = text;
    }
}
