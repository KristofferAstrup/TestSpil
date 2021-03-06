package Worlds.WorldObjects.DynamicObjects.PhysicObjects.Projectiles;

import Controllers.Controller;
import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Worlds.*;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Mob;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Player;
import javafx.scene.image.Image;

import java.io.Serializable;

public class Dagger extends Projectile implements Serializable {

    private static double baseSpeed = 10;

    public Dagger(World world, DynamicVector pos, double angle) {
        super(world, pos, angle);

    }

    @Override
    public void init()
    {
        super.init();
        size = new DynamicVector(0.80,0.20);
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);
        if(blockedDirs.containsValue(true)){
            //destroy();
            rot = 0;
            gravityEnabled = true;
        }
        else
        {
            rot += 360*4*delta;
        }
        Mob closestMob = world.getClosestMob(getPos(),0.25);
        if(closestMob != null && !(closestMob instanceof Player)){
            closestMob.damage(1);

            Image img = ImageLibrary.getImage("Dirt_O.png");
            int quantity = Controller.random.nextInt(3)+5;


            destroy();
        }
    }

    @Override
    protected double getBaseSpeed() {
        return baseSpeed;
    }

    @Override
    public void reset()
    {
        super.reset();
        gravityEnabled = false;
    }

    @Override
    public Image getImage() {
        return ImageLibrary.getImage("Dagger.png");
    }

}
