package State.EditorState;

import Controller.*;
import Factories.EditorClassGroup;
import Libraries.EditorClass;
import Libraries.ImageLibrary;
import Libraries.EditorLibrary;
import State.IState;
import Vectors.DynamicVector;
import Vectors.Vector;
import View.View;
import World.World;
import World.Dir;
import World.Detail;
import World.WorldObject.Block.Block;
import World.WorldObject.DynamicObject.DynamicObject;
import World.WorldObject.DynamicObject.Goal;
import World.WorldObject.DynamicObject.PhysicObject.Mob.Mob;
import World.WorldObject.DynamicObject.PhysicObject.Mob.Pig;
import World.WorldObject.WorldObject;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.omg.SendingContext.RunTime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

/**
 * Created by Kris on 02-03-2017.
 */
public class EditorState implements IState {

    private KeyboardController keyboardController;
    private MouseController mouseController;
    private World world;
    public static double time = 0;
    private DynamicVector worldTarget;
    private DynamicVector cameraPivot;
    private DynamicVector mousePanCameraPivot;
    private Vector objectMenuTarget;
    private Vector objectMenuSelected;
    private double moveDelayBase = 0.2;
    private double moveDelayFast = 0.05;
    private double[] moveDelays = new double[4];
    private PanType panType = PanType.keyboard;
    private double mousePanSpeedBase = 14;
    private double mousePanSpeedFast = 28;

    private double gridSize = 1; ****

    private double zoomScale = 2;

    private View view;

    private EditorClass editorClassSelected;
    private EditorClassGroup editorClassGroup;

    private EditorMode mode;

    private int ObjTypesPerLine = 10;

    public double getZoomScale(){return zoomScale;}
    public Vector getWorldTarget() {return worldTarget;}
    public Vector getObjectMenuTarget() {return objectMenuTarget;}
    public Vector getObjectMenuSelected(){return objectMenuSelected;}
    public DynamicVector getCameraPivot(){return cameraPivot;}
    public EditorMode getEditorMode(){return mode;}

    public EditorState(KeyboardController keyboardController,MouseController mouseController,View view)
    {
        this.keyboardController = keyboardController;
        this.mouseController = mouseController;
        this.view = view;
        mode = EditorMode.World;
        objectMenuTarget = new Vector(0,0);
        objectMenuSelected = new Vector(0,0);
        menuObjectSelect(0,0);
        changeObjTypeGroup(EditorClassGroup.blocks);

    }

    public void changeObjTypeGroup(EditorClassGroup objTypeGroup)
    {
        this.editorClassGroup = objTypeGroup;
        editorClassSelected = EditorLibrary.getEditorClasses(editorClassGroup)[0];
    }

    public void changeObjTypeSelected(int index)
    {
        if(index > 0 && index < EditorLibrary.getEditorClasses(editorClassGroup).length) {
            editorClassSelected = EditorLibrary.getEditorClasses(editorClassGroup)[index];
        }
    }

    public EditorClass getObjTypeSelected()
    {
        return editorClassSelected;
    }

    public EditorClassGroup getObjTypeGroup()
    {
        return editorClassGroup;
    }

    public static WorldObject createWorldObject(Class c,World world,Vector pos)
    {
        return createWorldObject(c,world,pos.getDynamicVector());
    }

    public static WorldObject createWorldObject(Class c,World world,DynamicVector pos)
    {
        if(!(WorldObject.class.isAssignableFrom(c)))throw new RuntimeException("Class used in createWorldObject, is not assignable from WorldObject");
        try{

            WorldObject worldObject = null;

            if(Block.class.isAssignableFrom(c)) {
                Constructor ctor = c.getDeclaredConstructor(World.class, Vector.class);
                worldObject = (WorldObject) ctor.newInstance(world, new Vector(pos));
            }
            else if(DynamicObject.class.isAssignableFrom(c)) {
                Constructor ctor = c.getDeclaredConstructor(World.class, DynamicVector.class);
                worldObject = (WorldObject) ctor.newInstance(world, pos);
            }
            if(worldObject == null) throw new RuntimeException("Class is assignable from WorldObject but not supported by the createWorldObject method");

            world.addWorldObject(worldObject);
            return worldObject;

        }
        catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        throw new RuntimeException("Unexpected occurence in createWorldObject");
    }

    public static void deleteObj(World world, Vector target, EditorClassGroup objTypeGroup)
    {
        switch(objTypeGroup)
        {
            case blocks:
            {
                world.deleteBlock(target);
            }
            case mobs:
            {
                world.deleteDynamicObjects(target);
            }
        }
    }

