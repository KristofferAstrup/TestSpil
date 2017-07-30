package Libraries;

import javafx.scene.image.Image;

/**
 * Created by kristoffer on 21-06-2017.
 */
public class EditorClass {

    private Class c;
    private Image image;
    private String name;

    public EditorClass(Class c,Image image)
    {
        this(c,image,c.getName());
    }

    public EditorClass(Class c,Image image,String name)
    {
        this.c = c;
        this.image = image;
        this.name = name;
    }

    public Class getClasss(){return c;}
    public Image getImage(){return image;}
    public String getName(){return name;}

}
