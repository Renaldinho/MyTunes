package be;

public class PlayList {
    int id;
    String name;
    int songs,time;
    public PlayList(int id, String name, int songs, int time) {
    this.id=id;
    this.name=name;
    this.songs=songs;
    this.time=time;
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

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", songs=" + songs +
                ", time=" + time +
                '}';
    }
}