    public static void fillBlock(World world,Vector target,Class fillClass)
    {
        if(!Block.class.isAssignableFrom(fillClass)) throw new IllegalArgumentException("Class used in fillBlock is not an extension Block");
        HashSet<Vector> collectedVectors = new HashSet<>();
        Class comparingClass = world.getBlock(target) == null ? null : world.getBlock(target).getClass();

        collectedVectors.add(target);
        collectEqualBlocks(world,target,comparingClass,collectedVectors);

        for(Vector vector : collectedVectors)
        {
            world.deleteBlock(vector);
            createWorldObject(fillClass,world,vector);
        }
    }

    private static void collectEqualBlocks(World world,Vector target,Class c,HashSet<Vector> collectedVectors)
    {
        for(Dir dir : Dir.getValues())
        {
            Vector transTarget = target.add(dir.getVector());
            if(!collectedVectors.contains(transTarget) && !world.outsideBoundary(transTarget) && (world.getBlock(transTarget) == null ? c == null : world.getBlock(transTarget).getClass() == c))
            {
                collectedVectors.add(transTarget);
                collectEqualBlocks(world,transTarget,c,collectedVectors);
            }
        }
    }


    public State getState(){return State.Editor;}

    public World getWorld()
    {
        return world;
    }

    public void startState()
    {
        this.world = Controller.getWorld();
        world.endInit();
        worldTarget = new DynamicVector(world.getPlayerSpawnPoint()); //new Vector(world.getPlayerSpawnPoint());//new Vector(2,world.getWorldHeight()-2); //No particular reason for (1,1), just the initial worldTarget point.
        cameraPivot = new DynamicVector(worldTarget);
        mousePanCameraPivot = new DynamicVector(worldTarget);

        /*try {
            Constructor<Pig> ctor = Pig.class.getDeclaredConstructor(World.class,DynamicVector.class);
            Pig pig = ctor.newInstance(world,new DynamicVector(10,10));
            world.addDynamicObject(pig);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }*/
    }

    public void endState()
    {

    }

    public void update(double delta)
    {
        time += delta;

        zoomScale += 0.25*mouseController.getMouseScrollDir();
        if(!Controller.debugging()){
            zoomScale = Math.min(4,Math.max(zoomScale,1));
        }

        Vector movement = getMovement(KeyCode.RIGHT,KeyCode.LEFT,KeyCode.UP,KeyCode.DOWN,keyboardController.getKeyPressed(KeyCode.ALT)?moveDelayFast:moveDelayBase);
        DynamicVector mousePanDir = view.getPanDirectionVector(mouseController.getMousePosition(),200,150);

        if(mode == EditorMode.World)
        {
            if((!mouseController.getMouseMovement().equals(Vector.ZERO) || !mousePanDir.equals(Vector.ZERO))) {

                worldTarget.set(view.getWorldPositionFromScreen(world,mouseController.getMousePosition()));
                worldTarget = getVectorInWorldBounds(worldTarget).getDynamicVector();

                panType = PanType.mouse;
            }
            else if (!movement.equals(Vector.ZERO)){
                moveWorldTarget(movement);
                panType = PanType.keyboard;
            }

            if(((keyboardController.getKeyPressed(KeyCode.SPACE) || mouseController.getButtonPressed(MouseButton.PRIMARY)) && editorClassGroup==EditorClassGroup.blocks) || keyboardController.getKeyJustPressed(KeyCode.SPACE))
            {
                createWorldObject(editorClassSelected.getClasss(),world,worldTarget);
            }
            if(keyboardController.getKeyPressed(KeyCode.BACK_SPACE))
            {
                deleteObj(world,worldTarget,editorClassGroup);
            }
            if(keyboardController.getKeyPressed(KeyCode.ENTER))
            {
                world.setPlayerSpawnPoint(worldTarget.getDynamicVector());
            }
            if(keyboardController.getKeyJustPressed(KeyCode.M))
            {
                world.addDynamicObject(new Pig(world, worldTarget.getDynamicVector()));
            }
            if(keyboardController.getKeyJustPressed(KeyCode.S))
            {
                SaveLoadController.saveFile("MyWorld",world);
            }
            if(keyboardController.getKeyJustPressed(KeyCode.F))
            {
                if(Block.class.isAssignableFrom(editorClassSelected.getClasss())){
                    fillBlock(world,worldTarget,editorClassSelected.getClasss());
                }
            }
            if(keyboardController.getKeyJustPressed(KeyCode.G))
            {
                createWorldObject(Goal.class,world,worldTarget);
            }
            if(keyboardController.getKeyJustPressed(KeyCode.L))
            {
                Controller.setWorld((World)SaveLoadController.loadFile("MyWorld"));
                world = Controller.getWorld();
            }

            if((keyboardController.getKeyPressed(KeyCode.V)))
            {
                Detail detail = new Detail(new DynamicVector(getWorldTarget()).add(0,0.5), ImageLibrary.getImage("Grasstop.png"));
                world.addDetail(detail);
            }
        }
        else if(mode == EditorMode.ObjectSelect)
        {
            moveObjectMenuTarget(movement);

            if(keyboardController.getKeyJustPressed(KeyCode.SPACE))
            {
                changeObjTypeSelected(objectMenuTarget.getX()+objectMenuTarget.getY()*ObjTypesPerLine);
                menuObjectSelect(objectMenuTarget.getX(),objectMenuTarget.getY());
            }
            if(keyboardController.getKeyJustPressed(KeyCode.ALT))
            {
                changeObjTypeGroup(editorClassGroup==EditorClassGroup.blocks ?EditorClassGroup.mobs :EditorClassGroup.blocks);
            }
        }

        if(panType == PanType.keyboard) {
            cameraPivot.setAdd(new DynamicVector(getWorldTarget().getX() - cameraPivot.getX_dyn(), getWorldTarget().getY() - cameraPivot.getY_dyn()).multiply(8 * delta));
        }
        else if(panType == PanType.mouse){

            if(mouseController.getButtonPressed(MouseButton.MIDDLE)) {
                //cameraPivot.setAdd(new DynamicVector(2*delta,2*delta));
            }
            else {
                double speed = keyboardController.getKeyPressed(KeyCode.ALT) ? mousePanSpeedFast : mousePanSpeedBase;
                DynamicVector cameraPivotAdd = new DynamicVector(mousePanDir.getX_dyn() * delta * speed, mousePanDir.getY_dyn() * delta * speed);
                cameraPivot.setAdd(cameraPivotAdd);
                cameraPivot = view.getMinimumCornerCenterVector(world, cameraPivot);
            }
        }

        if(keyboardController.getKeyJustPressed(KeyCode.SHIFT))
        {
            mode = mode==EditorMode.ObjectSelect?EditorMode.World:EditorMode.ObjectSelect;
        }
    }

