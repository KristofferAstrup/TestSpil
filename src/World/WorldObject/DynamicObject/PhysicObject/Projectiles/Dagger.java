package World.WorldObject.DynamicObject.PhysicObject.Projectiles;

import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import World.*;
import javafx.scene.image.Image;

public class Dagger extends Projectile {

    private static double baseSpeed = 3;

    public Dagger(World world, DynamicVector pos, float angle) {
        super(world, pos, angle);

    }

    @Override
    public void init()
    {
        super.init();
        size = new DynamicVector(0.25,0.25);
    }

    @Override
    protected double getBaseSpeed() {
        return baseSpeed;
    }

    @Override
    public Image getImage() {
        return ImageLibrary.getImage("pig_2.png");
    }

}
