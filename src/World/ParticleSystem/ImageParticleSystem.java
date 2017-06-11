package World.ParticleSystem;

import World.World;
import Vectors.DynamicVector;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class ImageParticleSystem extends ParticleSystem {

    private double defaultLifeTime = 1;
    private ArrayList<Particle> particles;
    private DynamicVector force;

    private ArrayList<Particle> oldParticles;

    public ImageParticleSystem(World world,DynamicVector force)
    {
        super(world);
        this.force = force;
        init();
    }

    public void setForce(DynamicVector force){
        this.force = force;
    }

    public DynamicVector getForce(){
        return force;
    }

    @Override
    public void init()
    {
        particles = new ArrayList<>();
        oldParticles = new ArrayList<>();
    }

    @Override
    public void reset(){
        particles = new ArrayList<>();
        oldParticles = new ArrayList<>();
    }

    @Override
    public void update(double delta) {
        DynamicVector deltaForce = force.multiply(delta);
        for(Particle particle : particles)
        {
            particle.lifetime -= delta;
            if(particle.lifetime <= 0){
                oldParticles.add(particle);
                continue;
            }
            if(particle.gravity)
                particle.speed.setAdd(deltaForce);
        }
        if(oldParticles.size() > 0)
        {
            for(Particle particle : oldParticles)
            {
                particles.remove(particle);
            }
            oldParticles = new ArrayList<>();
        }
    }

    public void addParticle(Particle particle)
    {
        particles.add(particle);
    }

    public class Particle
    {
        Image img;
        DynamicVector pos;
        DynamicVector speed;
        double lifetime;
        boolean gravity;
        public Particle(DynamicVector pos,DynamicVector speed,Image img,boolean gravity)
        {
            this.pos = pos;
            this.img = img;
            this.gravity = gravity;
            this.lifetime = defaultLifeTime;
        }
        public Particle(DynamicVector pos,DynamicVector speed,Image img,boolean gravity,double lifetime)
        {
            this(pos,speed,img,gravity);
            this.lifetime = lifetime;
        }
    }

}