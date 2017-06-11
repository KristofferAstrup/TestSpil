package World;

import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import javafx.scene.image.Image;

public class Detail {

    DynamicVector pos;
    Image image;
    String imagePath;

    public Detail(DynamicVector pos,String imagePath)
    {
        this.pos = pos;
        this.imagePath = imagePath;
        init();
    }

    public void init()
    {
        image = ImageLibrary.getImage(imagePath);
    }

    public Image getImage(){return image;}

    public DynamicVector getPos(){return pos;}

}
