package World.ParticleSystem;

import World.World;
import Vectors.DynamicVector;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageParticleSystem extends ParticleSystem implements Serializable {

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

    public ArrayList<Particle> getParticles(){return particles;}

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
            if(particle.gravity) {
                particle.speed.setAdd(deltaForce);
            }
            particle.pos.setAdd(particle.speed.multiply(delta));
        }
        if(oldParticles.size() > 0)
        {
            particles.removeAll(oldParticles);
            oldParticles = new ArrayList<>();
        }
    }

    public void addParticle(DynamicVector pos,DynamicVector speed,Image img,boolean gravity)
    {
        particles.add(new Particle(pos,speed,img,gravity,defaultLifeTime));
    }

    public void addParticle(DynamicVector pos,DynamicVector speed,Image img,boolean gravity,double lifetime)
    {
        particles.add(new Particle(pos,speed,img,gravity,lifetime));
    }

    public class Particle
    {
        Image img;
        DynamicVector pos;
        DynamicVector speed;
        double lifetime;
        boolean gravity;
        public Particle(DynamicVector pos,DynamicVector speed,Image img,boolean gravity,double lifetime)
        {
            this.pos = pos;
            this.speed = speed;
            this.img = img;
            this.gravity = gravity;
            this.lifetime = lifetime;
        }
        public Image getImage(){return img;}
        public DynamicVector getPos(){return pos;}
        public DynamicVector getSpeed(){return speed;}
        public double getLifetime(){return lifetime;}
        @Override
        public String toString()
        {
            return "Pos: " + pos + ", speed: " + speed + ", lifetime: " + lifetime;
        }
    }

}
