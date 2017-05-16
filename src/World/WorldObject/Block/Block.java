package World.WorldObject.Block;

import Factories.ObjType;
import Libraries.ImageLibrary;
import Vectors.Vector;
import World.World;
import World.WorldObject.WorldObject;
import javafx.scene.image.Image;
import World.Dir;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Kris on 08-02-2017.
 */
public abstract class Block extends WorldObject implements Serializable {

    protected abstract String getTypeName();
    protected transient int rot;
    protected transient Image lastImage;

    public static ObjType objType;

    @Override
    public Image getImage() {
        return lastImage;
    }

    public Block(World world, Vector pos)
    {
        super(world,pos);
        this.pos = new Vector(pos);
        rot = 0;
        init();
        reset();
    }

    public abstract void updateImage();

    private Vector pos;

    @Override
    public void init()
    {
    }

    @Override
    public Vector getPos()
    {
        return pos;
    }

    @Override
    public void reset(){
        pos.set(getOrigin());
    }

    public void setRot(int rot)
    {
        this.rot = rot;
    }

    public int getRot()
    {
        return rot;
    }

    public static enum ORI{
        Z,
        U,
        Q,
        C,
        E,
        S,
        O
    }

    public Block.ORI getORIsetRot(BlockedOrientation orientation)
    {
        System.out.println(orientation.getBlocks());
        switch(orientation.getBlocks())
        {
            case 4:
                rot = 0;
                return Block.ORI.Z;
            case 3: {
                if(!orientation.getBlocked(Dir.Right)){rot = 90;}
                else if(!orientation.getBlocked(Dir.Down)){rot = 180;}
                else if(!orientation.getBlocked(Dir.Left)){rot = 270;}
                else{rot = 0;}
                return Block.ORI.S;
            }
            case 2: {
                if(orientation.getBlocked(Dir.Left)&&orientation.getBlocked(Dir.Right)){rot = 0; return Block.ORI.E;}
                if(orientation.getBlocked(Dir.Up)&&orientation.getBlocked(Dir.Down)){rot = 90; return Block.ORI.E;}

                if(orientation.getBlocked(Dir.Left)&&orientation.getBlocked(Dir.Down)){rot = 0; return Block.ORI.C;}
                if(orientation.getBlocked(Dir.Left)&&orientation.getBlocked(Dir.Up)){rot = 90; return Block.ORI.C;}
                if(orientation.getBlocked(Dir.Up)&&orientation.getBlocked(Dir.Right)){rot = 180; return Block.ORI.C;}
                if(orientation.getBlocked(Dir.Right)&&orientation.getBlocked(Dir.Down)){rot = 270; return Block.ORI.C;}
            }
            case 1: {
                if(orientation.getBlocked(Dir.Right)){rot = 90;}
                else if(orientation.getBlocked(Dir.Down)){rot = 180;}
                else if(orientation.getBlocked(Dir.Left)){rot = 270;}
                return Block.ORI.U;
            }
            case 0: {
                rot = 0;
                return Block.ORI.O;
            }
        }
        throw new RuntimeException("There was no ORI for Top:" + orientation.getBlocked(Dir.Up) + " Left:" + orientation.getBlocked(Dir.Left) + " Right:" + orientation.getBlocked(Dir.Right) + " Bottom:" + orientation.getBlocked(Dir.Down));
    }

}
