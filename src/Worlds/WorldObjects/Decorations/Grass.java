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
        DecorationFace decorationFace = world.getFace(getPos().getDynamicVector());
        lastFace = decorationFace;
        switch(decorationFace.face)
        {
            case Up:
                switch(decorationFace.position)
                {
                    case Solo:
                        lastImage = ImageLibrary.getImage("grass_up_solo");
                        break;
                    case Left:
                        lastImage = ImageLibrary.getImage("grass_up_left");
                        flipped = false;
                        break;
                    case Right:
                        lastImage = ImageLibrary.getImage("grass_up_left");
                        flipped = true;
                        break;
                    case Center:
                        lastImage = ImageLibrary.getImage("grass_up_center" + Controller.random.nextInt(4));
                        flipped = false;
                        break;
                }
                break;
            case Down:
                lastImage = ImageLibrary.getImage("grass_down");
                flipped = false;
                break;
            case Left:
                lastImage = ImageLibrary.getImage("grass_left");
                flipped = false;
                break;
            case Right:
                lastImage = ImageLibrary.getImage("grass_left");
                flipped = true;
                break;
            case Horizontal:
                lastImage = ImageLibrary.getImage("grass_horizontal");
                flipped = false;
                break;
            case Vertical:
                lastImage = ImageLibrary.getImage("grass_vertical");
                flipped = false;
                break;
        }
    }

}
