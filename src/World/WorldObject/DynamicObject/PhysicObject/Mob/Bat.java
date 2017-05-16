package World.WorldObject.DynamicObject.PhysicObject.Mob;

import Factories.ObjType;
import Factories.ObjTypeGroup;
import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Controller.Controller;
import World.World;
import State.GameState.GameState;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;

public class Bat extends Mob implements Serializable {

    public static final ObjType objType = new ObjType("Bat",ImageLibrary.getImage("bat_0.png"), ObjTypeGroup.mobs);

    private transient HashMap<String, Image> imgs;
    private static double horSpeed = 2;
    private static double baskDelay = 0.15;
    private static double baskTime = 0.05;
    private static boolean wingsDown = false;
    private static double upwardSpeed = 5;

    private static DynamicVector targetVec = new DynamicVector(7,15);

    public Bat(World world, DynamicVector pos) {
        super(world, pos);
    }

    @Override
    public void init()
    {
        super.init();
        healthMax = 1;
        size = new DynamicVector(0.9,0.6);
        imgs = new HashMap<String,Image>(){{
            put("sleep_0", ImageLibrary.getImage("bat_0.png"));
            put("fly_0", ImageLibrary.getImage("bat_1.png"));
            put("fly_1", ImageLibrary.getImage("bat_2.png"));
        }};
    }

    @Override
    public Image getImage() {
        if(!awake){return imgs.get("sleep_0");}
        return imgs.get("fly_" + (wingsDown?0:1));
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);

        targetVec = Controller.getWorld().getPlayerTarget(this).getPos();
        setSpeed();

        if(targetVec.dist(pos) < 1)
        {
            Controller.getWorld().getPlayerTarget(this).damage(1);
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
