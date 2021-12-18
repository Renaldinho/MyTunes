package be;
public class PlayList {
    int id,songs;
    String name;

    String time;
    public PlayList(int id, String name, int songs, String time) {
    this.id=id;
    this.name=name;
    this.songs=songs;
    this.time=time;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setSongs(int songs) {
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getSong() {
        return songs;
    }

    public String getTime() {
        return time;
    }


}
