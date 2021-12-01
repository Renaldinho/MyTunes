package be;

public class PlayList {

    private final int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public PlayList(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
