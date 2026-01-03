package pt.ul.fc.css.weatherwise.entities;

public class Author {
    private int id;
    private String name;
    
    public Author(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Author(){}
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId (int id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
