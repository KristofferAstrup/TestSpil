package World.WorldObject.DynamicObject.PhysicObject.Projectiles;

import Vectors.DynamicVector;
import World.*;
import javafx.scene.image.Image;

public class Dagger extends Projectile {

    private static double baseSpeed = 3;

    public Dagger(World world, DynamicVector pos, float angle) {
        super(world, pos, angle);
    }

    @Override
    protected double getBaseSpeed() {
        return 0;
    }

    @Override
    public Image getImage() {
        return null;
    }

}
