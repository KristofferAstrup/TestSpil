package World.WorldObject.DynamicObject.PhysicObject.Mob;

import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Controller.Controller;
import World.World;
import State.GameState.GameState;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;

public class Bat extends Mob implements Serializable {

    private static HashMap<String, Image> imgs = new HashMap<String,Image>(){{
        put("sleep_0", ImageLibrary.getImage("bat_0.png"));
        put("fly_0", ImageLibrary.getImage("bat_1.png"));
        put("fly_1", ImageLibrary.getImage("bat_2.png"));
    }};
    private static double horSpeed = 2;
    private static double baskDelay = 0.15;
    private static double baskTime = 0.05;
    private static boolean wingsDown = false;
    private static double upwardSpeed = 5;

    private static DynamicVector targetVec;

    public Bat(World world, DynamicVector pos) {
        super(world, pos);
    }

    @Override
    public void init()
    {
        super.init();
        healthMax = 1;
        size = new DynamicVector(0.9,0.3);
    }

    @Override
    public Image getImage() {
        if(!awake || !getAlive()){return imgs.get("sleep_0");}
        return imgs.get("fly_" + (wingsDown?0:1));
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);
        if(!getAlive())return;

        Player player = world.getPlayerTarget(this);
        targetVec = player.getPos();
        setSpeed();

        if(targetVec.dist(pos) < size.getX_dyn()/2+player.getSize().getX_dyn()/2)
        {
            world.getPlayerTarget(this).damage(1);
        }

        if(GameState.time % (baskDelay+baskTime) < baskDelay)
        {
            wingsDown = false;
        }
        else if(pos.getY_dyn() < targetVec.getY_dyn())
        {
            wingsDown = true;
            speed.setY_dyn(upwardSpeed);
        }

        if(!flip && pos.getX_dyn() > targetVec.getX_dyn() + 0.4)
        {
            flip = true;
            setSpeed();
        }
        else if(flip && pos.getX_dyn() < targetVec.getX_dyn() - 0.4)
        {
            flip = false;
            setSpeed();
        }
    }

    @Override
    public void reset()
    {
        super.reset();
        flip = Controller.random.nextBoolean();
        awake = true;
        if(awake){setSpeed();}
    }

    private void setSpeed()
    {
        speed.setX_dyn(horSpeed*(getFlipped()?-1:1));
    }

}
