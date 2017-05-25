package Libraries;

import Factories.ObjType;
import Factories.ObjTypeGroup;
import World.WorldObject.Block.BricksBlock;
import World.WorldObject.Block.DirtBlock;
import World.WorldObject.DynamicObject.PhysicObject.Mob.*;

import java.util.HashMap;

public class ObjTypeLibrary {

    private final static HashMap<ObjTypeGroup, ObjType[]> objTypes = new HashMap<ObjTypeGroup,ObjType[]>(){{
       put(ObjTypeGroup.blocks,new ObjType[]{
               DirtBlock.objType,
               BricksBlock.objType
       });
       put(ObjTypeGroup.mobs,new ObjType[]{
               //Player.objType,
               Pig.objType,
               Bat.objType,
               Boss.objType
        });
    }};

    public static ObjType[] getObjTypes(ObjTypeGroup group)
    {
        return objTypes.get(group);
    }

}
