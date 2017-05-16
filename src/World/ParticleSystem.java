package World;

import Controller.Controller;
import Vectors.DynamicVector;
import com.sun.javafx.geom.ConcentricShapePair;

import java.util.ArrayList;
import java.util.Iterator;

public class ParticleSystem {

    ArrayList<DynamicVector> particles = new ArrayList<>();
    DynamicVector from;
    DynamicVector dist;
    double spawnDelay = 0;
    double spawnRate;
    DynamicVector speed;

    public ParticleSystem(DynamicVector from,DynamicVector to, int frequency,DynamicVector speed)
    {
        spawnRate = 1.0/frequency;
        this.speed = speed;
        this.from = from;
        dist = new DynamicVector(to.getX_dyn()-from.getX_dyn(),to.getY_dyn()-from.getY_dyn());
    }

    public void reset(){particles = new ArrayList<>();}

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
            vec.add(_speed);
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
