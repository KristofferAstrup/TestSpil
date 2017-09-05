package Worlds.WorldObjects.Blocks;

import Libraries.ImageLibrary;
import Vectors.Vector;
import Worlds.World;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Kris on 15-02-2017.
 */
public class PlankwallBlock extends Block implements Serializable {

    private transient HashMap<String,Image> images;

    public PlankwallBlock(World world, Vector pos)
    {
        super(world,pos);
    }

    @Override
    public void init()
    {
        super.init();
        images = new HashMap<>();
        images.put("C",ImageLibrary.getImage("Plankwall_C.png"));
        images.put("E",ImageLibrary.getImage("Plankwall_E.png"));
        images.put("O",ImageLibrary.getImage("Plankwall_O.png"));
        images.put("S",ImageLibrary.getImage("Plankwall_S.png"));
        images.put("U",ImageLibrary.getImage("Plankwall_U.png"));
        images.put("Z",ImageLibrary.getImage("Plankwall_Z.png"));
    }

    @Override
    public void updateImage() {
        System.out.println(getORIsetRot(world.getBlockedOrientation(this,world.getBlockedDirs(getPos()))));
        switch(getORIsetRot(world.getBlockedOrientation(this,world.getBlockedDirs(getPos()))))
        {
            case Z:
                lastImage = images.get("Z");
                break;
            case U:
                lastImage = images.get("U");
                break;
            case Q:
                lastImage = images.get("E"); //Sat til E da der ikke er nogen Q
                break;
            case C:
                lastImage = images.get("C");
                break;
            case E:
                lastImage = images.get("E");
                break;
            case S:
                lastImage = images.get("S");
                break;
            case O:
                lastImage = images.get("O");
                break;
        }
    }

    @Override
    protected String getTypeName() {
        return "Plankwall";
    }

}
