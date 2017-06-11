package World.Background;

import Controller.IUpdate;
import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import javafx.scene.image.Image;

import java.io.Serializable;

public class BackgroundElement implements Serializable {

    private transient Image image;
    private String imagePath;
    private DynamicVector pivot; //Pivot per object size
    private DynamicVector moveScale;
    private boolean repeatX;

    public BackgroundElement(String imagePath, DynamicVector pivot, DynamicVector moveScale, boolean repeatX)
    {
        this.imagePath = imagePath;
        this.pivot = pivot;
        this.moveScale = moveScale;
        this.repeatX = repeatX;
        init();
    }

    public void init()
    {
        image = ImageLibrary.getImage(imagePath);
    }

    public Image getImage(){return image;}

    public DynamicVector getPivot(){return pivot;}

    public DynamicVector getMoveScale(){return moveScale;}

    public boolean getRepeatX(){return repeatX;}

}
