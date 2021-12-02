package be;

import java.sql.Time;

public class Song {
    private Integer id;
    private String title;
    private String artist;
    private String category;
    private Integer time;

    public Song(Integer id,String title, String artist, String category, Integer time, String filePath){
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.time = time;
    }

    public Song(String title, String artist, String category, Integer time){
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.time = time;
    }


    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
