package World.WorldObject.Block;

import Controller.Controller;
import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Vectors.Vector;
import World.World;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Kris on 15-02-2017.
 */
public class DirtBlock extends Block implements Serializable {

    private transient HashMap<ORI,Image> images;

    public DirtBlock(World world, Vector pos)
    {
        super(world,pos);
    }

    @Override
    public void init()
    {
        super.init();
        images = new HashMap<>();
        for (ORI ori : ORI.values()) {
            images.put(ori, ImageLibrary.getImage(getTypeName() + "_" + ori.toString() + ".png"));
        }
    }

    @Override
    public void updateImage() {
        lastImage = images.get(getORIsetRot(world.getBlockedOrientation(this,world.getBlockedDirs(getPos()))));
    }

    @Override
    protected String getTypeName() {
        return "Dirt";
    }

    @Override
    public void die()
    {
        Image img = ImageLibrary.getImage("DirtChunk_0.png");
        int quantity = Controller.random.nextInt(3)+5;
        for(int i=0;i<quantity;i++) {
            world.getImageParticleSystem().addParticle(new DynamicVector(getPos()), new DynamicVector(Controller.random.nextDouble()*6-3, Controller.random.nextDouble()*6-3), img, true, 0.5);
        }
    }
}
