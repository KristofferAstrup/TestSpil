package World.WorldObject.DynamicObject.PhysicObject.Mob;

import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Vectors.Vector;
import World.World;
import World.Dir;
import World.Detail;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.HashMap;

public class Boss  extends Mob implements Serializable {

     private static HashMap<String, Image> imgs = new HashMap<String,Image>(){{
        put("body", ImageLibrary.getImage("BossBody.png"));
        put("eye", ImageLibrary.getImage("BossEye.png"));
        put("eyeCharged", ImageLibrary.getImage("BossEyeCharged.png"));
        put("eyeLight", ImageLibrary.getImage("BossEyeLight.png"));
        put("eyeSmallPupil", ImageLibrary.getImage("BossEyeSmallPupil.png"));
        put("eyeLidsFull", ImageLibrary.getImage("BossEyeLidsFull.png"));
        put("eyeLidsHalf", ImageLibrary.getImage("BossEyeLidsHalf.png"));
        put("eyeLidsQuarter", ImageLibrary.getImage("BossEyeLidsQuarter.png"));
    }};

    private DynamicVector targetVec;

    private Detail eyeDetail;
    private Detail eyeLidDetail;
    private Detail eyeLight;

    private double blinkDelay;
    private static double blinkDelayDuration = 2;
    private static double blinkDuration = 0.25;

    private double hitReactionDelay;
    private static double hitReactionDelayDuration = 0.25;

    public Boss(World world, DynamicVector pos) {
        super(world, pos);
        scale = new DynamicVector(4,4);
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);

        eyeLidDetail.setPos(new DynamicVector(pos.add(0,0.56)));

        if(!getAlive())return;

        Player player = world.getPlayerTarget(this);
        targetVec = player.getPos();
        speed.setAdd(targetVec.subtract(pos).normalize().multiply(delta*4));

        DynamicVector normDir = targetVec.subtract(getPos()).normalize();
        eyeDetail.setPos(pos.add(normDir.multiply(0.40,0.10).add(0,0.56)));

        eyeLight.setPos(pos.add(-0.08,0.48));

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
                    world.deleteBlock(v,false);
                    //System.out.println("Pos:" + pos);
                    //System.out.println("v:" + v);
                    Vector vDirN = vDir.normal();
                    world.deleteBlock(v.add(vDirN),false);
                    world.deleteBlock(v.add(vDirN.mulitply(2)),false);
                    //System.out.println("vN:" + v.add(vDir.normal()));

                    Vector vDirNI = vDir.normalInv();
                    world.deleteBlock(v.add(vDirNI),false);
                    world.deleteBlock(v.add(vDirNI.mulitply(2)),false);
                    //System.out.println("vNI:" + v.add(vDir.normalInv()));
                }
            }
        }

        if(hitReactionDelay > 0)
        {
            hitReactionDelay -= delta;
        }
        else {
            blinkDelay -= delta;
            if (blinkDelay < -blinkDuration) {
                blinkDelay = blinkDelayDuration;
                eyeLidDetail.setVisible(false);
            } else if (blinkDelay < -blinkDuration * 4 / 5.0) {
                eyeLidDetail.setImage(imgs.get("eyeLidsQuarter"));
            } else if (blinkDelay < -blinkDuration * 3 / 5.0) {
                eyeLidDetail.setImage(imgs.get("eyeLidsHalf"));
            } else if (blinkDelay < -blinkDuration * 2 / 5.0) {
                eyeLidDetail.setImage(imgs.get("eyeLidsFull"));
            } else if (blinkDelay < -blinkDuration / 5.0) {
                eyeLidDetail.setImage(imgs.get("eyeLidsHalf"));
            } else if (blinkDelay < 0) {
                eyeLidDetail.setImage(imgs.get("eyeLidsQuarter"));
                eyeLidDetail.setVisible(true);
            }
        }
    }

    @Override
    public void init()
    {
        super.init();
        size = new DynamicVector(3,3);
        healthMax = 30;
    }

    @Override
    public void worldStart()
    {
        eyeDetail = new Detail(pos,imgs.get("eye"));
        eyeLight = new Detail(pos,imgs.get("eyeLight"));
        eyeLidDetail = new Detail(pos);
        eyeLidDetail.setVisible(false);
        world.addDetail(eyeDetail);
        //world.addDetail(eyeLight);
        world.addDetail(eyeLidDetail);
        blinkDelay = blinkDelayDuration;
    }

    @Override
    public void reset()
    {
        super.reset();
        gravityEnabled = false;
    }

    @Override
    public void damage(int damage)
    {
        super.damage(damage);
        blinkDelay = -blinkDuration*2.5/5.0;
        eyeLidDetail.setImage(imgs.get("eyeLidsFull"));
        eyeLidDetail.setVisible(true);
        hitReactionDelay = hitReactionDelayDuration;
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
        world.deleteDetail(eyeDetail);
        eyeDetail = null;
        world.deleteDetail(eyeLight);
        eyeLight = null;
        gravityEnabled = true;
        eyeLidDetail.setImage(imgs.get("eyeLidsFull"));
        eyeLidDetail.setVisible(true);
    }

}