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

    public abstract void reset();

    protected World world;

    private Vector origin;

    public Vector getOrigin()
    {
        return origin;
    }

    public WorldObject(World world,Vector pos)
    {
        this.world = world;
        origin = new Vector(pos);
    }

    public abstract void init();

}
