package World.ParticleSystem;

import Vectors.DynamicVector;
import World.World;

import java.util.ArrayList;

/**
 * Created by kristoffer on 25-05-2017.
 */
public abstract class ParticleSystem {

    protected transient ArrayList<DynamicVector> particles = new ArrayList<>();
    protected double spawnDelay = 0;
    protected double spawnRate;
    protected World world;

    public ParticleSystem(){

    }

    public ParticleSystem(World world)
    {
        this.world = world;
    }

    public void init()
    {
        particles = new ArrayList<>();
    }

    public void reset(){particles = new ArrayList<>();}

    public void setWorld(World world)
    {
        this.world = world;
    }

    public abstract void update(double delta);

}
