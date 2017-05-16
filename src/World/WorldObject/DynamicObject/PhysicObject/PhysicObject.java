package World.WorldObject.DynamicObject.PhysicObject;

import Vectors.DynamicVector;
import World.*;
import World.WorldObject.DynamicObject.DynamicObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Kris on 20-02-2017.
 */
public abstract class PhysicObject extends DynamicObject implements Serializable {

    protected transient DynamicVector speed;
    protected transient DynamicVector size;
    protected transient HashMap<Dir,Boolean> blockedDirs;

    public PhysicObject(World world, DynamicVector pos) {
        super(world, pos);
    }

    private static final double hotfixConstant = 0.05;
    private static final double blockedFixConstant = 0.001;

    @Override
    public void init()
    {
        System.out.println("Init");
        speed = new DynamicVector(0,0);
        blockedDirs = new HashMap<Dir,Boolean>() {{put(Dir.Right,false); put(Dir.Down,false); put(Dir.Left,false); put(Dir.Up,false);}};
    }

    @Override
    public void update(double delta)
    {
        blockCollisionCheck(delta);

        if(!blockedDirs.get(Dir.Down))
        {
            speed.setY_dyn(speed.getY_dyn()-world.getGravity()*delta);
        }
        pos.setX_dyn(pos.getX_dyn()+speed.getX_dyn()*delta);
        pos.setY_dyn(pos.getY_dyn()+speed.getY_dyn()*delta);

    }

    public DynamicVector getSpeed()
    {
        return speed;
    }

    public HashMap<Dir,Boolean> getBlockedDirs()
    {
        return blockedDirs;
    }

    protected void blockCollisionCheck(double delta)
    {
        //DOWN
        if(!world.checkIfEmpty((int)Math.round(getPos().getX_dyn()),(int)Math.round(getPos().getY_dyn()-blockedFixConstant-size.getY_dyn()/2.0+speed.getY_dyn()*delta))
                || !world.checkIfEmpty((int)Math.round(getPos().getX_dyn()-size.getX_dyn()/2+hotfixConstant),(int)Math.round(getPos().getY_dyn()+speed.getY_dyn()*delta-0.1-size.getY_dyn()/2.0))
                || !world.checkIfEmpty((int)Math.round(getPos().getX_dyn()+size.getX_dyn()/2-hotfixConstant),(int)Math.round(getPos().getY_dyn()+speed.getY_dyn()*delta-0.1-size.getY_dyn()/2.0)))//!world.checkIfEmpty(getPos().getX(),getPos().getY()-1) && getPos().getY_dyn()+speed.getY_dyn()*delta <= getPos().getY())
        {
            blockedDirs.put(Dir.Down,true);
            getPos().setY_dyn(getPos().getY()-0.5+size.getY_dyn()/2.0);
            speed.setY(0);
        }
        else
        {
            blockedDirs.put(Dir.Down,false);
        }

        //UP
        if(!world.checkIfEmpty((int)Math.round(getPos().getX_dyn()),(int)Math.round(getPos().getY_dyn()+speed.getY_dyn()*delta+size.getY_dyn()/2.0))
                || !world.checkIfEmpty((int)Math.round(getPos().getX_dyn()-size.getX_dyn()/2+hotfixConstant),(int)Math.round(getPos().getY_dyn()+speed.getY_dyn()*delta+size.getY_dyn()/2.0))
                || !world.checkIfEmpty((int)Math.round(getPos().getX_dyn()+size.getX_dyn()/2-hotfixConstant),(int)Math.round(getPos().getY_dyn()+speed.getY_dyn()*delta+size.getY_dyn()/2.0)))//!world.checkIfEmpty(getPos().getX(),getPos().getY()-1) && getPos().getY_dyn()+speed.getY_dyn()*delta <= getPos().getY())
        {
            blockedDirs.put(Dir.Up,true);
            getPos().setY_dyn(getPos().getY()+0.5-size.getY_dyn()/2.0);
            speed.setY_dyn(-0.1);
        }
        else
        {
            blockedDirs.put(Dir.Up,false);
        }

        //RIGHT
        if(!world.checkIfEmpty((int)Math.round(getPos().getX_dyn()+blockedFixConstant+speed.getX_dyn()*delta+size.getX_dyn()/2.0),getPos().getY())
                || !world.checkIfEmpty((int)Math.round(getPos().getX_dyn()+speed.getX_dyn()*delta+size.getX_dyn()/2.0),(int)Math.round(getPos().getY_dyn()-size.getY_dyn()/2+hotfixConstant))//+speed.getY_dyn()*delta)-1)
                || !world.checkIfEmpty((int)Math.round(getPos().getX_dyn()+speed.getX_dyn()*delta+size.getX_dyn()/2.0),(int)Math.round(getPos().getY_dyn()+size.getY_dyn()/2-hotfixConstant)))//!world.checkIfEmpty(getPos().getX(),getPos().getY()-1) && getPos().getY_dyn()+speed.getY_dyn()*delta <= getPos().getY())
        {
            blockedDirs.put(Dir.Right,true);
            getPos().setX_dyn(getPos().getX()+0.5-size.getX_dyn()/2.0);
            speed.setX_dyn(0);
        }
        else
        {
            blockedDirs.put(Dir.Right,false);
        }

        //LEFT
        if(!world.checkIfEmpty((int)Math.round(getPos().getX_dyn()-blockedFixConstant+speed.getX_dyn()*delta-size.getX_dyn()/2.0),getPos().getY())
                || !world.checkIfEmpty((int)Math.round(getPos().getX_dyn()+speed.getX_dyn()*delta-size.getX_dyn()/2.0),(int)Math.round(getPos().getY_dyn()-size.getY_dyn()/2+hotfixConstant))//+speed.getY_dyn()*delta)-1)
                || !world.checkIfEmpty((int)Math.round(getPos().getX_dyn()+speed.getX_dyn()*delta-size.getX_dyn()/2.0),(int)Math.round(getPos().getY_dyn()+size.getY_dyn()/2-hotfixConstant)))//!world.checkIfEmpty(getPos().getX(),getPos().getY()-1) && getPos().getY_dyn()+speed.getY_dyn()*delta <= getPos().getY())
        {
            blockedDirs.put(Dir.Left,true);
            getPos().setX_dyn(getPos().getX()-0.5+size.getX_dyn()/2.0);
            speed.setX_dyn(0);
        }
        else
        {
            blockedDirs.put(Dir.Left,false);
        }
    }

    @Override
    public void reset()
    {
        super.reset();
        speed.setX(0);
        speed.setY(0);
    }

    public void printBlockedDirs()
    {
        System.out.println(" Up: " + blockedDirs.get(Dir.Up) + " Down: " + blockedDirs.get(Dir.Down) + " Right: " + blockedDirs.get(Dir.Right) + " Left: " + blockedDirs.get(Dir.Left));
    }

}
