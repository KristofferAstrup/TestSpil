package World.WorldObject.DynamicObject.PhysicObject.Mob;

import Factories.ObjType;
import Factories.ObjTypeGroup;
import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Vectors.Vector;
import World.World;
import World.Dir;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by kristoffer on 20-05-2017.
 */
public class Boss  extends Mob implements Serializable {

    public static final ObjType objType = new ObjType("Boss", ImageLibrary.getImage("BossBody.png"), ObjTypeGroup.mobs);
    private static HashMap<String, Image> imgs = new HashMap<String,Image>(){{
        put("body", ImageLibrary.getImage("BossBody.png"));
    }};

    private static DynamicVector targetVec;

    public Boss(World world, DynamicVector pos) {
        super(world, pos);
        scale = new DynamicVector(4,4);
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);
        if(!getAlive())return;

        Player player = world.getPlayerTarget(this);
        targetVec = player.getPos();
        speed.setAdd(targetVec.subtract(pos).normalize().multiply(delta*4));

        if(targetVec.dist(pos) < size.getX_dyn()/2+player.getSize().getX_dyn()/2)
        {
            world.getPlayerTarget(this).damage(1);
        }

        //Destroy the blocks which Boss collides with
        if(blocked) {
            for (Dir dir : Dir.getValues()) {
                if (blockedDirs.get(dir)) {
                    Vector vDir = dir.getVector();
                    Vector v = pos.add(vDir.mulitply(2));
                    world.deleteBlock(v);
                    //System.out.println("Pos:" + pos);
                    //System.out.println("v:" + v);
                    Vector vDirN = vDir.normal();
                    world.deleteBlock(v.add(vDirN));
                    world.deleteBlock(v.add(vDirN.mulitply(2)));
                    //System.out.println("vN:" + v.add(vDir.normal()));

                    Vector vDirNI = vDir.normalInv();
                    world.deleteBlock(v.add(vDirNI));
                    world.deleteBlock(v.add(vDirNI.mulitply(2)));
                    //System.out.println("vNI:" + v.add(vDir.normalInv()));
                }
            }
        }
    }

    @Override
    public void init()
    {
        super.init();
        size = new DynamicVector(3,3);
        healthMax = 100;
    }

    @Override
    public void reset()
    {
        super.reset();
        gravityEnabled = false;
    }

    @Override
    public Image getImage() {
        return imgs.get("body");
    }

    @Override
    public void die()
    {
        super.die();
        System.out.println("Boss died!");
        gravityEnabled = true;
    }

}