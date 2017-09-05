package Worlds.WorldObjects.DynamicObjects.PhysicObjects.Projectiles;

import Controllers.Controller;
import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Vectors.Vector;
import Worlds.Dir;
import Worlds.World;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Mob;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Player;
import javafx.scene.image.Image;

import java.util.HashSet;

/**
 * Created by kristoffer on 04-09-2017.
 */
public class Fist extends Projectile {

    private double launchSpeed;
    private static final double speedLoss = 9.82*2;

    private HashSet<Mob> mobsHitted = new HashSet<>();

    public Fist(World world, DynamicVector pos, double angle, double speed) {
        super(world, pos, angle);
        this.launchSpeed = speed;
    }

    @Override
    public void init()
    {
        super.init();
        size = new DynamicVector(0.75,0.75);
        gravityEnabled = false;
    }

    @Override
    public Image getImage() {
        return ImageLibrary.getImage("Fist");
    }

    @Override
    protected double getBaseSpeed() {
        return launchSpeed;
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);

        rot = 90+DynamicVector.angle(getPos(),getPos().add(speed));
        speed = speed.subtract(speed.normalize().multiply(delta*speedLoss));

        if(speed.length() < 0.25){
            world.destroyWorldObject(this);
        }

        DynamicVector particleTranslation = Vector.angleToVector(90-rot).multiply(0.5);
        particleTranslation.setAdd(particleTranslation.normal().multiply((Controller.random.nextDouble()*2-1)*0.5));
        world.getImageParticleSystem().addParticle(getPos().subtract(particleTranslation),speed.multiply(-0.15),0,ImageLibrary.getImage("RedDot"),false,0.25);

        Mob closestMob = world.getClosestMob(getPos(),0.25);
        if(closestMob != null && !(closestMob instanceof Player) && !mobsHitted.contains(closestMob)){
            closestMob.damage(2);
            closestMob.getSpeed().setAdd(speed.multiply(0.5).add(0.0,1));
            mobsHitted.add(closestMob);
        }
    }

    @Override
    public void uponDestroyed()
    {
        for(int i=0;i<100;i++)
        {
            double particleRot = Controller.random.nextDouble()*360;
            DynamicVector particleTranslation = Vector.angleToVector(particleRot).multiply(Controller.random.nextDouble()*0.5);
            world.getImageParticleSystem().addParticle(getPos().add(particleTranslation),speed.multiply(0.15).add(particleTranslation.normalize().multiply(4.0)),0,ImageLibrary.getImage("RedDot"),true,0.25);
        }
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
        for(int i=0;i<25;i++)
        {
            double particleRot = Controller.random.nextDouble()*360;
            DynamicVector particleTranslation = Vector.angleToVector(particleRot).multiply(Controller.random.nextDouble()*0.5);
            world.getImageParticleSystem().addParticle(getPos().add(particleTranslation),speed.multiply(0.15).add(particleTranslation.normalize().multiply(4.0)),0,ImageLibrary.getImage("RedDot"),true,0.25);
        }
    }



}
