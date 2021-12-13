package be;

public class Category {
    int id ;
    String name ;
    public Category(int id, String name) {
    this.id=id;
    this.name=name;
    }

    public String getCategoryName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
