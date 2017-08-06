package World.WorldObject.Decoration;

import Controller.Controller;
import Libraries.ImageLibrary;
import Vectors.Vector;
import World.World;
import javafx.scene.image.Image;

import java.util.HashMap;

public class Grass extends Decoration {

    private transient HashMap<String,Image> images;

    public Grass(World world, Vector pos) {
        super(world, pos);
    }

    @Override
    public void init()
    {
        super.init();
        images.put("grass_up_center0", ImageLibrary.getImage("grass0.png"));
        images.put("grass_up_center1", ImageLibrary.getImage("grass1.png"));
        images.put("grass_up_center2", ImageLibrary.getImage("grass2.png"));
        images.put("grass_up_center3", ImageLibrary.getImage("grass3.png"));
    }

    @Override
    public void updateImage() {
        DecorationFace decorationFace = getFace();
        switch(decorationFace.face) //HAR KUN "UP" SPRITES! :¨(
        {
            case Up:
                lastImage = images.get("grass_up_center" + Controller.random.nextInt(4));
                break;
            case Down:
                break;
            case Left:
                break;
            case Right:
                break;
            case Horizontal:
                break;
            case Vertical:
                break;
        }
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public Vector getPos() {
        return null;
    }
}
