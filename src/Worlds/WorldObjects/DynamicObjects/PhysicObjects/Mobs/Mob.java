package Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs;

import Vectors.DynamicVector;
import Worlds.*;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.PhysicObject;

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
        if(health <= 0 && alive){
            die();
        }
    }

    @Override
    public void update(double delta){
        super.update(delta);
        if(!getAlive()) {
            if (blockedDirs.get(Dir.Down) && speed.getX_dyn() != 0){
                speed.setX_dyn(speed.getX_dyn() * 0.9);
                if(speed.getX_dyn() > -0.05 && speed.getX_dyn() < 0.05){
                    speed.setX_dyn(0);
                }
            }
        }
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
