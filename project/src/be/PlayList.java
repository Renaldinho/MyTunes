package be;

public class PlayList {
    int id;
    String name;
    public PlayList(int id, String name) {
    this.id=id;
    this.name=name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
