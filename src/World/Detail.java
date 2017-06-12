package World;

import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import javafx.scene.image.Image;

public class Detail {

    DynamicVector pos;
    Image image;
    String imagePath;
    boolean visible;

    public Detail(DynamicVector pos,String imagePath)
    {
        this.pos = pos;
        this.imagePath = imagePath;
        visible = true;
        init();
    }

    public void init()
    {
        image = ImageLibrary.getImage(imagePath);
    }

    public Image getImage(){return image;}

    public DynamicVector getPos(){return pos;}

    public boolean getVisible(){return visible;}

    public void setPos(DynamicVector pos){this.pos = pos;}

    public void setImage(Image image){this.image = image;}

    public void setVisible(boolean visible){this.visible = visible;}

}
