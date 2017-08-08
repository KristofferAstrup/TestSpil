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
        System.out.println("YO: " + this.getPos());
    }

    @Override
    public void updateImage() {
        //lastImage = ImageLibrary.getImage("grass_up_center0");
        DecorationFace decorationFace = getFace();
        lastImage = ImageLibrary.getImage("cloud2");
        System.out.println(decorationFace);
        switch(decorationFace.face) //HAR KUN "UP" SPRITES! :¨(
        {
            case Up:
                lastImage = ImageLibrary.getImage("grass_up_center" + Controller.random.nextInt(4));
                flipped = false;
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
                lastImage = ImageLibrary.getImage("cloud0"); //MANGLER VERTICAL OG HORIZONTAL SPRITES!!
                break;
            case Vertical:
                lastImage = ImageLibrary.getImage("cloud1");
                break;
        }
    }

}
