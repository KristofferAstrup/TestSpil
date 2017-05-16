package World.Background;

import Vectors.DynamicVector;
import javafx.scene.image.Image;

public class BackgroundElement {

    private Image image;
    private DynamicVector pivot; //Pivot per object size
    private DynamicVector moveScale;
    private boolean repeatX;

    public BackgroundElement(Image image, DynamicVector pivot, DynamicVector moveScale, boolean repeatX)
    {
        this.image = image;
        this.pivot = pivot;
        this.moveScale = moveScale;
        this.repeatX = repeatX;
    }

    public Image getImage(){return image;}

    public DynamicVector getPivot(){return pivot;}

    public DynamicVector getMoveScale(){return moveScale;}

    public boolean getRepeatX(){return repeatX;}

}
