package Worlds.WorldObjects.DynamicObjects.PhysicObjects;

import Vectors.DynamicVector;
import Worlds.*;
import Worlds.WorldObjects.DynamicObjects.DynamicObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Kris on 20-02-2017.
 */
public abstract class PhysicObject extends DynamicObject implements Serializable {

    protected transient DynamicVector speed;
    protected boolean blocked = false;
    protected transient HashMap<Dir,Boolean> blockedDirs;
    protected boolean gravityEnabled = true;

    public PhysicObject(World world, DynamicVector pos) {
        super(world, pos);
    }

    private static final double hotfixConstant = 0.05;
    private static final double blockedFixConstant = 0.001;

    @Override
    public void init()
    {
        speed = new DynamicVector(0,0);
        blockedDirs = new HashMap<Dir,Boolean>() {{put(Dir.Right,false); put(Dir.Down,false); put(Dir.Left,false); put(Dir.Up,false);}};
    }

    @Override
    public void update(double delta)
    {
        blockCollisionCheck(delta);

        if(gravityEnabled && !blockedDirs.get(Dir.Down))
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
        blocked = false;

        //DOWN
        int y = (int)Math.round(getPos().getY_dyn()-blockedFixConstant+speed.getY_dyn()*delta-size.getY_dyn()/2.0);
        if(!world.checkIfEmptyBlock((int)Math.round(getPos().getX_dyn()),y)
                || !world.checkIfEmptyBlock((int)Math.round(getPos().getX_dyn()-size.getX_dyn()/2+hotfixConstant),y)
                || !world.checkIfEmptyBlock((int)Math.round(getPos().getX_dyn()+size.getX_dyn()/2-hotfixConstant),y))//!world.checkIfEmptyBlock(getWorldPositionFromScreen().getX(),getWorldPositionFromScreen().getY()-1) && getWorldPositionFromScreen().getY_dyn()+speed.getY_dyn()*delta <= getWorldPositionFromScreen().getY())
        {
            blockedDirs.put(Dir.Down,true);
            getPos().setY_dyn(y+0.5+size.getY_dyn()/2);
            speed.setY(0);
            blocked = true;
        }
        else
        {
            blockedDirs.put(Dir.Down,false);
        }

        //UP
        y = (int)Math.round(getPos().getY_dyn()+speed.getY_dyn()*delta+size.getY_dyn()/2.0);
        if(!world.checkIfEmptyBlock((int)Math.round(getPos().getX_dyn()),y)
                || !world.checkIfEmptyBlock((int)Math.round(getPos().getX_dyn()-size.getX_dyn()/2+hotfixConstant),y)
                || !world.checkIfEmptyBlock((int)Math.round(getPos().getX_dyn()+size.getX_dyn()/2-hotfixConstant),y))//!world.checkIfEmptyBlock(getWorldPositionFromScreen().getX(),getWorldPositionFromScreen().getY()-1) && getWorldPositionFromScreen().getY_dyn()+speed.getY_dyn()*delta <= getWorldPositionFromScreen().getY())
        {
            blockedDirs.put(Dir.Up,true);
            getPos().setY_dyn(y-0.5-size.getY_dyn()/2);
            speed.setY_dyn(-0.1);
            blocked = true;
        }
        else
        {
            blockedDirs.put(Dir.Up,false);
        }

        //RIGHT
        int x = (int)Math.round(getPos().getX_dyn()+blockedFixConstant+speed.getX_dyn()*delta+(size.getX_dyn()/2.0));
        if(!world.checkIfEmptyBlock(x,getPos().getY())
                || !world.checkIfEmptyBlock(x,(int)Math.round(getPos().getY_dyn()-size.getY_dyn()/2+hotfixConstant))//+speed.getY_dyn()*delta)-1)
                || !world.checkIfEmptyBlock(x,(int)Math.round(getPos().getY_dyn()+size.getY_dyn()/2-hotfixConstant)))//!world.checkIfEmptyBlock(getWorldPositionFromScreen().getX(),getWorldPositionFromScreen().getY()-1) && getWorldPositionFromScreen().getY_dyn()+speed.getY_dyn()*delta <= getWorldPositionFromScreen().getY())
        {
            blockedDirs.put(Dir.Right,true);
            getPos().setX_dyn(x-0.5-size.getX_dyn()/2);
            speed.setX_dyn(0);
            blocked = true;
        }
        else
        {
            blockedDirs.put(Dir.Right,false);
        }

        //LEFT
        x = (int)Math.round(getPos().getX_dyn()-blockedFixConstant+speed.getX_dyn()*delta-(size.getX_dyn()/2.0));
        if(!world.checkIfEmptyBlock(x,getPos().getY())
                || !world.checkIfEmptyBlock(x,(int)Math.round(getPos().getY_dyn()-size.getY_dyn()/2+hotfixConstant))//+speed.getY_dyn()*delta)-1)
                || !world.checkIfEmptyBlock(x,(int)Math.round(getPos().getY_dyn()+size.getY_dyn()/2-hotfixConstant)))//!world.checkIfEmptyBlock(getWorldPositionFromScreen().getX(),getWorldPositionFromScreen().getY()-1) && getWorldPositionFromScreen().getY_dyn()+speed.getY_dyn()*delta <= getWorldPositionFromScreen().getY())
        {
            blockedDirs.put(Dir.Left,true);
            getPos().setX_dyn(x+0.5+size.getX_dyn()/2);
            speed.setX_dyn(0);
            blocked = true;
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
