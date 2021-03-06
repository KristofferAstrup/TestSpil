package Worlds.WorldObjects.DynamicObjects.PhysicObjects.Projectiles;

import Vectors.DynamicVector;
import Worlds.*;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.PhysicObject;

import java.io.Serializable;

public abstract class Projectile extends PhysicObject implements Serializable
{

    protected double angle = 0;

    public Projectile(World world, DynamicVector pos, double angle) {
        super(world, pos);
        this.angle = angle;
    }

    protected abstract double getBaseSpeed();

    @Override
    public void update(double delta) {
        super.update(delta);

        blockCollisionCheck(delta);

        pos.setX_dyn(pos.getX_dyn()+speed.getX_dyn()*delta);
        pos.setY_dyn(pos.getY_dyn()+speed.getY_dyn()*delta);

        if(world.outsideBlockBoundary(pos)){destroy();}
    }

    @Override
    public void init()
    {
        super.init();
    }

    @Override
    public void reset()
    {
        super.reset();
        speed = new DynamicVector(Math.cos(Math.toRadians(angle))*getBaseSpeed(),Math.sin(Math.toRadians(angle))*getBaseSpeed());
    }

}
