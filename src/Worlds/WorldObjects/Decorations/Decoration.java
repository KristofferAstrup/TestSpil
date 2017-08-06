package Worlds.WorldObjects.Decorations;

import Vectors.Vector;
import Worlds.World;
import Worlds.WorldObjects.WorldObject;
import javafx.scene.image.Image;

public abstract class Decoration extends WorldObject {

    protected Vector pos;
    protected transient boolean flipped;
    protected transient Image lastImage;

    public Decoration(World world, Vector pos) {
        super(world,pos);
        this.pos = new Vector(pos);
        init();
        reset();
    }

    public boolean getFlipped(){return flipped;}

    public abstract void updateImage();

    protected final DecorationFace getFace(){

        DecorationFace decorationFace = new DecorationFace();

        if(pos.getY()%2==0) //Horizontal
        {
            if(world.checkIfEmptyBlock(pos.getX(),pos.getY()/2)){
                if(world.checkIfEmptyBlock(pos.getX(),pos.getY()/2+1)) {

                    decorationFace.face = DecorationFace.Face.Vertical;
                    DecorationFace.Position posUpper = getDecorationFacePosition(1,1,XY.x); //Ordinalet (nr) i Position Enumet svarer til hvor mange blokke der er nær decorationen, dvs. den vælger den med færrest hvilket oversætter til en prioritering af Solo>Left>Right>Center
                    DecorationFace.Position posLower = getDecorationFacePosition(1,0,XY.x);
                    decorationFace.position = posUpper.ordinal() > posLower.ordinal() ? posUpper : posLower;

                } else {

                    decorationFace.face = DecorationFace.Face.Up;
                    decorationFace.position = getDecorationFacePosition(1,0,XY.x);
                }
            }
            else if(world.checkIfEmptyBlock(pos.getX(),pos.getY()/2+1)){

                decorationFace.face = DecorationFace.Face.Down;
                decorationFace.position = getDecorationFacePosition(1,1,XY.x);

            }
        }
        else                //Vertical
        {
            if(world.checkIfEmptyBlock(pos.getX(),(pos.getY()-1)/2)){
                if(world.checkIfEmptyBlock(pos.getX()-1,(pos.getY()-1)/2)) {

                    decorationFace.face = DecorationFace.Face.Horizontal;
                    DecorationFace.Position posUpper = getDecorationFacePosition(-1,1,XY.y); //Ordinalet (nr) i Position Enumet svarer til hvor mange blokke der er nær decorationen, dvs. den vælger den med færrest hvilket oversætter til en prioritering af Solo>Left>Right>Center
                    DecorationFace.Position posLower = getDecorationFacePosition(0,1,XY.y);
                    decorationFace.position = posUpper.ordinal() > posLower.ordinal() ? posUpper : posLower;

                } else {

                    decorationFace.face = DecorationFace.Face.Left;
                    decorationFace.position = getDecorationFacePosition(0,1,XY.y);
                }
            }
            else if(world.checkIfEmptyBlock(pos.getX()-1,(pos.getY()-1)/2)){

                decorationFace.face = DecorationFace.Face.Right;
                decorationFace.position = getDecorationFacePosition(-1,1,XY.x);

            }
        }

        if(decorationFace.face == null)
        throw new RuntimeException("Was unable to find a matching DecorationFace!");

        return decorationFace;
    }

    private DecorationFace.Position getDecorationFacePosition(int dx,int dy,XY xy)
    {
        int y = (pos.getY()-(xy==XY.y?1:0))/2;
        int x = pos.getX();
        int rdx = (xy==XY.x?-dx:dx);
        int rdy = (xy==XY.y?-dy:dy);
        if(world.checkIfEmptyBlock(x+dx+dy,y+dx+dy))
        {
            if(world.checkIfEmptyBlock(x+rdx+rdy,y+rdx+rdy))
            {
                return DecorationFace.Position.Solo;
            }
            else
            {
                return DecorationFace.Position.Right;
            }
        }
        else if(world.checkIfEmptyBlock(x+rdx+rdy,y+rdx+rdy))
        {
            return DecorationFace.Position.Left;
        }
        else
        {
            return DecorationFace.Position.Center;
        }
    }

    private enum XY
    {
        x,
        y
    }

    @Override
    public void init() {

    }

    @Override
    public void reset() {
        super.reset();
        pos.set(getOrigin());
    }

}
