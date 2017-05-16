package World.WorldObject.Block;

import Factories.ObjType;
import Factories.ObjTypeGroup;
import Libraries.ImageLibrary;
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
    static final public ObjType objType = new ObjType("DirtBlock", ImageLibrary.getImage("Dirt_O.png"), ObjTypeGroup.blocks);

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
}