    //private void fill(Vector pos,)

    private Vector getVectorInWorldBounds(Vector vector)
    {
        vector = new Vector(vector);

        if(vector.getX() < 0){vector.setX(0);}
        else if(vector.getX() >= world.getWorldWidth()){vector.setX(world.getWorldWidth()-1);}

        if(vector.getY() < 0){vector.setY(0);}
        else if(vector.getY() >= world.getWorldHeight()){vector.setY(world.getWorldHeight()-1);}

        return vector;
    }

    private void moveWorldTarget(Vector movement)
    {
        worldTarget.set(Math.min(world.getWorldWidth()-1,Math.max(0,worldTarget.getX()+movement.getX())),
                Math.min(world.getWorldHeight()-1,Math.max(0,worldTarget.getY()+movement.getY())));
    }

    private void moveObjectMenuTarget(Vector movement)
    {
        objectMenuTarget = objectMenuTarget.add(movement.getX(),-movement.getY());
        int objTypes = (EditorLibrary.getEditorClasses(editorClassGroup).length-1);

        if(objectMenuTarget.getX() < 0)
        {
            if(objectMenuTarget.getY() == 0)
            {
                objectMenuTarget.setX(0);
            }
            else
            {
                objectMenuTarget.setY(objectMenuTarget.getY()-1);
                objectMenuTarget.setX(9);
            }
        }
        else if(objectMenuTarget.getX() > 9)
        {
            objectMenuTarget.setX(0);
            objectMenuTarget.setY(objectMenuTarget.getY()+1);
        }
        if(objectMenuTarget.getY() < 0){
            objectMenuTarget.setY(0);
        }
        else if(objectMenuTarget.getY() >= Math.floor(objTypes/ObjTypesPerLine))
        {
            objectMenuTarget.setY((int)Math.floor(objTypes/ObjTypesPerLine));
            if(objectMenuTarget.getX() > objTypes%ObjTypesPerLine){objectMenuTarget.setX(objTypes%ObjTypesPerLine);}
        }
    }

    private void menuObjectSelect(int x,int y)
    {
        objectMenuSelected.set(x,y);
    }

    private Vector getMovement(KeyCode right,KeyCode left,KeyCode up,KeyCode down,double delay)
    {
        Vector movement = new Vector(0,0);
        if(keyboardController.getKeyJustPressed(right) || (keyboardController.getKeyPressed(right) && time >= moveDelays[0]+delay))
        {
            movement.setX(1);
            moveDelays[0] = time;
        }
        else if(keyboardController.getKeyJustPressed(left) || (keyboardController.getKeyPressed(left) && time >= moveDelays[1]+delay))
        {
            movement.setX(-1);
            moveDelays[1] = time;
        }
        if(keyboardController.getKeyJustPressed(up) || (keyboardController.getKeyPressed(up) && time >= moveDelays[2]+delay))
        {
            movement.setY(1);
            moveDelays[2] = time;
        }
        else if(keyboardController.getKeyJustPressed(down) || (keyboardController.getKeyPressed(down) && time >= moveDelays[3]+delay))
        {
            movement.setY(-1);
            moveDelays[3] = time;
        }
        return movement;
    }

    private enum PanType
    {
        keyboard,
        mouse
    }

}
