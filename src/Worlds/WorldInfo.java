package Worlds;

/**
 * Created by kristoffer on 23-08-2017.
 */
public class WorldInfo {

    private String name;
    private String author;
    private String description;

    public WorldInfo(String name, String author, String description) {
        this.name = name;
        this.author = author;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
