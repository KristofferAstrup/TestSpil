package World.WorldObject.DynamicObject.PhysicObject.Mob;

import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import World.World;
import World.Dir;
import State.GameState.GameState;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Kris on 20-02-2017.
 */
public class Player extends Mob implements Serializable {

    private transient boolean braking = false;
    private static HashMap<String, Image> imgs = new HashMap<String,Image>(){{
        put("run_0", ImageLibrary.getImage("char_run_0.png"));
        put("run_1", ImageLibrary.getImage("char_run_1.png"));
        put("run_2", ImageLibrary.getImage("char_run_2.png"));
        put("run_3", ImageLibrary.getImage("char_run_3.png"));
        put("airborne_down", ImageLibrary.getImage("char_airborne_down.png"));
        put("airborne_stale", ImageLibrary.getImage("char_airborne_stale.png"));
        put("airborne_up", ImageLibrary.getImage("char_airborne_up.png"));
        put("brake", ImageLibrary.getImage("char_brake.png"));
        put("idle", ImageLibrary.getImage("char_idle.png"));
        put("wallslide", ImageLibrary.getImage("char_wallslide.png"));
    }};

    public Player(World world, DynamicVector _pos) {
        super(world,_pos);
    }

    @Override
    public void init()
    {
        super.init();
        healthMax = 1;
        size = new DynamicVector(0.65,0.95);
    }

    public void setBraking(boolean braking)
    {
        this.braking = braking;
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);
    }

    @Override
    public Image getImage() {
        if(!getAlive()){return null;}

        if(blockedDirs.get(Dir.Down))
        {
            if(speed.getX_dyn() != 0) {
                if(braking) {
                    return imgs.get("brake");
                }else {
                    return imgs.get("run_" + (int)((GameState.time*8)%4));
                }
            }else{
                return imgs.get("idle");
            }
        }
        else
        {
            if(blockedDirs.get(Dir.Left))
            {
                return imgs.get("wallslide");
            }
            else if(blockedDirs.get(Dir.Right)) {
                return imgs.get("wallslide");
            }
            else if(speed.getY_dyn() > 1)
            {
                return imgs.get("airborne_up");
            } else if(speed.getY_dyn() < -1)
            {
                return imgs.get("airborne_down");
            } else {
                return imgs.get("airborne_stale");
            }
        }
    }

    @Override
    public void reset()
    {
        super.reset();
    }

}
