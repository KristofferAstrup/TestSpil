package World.WorldObject.Block;

import Libraries.ImageLibrary;
import Vectors.Vector;
import World.World;
import javafx.scene.image.Image;
import Controller.Controller;

import java.util.HashMap;

public class StoneBlock extends Block {

    private transient HashMap<String,Image> images;

    public StoneBlock(World world, Vector pos) {
        super(world, pos);
    }

    @Override
    public void init()
    {
        super.init();
        images = new HashMap<>();
        images.put("0", ImageLibrary.getImage("Stone0.png"));
        images.put("1",ImageLibrary.getImage("Stone1.png"));
        images.put("2",ImageLibrary.getImage("Stone2.png"));
        images.put("3",ImageLibrary.getImage("Stone3.png"));
        images.put("4",ImageLibrary.getImage("Stone4.png"));
        images.put("5",ImageLibrary.getImage("Stone5.png"));
    }

    @Override
    protected String getTypeName() {
        return "StoneBlock";
    }

    @Override
    public void updateImage() {
        int index = Controller.random.nextInt(10)==0 ? Controller.random.nextInt(2)+4 : Controller.random.nextInt(4);
        lastImage = images.get(String.valueOf(index));
    }
}
