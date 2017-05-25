package World;

import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * Created by kristoffer on 25-05-2017.
 */
public class ColorWrapper implements Serializable {

    private transient Color color;
    private double red;
    private double green;
    private double blue;
    private double alpha;

    public ColorWrapper(double red,double green,double blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 1;
        init();
    }

    public ColorWrapper(double red,double green,double blue,double alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        init();
    }

    public void init() {
        color = new Color(red,green,blue,alpha);
    }

    public Color getColor()
    {
        return color;
    }

}
