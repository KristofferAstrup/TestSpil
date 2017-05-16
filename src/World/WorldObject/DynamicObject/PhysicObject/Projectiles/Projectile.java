package World.WorldObject.DynamicObject.PhysicObject.Projectiles;

import Vectors.DynamicVector;
import World.*;
import World.WorldObject.DynamicObject.PhysicObject.PhysicObject;

public abstract class Projectile extends PhysicObject
{

    protected float angle;

    public Projectile(World world, DynamicVector pos, float angle) {
        super(world, pos);
        this.angle = angle;
    }

    protected abstract double getBaseSpeed();

    @Override
    public void update(double delta) {

        blockCollisionCheck(delta);

        pos.setX_dyn(pos.getX_dyn()+speed.getX_dyn()*delta);
        pos.setY_dyn(pos.getY_dyn()+speed.getY_dyn()*delta);
    }

    @Override
    public void init()
    {
        speed = new DynamicVector(Math.cos(angle)*getBaseSpeed(),Math.sin(angle)*getBaseSpeed());
    }

}
