package World.WorldObject.DynamicObject.PhysicObject.Mob;

import Vectors.DynamicVector;
import World.*;
import World.WorldObject.DynamicObject.PhysicObject.PhysicObject;

import java.io.Serializable;

public abstract class Mob extends PhysicObject implements Serializable {

    private int health;
    private boolean alive;
    protected int healthMax;
    protected boolean awake;

    public Mob(World world, DynamicVector pos) {
        super(world, pos);
    }

    @Override
    public void init()
    {
        super.init();
        alive = true;
    }

    public void damage(int dmg)
    {
        health -= dmg;
        if(health <= 0 && !alive){
            die();
        }
    }

    @Override
    public void update(double delta){
        super.update(delta);
        //if(blockedDirs.get(Dir.Down)){
    }

    public void heal(int heal)
    {
        health = Math.min(health + heal,healthMax);
    }

    public void die()
    {
        alive = false;
    }

    public int getHeatlh(){
        return health;
    }

    public boolean getAlive(){
        return alive;
    }

    @Override
    public void reset()
    {
        super.reset();
        health = healthMax;
        alive = true;
    }

}
