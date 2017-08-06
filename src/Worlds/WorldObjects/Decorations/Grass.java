package Worlds.WorldObjects.Decorations;

import Controllers.Controller;
import Libraries.ImageLibrary;
import Vectors.Vector;
import Worlds.World;
import javafx.scene.image.Image;

import java.util.HashMap;

public class Grass extends Decoration {

    public Grass(World world, Vector pos) {
        super(world, pos);
    }

    @Override
    public void updateImage() {
        lastImage = ImageLibrary.getImage("grass_up_center0");
        /*DecorationFace decorationFace = getFace();
        switch(decorationFace.face) //HAR KUN "UP" SPRITES! :¨(
        {
            case Up:
                lastImage = ImageLibrary.getImage("grass_up_center" + Controller.random.nextInt(4));
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
        }*/
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
