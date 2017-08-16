package Worlds.WorldObjects.DynamicObjects;

import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Worlds.World;
import javafx.scene.image.Image;

public class Goal extends DynamicObject {

    public Goal(World world, DynamicVector pos) {
        super(world, pos);
        System.out.println("YY"+pos.getY_dyn());
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
