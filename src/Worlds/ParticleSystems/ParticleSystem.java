package Worlds.ParticleSystems;

import Worlds.World;

/**
 * Created by kristoffer on 25-05-2017.
 */
public abstract class ParticleSystem {

    protected World world;

    public ParticleSystem(){
        //Empty ctor for reflection in serializing
    }

    public abstract void init();

    public abstract void reset();

    public ParticleSystem(World world)
    {
        this.world = world;
    }

    public void setWorld(World world)
    {
        this.world = world;
    }

    public abstract void update(double delta);

}
