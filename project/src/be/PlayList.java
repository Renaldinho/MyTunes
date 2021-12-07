package be;

public class PlayList {
    int id;
    String name;
    int songs;
    String time;
    public PlayList(int id, String name, int songs,String time) {
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

    public String getTime() {
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
