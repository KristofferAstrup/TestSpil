package Worlds.WorldObjects.Blocks;

import Controllers.Controller;
import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Vectors.Vector;
import Worlds.World;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;

public class BricksBlock extends Block implements Serializable {

    private transient HashMap<String,Image> images;

    public BricksBlock(World world, Vector pos)
    {
        super(world,pos);
    }

    @Override
    public void init()
    {
        super.init();
        images = new HashMap<>();
        images.put("Center",ImageLibrary.getImage("Bricks_O.png"));
    }

    @Override
    public void updateImage() {
        lastImage = images.get("Center");
        //BlockedOrientation ori = world.getBlockedOrientation(this,world.getBlockedDirs(getPos()));
        //if(ori.blocks == 4)
    }

    @Override
    protected String getTypeName() {
        return "Bricks";
    }

    @Override
    public void uponDestroyed()
    {
        int quantity = Controller.random.nextInt(3)+5;
        for(int i=0;i<quantity;i++) {
            Image img = ImageLibrary.getImage("BrickChunk_" + Controller.random.nextInt(2) + ".png");
            world.getImageParticleSystem().addParticle(new DynamicVector(getPos()), new DynamicVector(Controller.random.nextDouble()*6-3, Controller.random.nextDouble()*6-3),Controller.random.nextInt(120)-60, img, true, 0.5);
        }
    }

}
