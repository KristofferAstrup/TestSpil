package World.WorldObject.DynamicObject;

import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import World.World;
import javafx.scene.image.Image;

public class Goal extends DynamicObject {

    public Goal(World world, DynamicVector pos) {
        super(world, pos);
    }

    @Override
    public Image getImage() {
        return ImageLibrary.getImage("Gate.png");
    }

    @Override
    public void init() {
        size = new DynamicVector(2,2);
        scale = new DynamicVector(2,2);
    }

    @Override
    public void update(double delta) {

    }
}
