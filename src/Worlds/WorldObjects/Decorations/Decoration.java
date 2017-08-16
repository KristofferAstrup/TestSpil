package Worlds.WorldObjects.Decorations;

import Vectors.Vector;
import Worlds.World;
import Worlds.WorldObjects.WorldObject;
import javafx.scene.image.Image;

public abstract class Decoration extends WorldObject {

    protected Vector pos;
    protected transient boolean flipped;
    protected transient Image lastImage;
    protected transient DecorationFace lastFace;

    public Decoration(World world, Vector pos) {
        super(world,pos);
        this.pos = new Vector(pos);
        init();
        reset();
    }

    @Override
    public Vector getPos()
    {
        return pos;
    }

    public boolean getFlipped(){return flipped;}

    @Override
    public Image getImage() {
        return lastImage;
    }

    public DecorationFace getDecorationFace() {return lastFace;}

    public abstract void updateImage();



    @Override
    public void init() {

    }

    @Override
    public void reset() {
        super.reset();
        pos.set(getOrigin());
    }

    public abstract void die();

}
