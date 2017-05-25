package World.WorldObject.DynamicObject.PhysicObject.Projectiles;

import Vectors.DynamicVector;
import World.*;
import World.WorldObject.DynamicObject.PhysicObject.PhysicObject;

import java.io.Serializable;

public abstract class Projectile extends PhysicObject implements Serializable
{

    protected float angle = 0;

    public Projectile(World world, DynamicVector pos, float angle) {
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

        if(world.outsideBoundary(pos)){destroy();}
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
