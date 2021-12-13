package be;

public class PlayList {
<<<<<<< Updated upstream
    int id;
    String name;
    public PlayList(int id, String name) {
=======

    int id,songs;
    String name;
    String time;

    public PlayList(int id, String name, int songs, String time) {
>>>>>>> Stashed changes
    this.id=id;
    this.name=name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
