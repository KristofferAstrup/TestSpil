package World.WorldObject;

import Factories.ObjType;
import Vectors.DynamicVector;
import Vectors.Vector;
import World.World;
import javafx.scene.image.Image;

import java.io.Serializable;

public abstract class WorldObject implements Serializable {

    public abstract Image getImage();

    public abstract Vector getPos();

    public void reset(){
        destroyed = false;
    }

    protected World world;

    private Vector origin;

    private boolean destroyed;

    public Vector getOrigin()
    {
        return origin;
    }

    public WorldObject(World world,Vector pos)
    {
        setWorld(world);
        origin = new Vector(pos);
    }

    public void setWorld(World world){this.world = world;}

    public abstract void init();

    public void destroy(){world.destroyWorldObject(this);}

    public void setDestroyed(boolean destroyed){this.destroyed = destroyed;}

    public boolean isDestroyed(){return destroyed;}

}
