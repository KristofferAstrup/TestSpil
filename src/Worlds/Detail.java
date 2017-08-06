package Worlds;

import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import javafx.scene.image.Image;

import java.io.Serializable;

public class Detail implements Serializable {

    DynamicVector pos;
    transient Image image;
    String imagePath;
    boolean visible;

    public Detail(DynamicVector pos,String imagePath)
    {
        this.pos = pos;
        this.imagePath = imagePath;
        visible = true;
        init();
    }

    public Detail(DynamicVector pos,Image image)
    {
        this.pos = pos;
        this.image = image;
        visible = true;
    }

    public Detail(DynamicVector pos)
    {
        this.pos = pos;
        visible = false;
    }

    public void init()
    {
        if(imagePath != null)
        image = ImageLibrary.getImage(imagePath);
    }

    public Image getImage(){return image;}

    public DynamicVector getPos(){return pos;}

    public boolean getVisible(){return visible;}

    public void setPos(DynamicVector pos){this.pos = pos;}

    public void setImage(Image image){this.image = image;}

    public void setVisible(boolean visible){this.visible = visible;}

}
