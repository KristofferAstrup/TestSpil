package World.WorldObject.DynamicObject.PhysicObject.Mob;

import Factories.ObjType;
import Factories.ObjTypeGroup;
import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import World.World;
import World.Dir;
import State.GameState.GameState;
import Controller.Controller;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Kris on 01-03-2017.
 */
public class Pig extends Mob implements Serializable {

    public static final ObjType objType = new ObjType("Pig",ImageLibrary.getImage("pig_0.png"), ObjTypeGroup.mobs);

    private transient HashMap<String, Image> imgs;
    private static double moveSpeed = 0.75;

    public Pig(World world, DynamicVector pos) {
        super(world, pos);
    }

    @Override
    public void init()
    {
        super.init();
        healthMax = 1;
        size = new DynamicVector(0.9,0.6);
        imgs = new HashMap<String,Image>(){{
            put("sleep_0", ImageLibrary.getImage("pig_2.png"));
            put("sleep_1", ImageLibrary.getImage("pig_3.png"));
            put("walk_0", ImageLibrary.getImage("pig_0.png"));
            put("walk_1", ImageLibrary.getImage("pig_1.png"));
            put("dead_0", ImageLibrary.getImage("pig_4"));
        }};
    }

    @Override
    public Image getImage() {
        if(!getAlive()){return imgs.get("dead_0");}
        else if(!awake){return imgs.get("sleep_" + ((int)((GameState.time*2)%2)));}
        else {return imgs.get("walk_" + ((int)((GameState.time*3)%2)));}
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);
        if(blockedDirs.get(Dir.Right))
        {
            flip = true;
            setSpeed();
        }
        else if(blockedDirs.get(Dir.Left))
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
        awake = Controller.random.nextBoolean();
        if(awake){setSpeed();}
    }

    private void setSpeed()
    {
        speed.setX_dyn(moveSpeed*(getFlipped()?-1:1));
    }

}
