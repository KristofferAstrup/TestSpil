package World.WorldObject.Block;

import Factories.ObjType;
import Factories.ObjTypeGroup;
import Libraries.ImageLibrary;
import Vectors.Vector;
import World.World;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;

public class BricksBlock extends Block implements Serializable {

    private transient HashMap<String,Image> images;
    static final public ObjType objType = new ObjType("BricksBlock", ImageLibrary.getImage("Bricks_O.png"), ObjTypeGroup.blocks);

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
}
