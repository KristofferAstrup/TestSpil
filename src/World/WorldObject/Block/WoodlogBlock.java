package World.WorldObject.Block;

import Libraries.ImageLibrary;
import Vectors.Vector;
import World.World;

public class WoodlogBlock extends Block {

    public WoodlogBlock(World world, Vector pos) {
        super(world, pos);
    }

    @Override
    protected String getTypeName() {
        return "WoodlogBlock";
    }

    @Override
    public void updateImage() {
        lastImage = ImageLibrary.getImage("Woodlog.png");
    }
}
