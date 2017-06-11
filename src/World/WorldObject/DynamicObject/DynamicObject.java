package World.WorldObject.DynamicObject;

import Controller.IUpdate;
import Vectors.DynamicVector;
import World.World;
import World.WorldObject.WorldObject;

import java.io.Serializable;

/**
 * Created by Kris on 20-02-2017.
 */
public abstract class DynamicObject extends WorldObject implements Serializable, IUpdate {

    protected DynamicVector pos;
    protected transient boolean flip = false;
    protected transient boolean active = true;
    protected transient double rot;

    protected transient DynamicVector size;
    protected DynamicVector scale = new DynamicVector(1,1);

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

    public void setFlipped(boolean flipped){this.flip = flipped;}

    public DynamicVector getSize() {return size;}

    public DynamicVector getScale() {return scale;}

    public double getRot(){return rot;}

    @Override
    public DynamicVector getPos()
    {
        return pos;
    }

    @Override
    public void reset(){
        super.reset();
        pos.set(getOrigin());
        flip = originFlip;
        active = originActive;
    }

}
