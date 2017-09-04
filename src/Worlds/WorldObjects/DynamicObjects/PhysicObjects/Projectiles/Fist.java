package Worlds.WorldObjects.DynamicObjects.PhysicObjects.Projectiles;

import Controllers.Controller;
import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Worlds.Dir;
import Worlds.World;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Mob;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Player;
import javafx.scene.image.Image;

/**
 * Created by kristoffer on 04-09-2017.
 */
public class Fist extends Projectile {

    private double launchSpeed;
    private double speedLoss = 9.82*2;

    public Fist(World world, DynamicVector pos, double angle, double speed) {
        super(world, pos, angle);
        this.launchSpeed = speed;
    }

    @Override
    public void init()
    {
        super.init();
        size = new DynamicVector(0.50,0.50);
        gravityEnabled = false;
    }

    @Override
    public Image getImage() {
        return ImageLibrary.getImage("Dagger.png");
    }

    @Override
    protected double getBaseSpeed() {
        return launchSpeed;
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);

        speed = speed.subtract(speed.normalize().multiply(delta*speedLoss));

        if(speed.length() < 0.25){
            world.destroyWorldObject(this);
        }

        /*Mob closestMob = world.getClosestMob(getPos(),0.25);
        if(closestMob != null && !(closestMob instanceof Player)){
            closestMob.damage(1);

            Image img = ImageLibrary.getImage("Dirt_O.png");
            int quantity = Controller.random.nextInt(3)+5;


            destroy();
        }*/
    }

    @Override
    protected void collisionResponse(Dir dir,Double delta)
    {
        DynamicVector prevSpeed = new DynamicVector(speed);
        super.collisionResponse(dir,delta);
        if(blockedDirs.get(Dir.Down)){
            speed.set(prevSpeed.getX_dyn(),-prevSpeed.getY_dyn());
            pos.add(0,speed.getY_dyn()*delta);
        }
        else if(blockedDirs.get(Dir.Up)){
            speed.set(prevSpeed.getX_dyn(),-prevSpeed.getY_dyn());
            pos.add(0,speed.getY_dyn()*delta);
        }
        if(blockedDirs.get(Dir.Left)){
            speed.set(-prevSpeed.getX_dyn(),prevSpeed.getY_dyn());
            pos.add(speed.getX_dyn()*delta,0);
        }
        else if(blockedDirs.get(Dir.Right)){
            speed.set(-prevSpeed.getX_dyn(),prevSpeed.getY_dyn());
            pos.add(speed.getX_dyn()*delta,0);
        }
    }

}
