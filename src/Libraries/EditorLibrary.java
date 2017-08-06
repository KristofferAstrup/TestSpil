package Libraries;

import Factories.EditorClassGroup;
import Worlds.WorldObjects.Blocks.*;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.*;

import java.util.HashMap;

public class EditorLibrary {

    private static HashMap<EditorClassGroup,EditorClass[]> editorClassGroups = new HashMap<EditorClassGroup,EditorClass[]>(){{
        put(EditorClassGroup.blocks,new EditorClass[]{
                new EditorClass(DirtBlock.class,ImageLibrary.getImage("Dirt_O.png")),
                new EditorClass(PlankwallBlock.class,ImageLibrary.getImage("Plankwall_E.png")),
                new EditorClass(StoneBlock.class,ImageLibrary.getImage("Stone0.png")),
                new EditorClass(WoodlogBlock.class,ImageLibrary.getImage("Woodlog.png")),
                new EditorClass(BricksBlock.class,ImageLibrary.getImage("Bricks_O.png")),
        });
        put(EditorClassGroup.mobs,new EditorClass[]{
                new EditorClass(Pig.class,ImageLibrary.getImage("pig_0.png")),
                new EditorClass(Bat.class,ImageLibrary.getImage("bat_0.png")),
                new EditorClass(Boss.class,ImageLibrary.getImage("BossBody.png")),
        });
    }};

    public static EditorClass[] getEditorClasses(EditorClassGroup group)
    {
        return editorClassGroups.get(group);
    }

    public static EditorClassGroup findEditorClassGroup(String editorClassGroupName){
        for(EditorClassGroup editorClassGroup : EditorClassGroup.values()) {
            if(editorClassGroup.name() == editorClassGroupName && editorClassGroups.containsKey(editorClassGroup))
            {
                return editorClassGroup;
            }
        }
        return null;
    }

    public static EditorClass findEditorClass(String className)
    {
        for(EditorClassGroup editorClassGroup : EditorClassGroup.values())
        {
            if(!editorClassGroups.containsKey(editorClassGroup))continue;
            for(EditorClass editorClass : editorClassGroups.get(editorClassGroup))
            {
                if(editorClass.getName() == className){
                    return editorClass;
                }
            }
        }
        return null;
    }

}
