package Factories;

import Vectors.DynamicVector;
import Vectors.Vector;
import World.WorldObject.Block.Block;
import World.WorldObject.Block.BricksBlock;
import World.WorldObject.Block.DirtBlock;
import World.WorldObject.DynamicObject.PhysicObject.Mob.Bat;
import World.WorldObject.DynamicObject.PhysicObject.Mob.Mob;
import World.WorldObject.DynamicObject.PhysicObject.Mob.Pig;
import World.WorldObject.DynamicObject.PhysicObject.Mob.Player;
import World.WorldObject.WorldObject;
import World.World;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

public class WorldObjectFactory {

    private static ObjType[] objTypes = new ObjType[]{
            DirtBlock.objType,
            BricksBlock.objType,
            Player.objType,
            Pig.objType,
            Bat.objType
    };

    public static ObjType[] getObjTypes(){return objTypes;}

    public static ObjType findObjType(String value){
        for(ObjType objType : objTypes){
            if(objType.getValue().toLowerCase().equals(value.toLowerCase())){
                return objType;
            }
        }
        return null;
    }

    public static ObjTypeGroup findObjTypeGroup(String value){
        try{
            ObjTypeGroup objTypeGroup = ObjTypeGroup.valueOf(value.toLowerCase());
            return objTypeGroup;
        }
        catch(IllegalArgumentException e)
        {
            return null;
        }
    }

    public static Block createBlock(ObjType objType, World world, Vector vector)
    {
        if(objType.equals(DirtBlock.objType)){return new DirtBlock(world,vector);}
        if(objType.equals(BricksBlock.objType)){return new BricksBlock(world,vector);}
        throw new NotImplementedException();
    }

    public static Mob createMob(ObjType objType, World world, DynamicVector vector)
    {
        if(objType.equals(Player.objType)){return new Player(world,vector.getDynamicVector());}
        if(objType.equals(Pig.objType)){return new Pig(world,vector);}
        if(objType.equals(Bat.objType)){return new Bat(world,vector);}
        throw new NotImplementedException();
    }

}
