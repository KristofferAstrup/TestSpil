package World.ParticleSystem;

import Controller.Controller;
import Vectors.DynamicVector;
import World.World;
import com.sun.javafx.geom.ConcentricShapePair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class GlobalParticleSystem extends ParticleSystem implements Serializable {

    protected double spawnDelay = 0;
    protected double spawnRate;
    protected transient ArrayList<DynamicVector> particles = new ArrayList<>();
    private DynamicVector from;
    private DynamicVector dist;
    private DynamicVector speed;

    public GlobalParticleSystem(World world,DynamicVector from,DynamicVector to, int frequency,DynamicVector speed)
    {
        super(world);
        spawnRate = 1.0/frequency;
        this.speed = speed;
        this.from = from;
        dist = new DynamicVector(to.getX_dyn()-from.getX_dyn(),to.getY_dyn()-from.getY_dyn());
    }

    @Override
    public void init()
    {
        particles = new ArrayList<>();
    }

    @Override
    public void reset(){particles = new ArrayList<>();}

    @Override
    public void update(double delta)
    {
        if(spawnDelay <=0)
        {
            particles.add(new DynamicVector(Controller.random.nextDouble()*dist.getX_dyn()+from.getX_dyn(),Controller.random.nextDouble()*dist.getY_dyn()+from.getY_dyn()));
            spawnDelay += spawnRate;
        }
        else
        {
            spawnDelay -= delta;
        }
        DynamicVector _speed = new DynamicVector(speed.getX_dyn()*delta,speed.getY_dyn()*delta);

        Iterator<DynamicVector> iterator = particles.iterator();
        while(iterator.hasNext())
        {
            DynamicVector vec = iterator.next();
            vec.setAdd(_speed);
            if(vec.getX_dyn() < 0)
            {
                vec.setX(1);
            }
            if(vec.getY_dyn() < 0)
            {
                iterator.remove();
            }
        }
    }

    public ArrayList<DynamicVector> getParticles(){return particles;}

    public DynamicVector getSpeed(){return speed;}

}
