package Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs;

import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Worlds.World;
import Worlds.Dir;
import States.GameStates.GameState;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Kris on 20-02-2017.
 */
public class Player extends Mob implements Serializable {

    private transient boolean braking = false;

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
                    return ImageLibrary.getImage("char_brake");
                }else {
                    return ImageLibrary.getImage("char_run_" + (int)((GameState.time*8)%4));
                }
            }else{
                return ImageLibrary.getImage("char_idle");
            }
        }
        else
        {
            if(blockedDirs.get(Dir.Left))
            {
                return ImageLibrary.getImage("char_wallslide");
            }
            else if(blockedDirs.get(Dir.Right)) {
                return ImageLibrary.getImage("char_wallslide");
            }
            else if(speed.getY_dyn() > 1)
            {
                return ImageLibrary.getImage("char_airborne_up");
            } else if(speed.getY_dyn() < -1)
            {
                return ImageLibrary.getImage("char_airborne_down");
            } else {
                return ImageLibrary.getImage("char_airborne_stale");
            }
        }
    }

    @Override
    public void reset()
    {
        super.reset();
    }

}
