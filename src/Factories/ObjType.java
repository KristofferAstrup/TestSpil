package Factories;

import World.World;
import World.WorldObject.WorldObject;
import javafx.scene.image.Image;

public class ObjType {

    private String value;
    private Image icon;
    private ObjTypeGroup group;

    public ObjType(String value,Image icon,ObjTypeGroup group)
    {
        this.value = value;
        this.icon = icon;
        this.group = group;
    }

    public String getValue()
    {
        return value;
    }

    public Image getIcon(){return icon;}

    public ObjTypeGroup getObjTypeGroup(){return group;}

    @Override
    public boolean equals(Object objType)
    {
        return value == ((ObjType)objType).value;
    }

}
