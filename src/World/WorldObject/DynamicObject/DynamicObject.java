package World.WorldObject.DynamicObject;

import Vectors.DynamicVector;
import World.World;
import World.WorldObject.WorldObject;

import java.io.Serializable;

/**
 * Created by Kris on 20-02-2017.
 */
public abstract class DynamicObject extends WorldObject implements Serializable {

    protected DynamicVector pos;
    protected transient boolean flip = false;
    protected transient boolean active = true;

    protected boolean originFlip = false;
    protected boolean originActive = true;

    public DynamicObject(World world, DynamicVector pos)
    {
        super(world,pos);
        this.pos = new DynamicVector(pos);
    }

    public abstract void update(double delta);

    public boolean getFlipped()
    {
        return flip;
    }

    @Override
    public DynamicVector getPos()
    {
        return pos;
    }

    @Override
    public void reset(){
        pos.set(getOrigin());
        flip = originFlip;
        active = originActive;
    }

}
