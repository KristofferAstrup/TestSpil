package States.EditorStates;

import Controllers.*;
import Factories.EditorClassGroup;
import Libraries.EditorClass;
import Libraries.ImageLibrary;
import Libraries.EditorLibrary;
import Libraries.KeybindLibrary;
import States.IState;
import Vectors.DynamicVector;
import Vectors.Vector;
import Views.View;
import Worlds.World;
import Worlds.Dir;
import Worlds.Detail;
import Worlds.WorldObjects.Blocks.Block;
import Worlds.WorldObjects.Decorations.Decoration;
import Worlds.WorldObjects.Decorations.Grass;
import Worlds.WorldObjects.DynamicObjects.DynamicObject;
import Worlds.WorldObjects.DynamicObjects.Goal;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Pig;
import Worlds.WorldObjects.WorldObject;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.HashMap;
import java.util.Arrays;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

/**
 * Created by Kris on 02-03-2017.
 */
public class EditorState implements IState {

    private InputController inputController;
    private World world;
    public double time = 0;
    private DynamicVector worldTarget;
    private DynamicVector worldTargetLastCreation = new DynamicVector(-1,-1); //Not to be true when comparing!
    private DynamicVector cameraPivot;
    private DynamicVector mousePanCameraPivot;
    private Vector objectMenuTarget;
    private Vector objectMenuSelected;
    private double moveDelayBase = 0.2;
    private double moveDelayFast = 0.05;
    private double[] moveDelays = new double[4];
    private PanType panType = PanType.keyboard;
    private double mousePanSpeedBase = 20;
    private double mousePanSpeedFast = 40;

    private final DynamicVector[] gridSizeBases = new DynamicVector[]{
            new DynamicVector(4,4),
            new DynamicVector(2,2),
            new DynamicVector(1,1),
            new DynamicVector(0.5,0.5),
            new DynamicVector(0.25,0.25)
    };

    private DynamicVector gridSize = new DynamicVector(1,1);
    private HashMap<EditorClassGroup,DynamicVector[]> gridSizes = new HashMap<EditorClassGroup,DynamicVector[]>(){{
        put(EditorClassGroup.blocks,Arrays.copyOfRange(gridSizeBases,0,3));
        put(EditorClassGroup.dynamics,Arrays.copyOfRange(gridSizeBases,0,gridSizeBases.length));
        put(EditorClassGroup.mobs,Arrays.copyOfRange(gridSizeBases,0,gridSizeBases.length));
        put(EditorClassGroup.decorations,new DynamicVector[]{new DynamicVector(0.5,0.5)});
    }};
    private EditorClass editorClassSelected;
    private EditorClassGroup editorClassGroup;

    private boolean gridEnabled = true;

    private double zoomScale = 2;

    private View view;

    private EditorMode mode;

    private int ObjTypesPerLine = 10;

    public double getZoomScale(){return zoomScale;}
    public DynamicVector getWorldTarget() {return worldTarget;}
    public double getTime() {return time;}
    public DynamicVector getGridSize() {return gridSize;}
    public Vector getObjectMenuTarget() {return objectMenuTarget;}
    public Vector getObjectMenuSelected(){return objectMenuSelected;}
    public DynamicVector getCameraPivot(){return cameraPivot;}
    public EditorMode getEditorMode(){return mode;}
    public boolean getGridEnabled(){return gridEnabled;}

    public EditorState(InputController inputController,View view)
    {
        this.inputController = inputController;
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
        gridSize = gridSizes.get(this.editorClassGroup)[gridSizes.get(this.editorClassGroup).length-1];
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
        if(!(WorldObject.class.isAssignableFrom(c)))throw new RuntimeException("Class used in createWorldObject, is not assignable from WorldObjects");
        try{

            WorldObject worldObject = null;

            if(Block.class.isAssignableFrom(c)) {
                Constructor ctor = c.getDeclaredConstructor(World.class, Vector.class);
                worldObject = (Block) ctor.newInstance(world, new Vector(pos));
            }
            else if(Decoration.class.isAssignableFrom(c)) {
                Constructor ctor = c.getDeclaredConstructor(World.class, Vector.class);
                worldObject = (Decoration) ctor.newInstance(world, new Vector(pos));
                System.out.println(((Decoration)worldObject).getPos().getX());
            }
            else if(DynamicObject.class.isAssignableFrom(c)) {
                Constructor ctor = c.getDeclaredConstructor(World.class, DynamicVector.class);
                worldObject = (DynamicObject) ctor.newInstance(world, pos);
            }
            if(worldObject == null) throw new RuntimeException("Class is assignable from WorldObjects but not supported by the createWorldObject method");

            world.addWorldObject(worldObject);
            return worldObject;

        }
        catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        throw new RuntimeException("Unexpected occurrence in createWorldObject");
    }

    public static void deleteObj(World world, Vector target, EditorClassGroup objTypeGroup)
    {
        switch(objTypeGroup)
        {
            case blocks:
            {
                world.deleteBlock(target,true);
            }
            case mobs:
            {
                world.deleteDynamicObjects(target);
            }
        }
    }

    public static void fillBlock(World world,Vector target,Class fillClass)
    {
        if(!Block.class.isAssignableFrom(fillClass)) throw new IllegalArgumentException("Class used in fillBlock is not an extension Blocks");
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
            if(!collectedVectors.contains(transTarget) && !world.outsideBlockBoundary(transTarget) && (world.getBlock(transTarget) == null ? c == null : world.getBlock(transTarget).getClass() == c))
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
    }

    public void endState()
    {

    }

    public void update(double delta)
    {
        time += delta;

        if(!Controller.consoleOpen())
        control(delta);
    }

    private void control(double delta)
    {
        zoomScale += 0.25*inputController.getMouseController().getMouseScrollDir();
        if(!Controller.debugging()){
            zoomScale = Math.min(4,Math.max(zoomScale,1));
        }

        Vector movement = getMovement(KeyCode.RIGHT,KeyCode.LEFT,KeyCode.UP,KeyCode.DOWN, KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.EditorFastPan,inputController)?moveDelayFast:moveDelayBase);
        DynamicVector mousePanDir = view.getPanDirectionVector(inputController.getMouseController().getMousePosition(),200,150);

        if(mode == EditorMode.World)
        {
            if((!inputController.getMouseController().getMouseMovement().equals(Vector.ZERO) || !mousePanDir.equals(Vector.ZERO))) {

                DynamicVector worldPos = view.getWorldPositionFromScreen(world,inputController.getMouseController().getMousePosition());
                if(gridEnabled)worldPos.set(Math.round(worldPos.getX_dyn()*(1/gridSize.getX_dyn()))*gridSize.getX_dyn(),Math.round(worldPos.getY_dyn()*(1/gridSize.getY_dyn()))*gridSize.getY_dyn());
                worldTarget.set(worldPos);
                worldTarget = getVectorInWorldBounds(worldTarget);

                panType = PanType.mouse;
            }
            else if (!movement.equals(Vector.ZERO)){
                moveWorldTarget(movement);
                panType = PanType.keyboard;
            }

            if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.SwitchGrid,inputController))
            {
                gridEnabled = !gridEnabled;
            }

            if(inputController.getKeyboardController().getKeyJustPressed(KeyCode.N))
            {
                world.addWorldObject(createWorldObject(Goal.class,world,worldTarget.add(0.0,0.5)));
            }

            if(inputController.getKeyboardController().getKeyJustPressed(KeyCode.Y))
            {
                DynamicVector t = worldTarget.multiply(1.0,2.0);
                System.out.println("TARGET : " + t + " | " + worldTarget);
                createWorldObject(Grass.class,world,t);
            }
            if(inputController.getKeyboardController().getKeyJustPressed(KeyCode.U))
            {
                DynamicVector t = worldTarget.multiply(1.0,2.0).add(0,0.5);
                System.out.println("TARGET : " + t + " | " + worldTarget);
                createWorldObject(Grass.class,world,t);
            }

            if(KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.EditorDelete,inputController))
            {
                deleteObj(world,worldTarget,editorClassGroup);
                view.setTargetEffect(true);
            }
            else if(KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.EditorCreate,inputController))
            {
                if(!worldTarget.equals(worldTargetLastCreation)){
                    view.setTargetEffect(false);
                    worldTargetLastCreation.set(worldTarget);

                    if(editorClassGroup == EditorClassGroup.decorations)
                    {
                        DynamicVector newTarget = worldTarget.multiply(1.0,2.0).add(0,1.0);
                        if(newTarget.getY_dyn()%2<0.5)newTarget.add(0.5,0);
                        createWorldObject(editorClassSelected.getClasss(),world,newTarget);
                    }
                    else
                    {
                        createWorldObject(editorClassSelected.getClasss(),world,worldTarget);
                    }
                }
            }
            else
            {
                view.setTargetEffect(false);
            }

            if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.EditorSetSpawn,inputController))
            {
                world.setPlayerSpawnPoint(worldTarget.getDynamicVector());
            }
            if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.EditorFill,inputController))
            {
                if(Block.class.isAssignableFrom(editorClassSelected.getClasss())){
                    fillBlock(world,worldTarget,editorClassSelected.getClasss());
                }
            }
            /*
            if((keyboardController.getKeyPressed(KeyCode.V)))
            {
                Detail detail = new Detail(new DynamicVector(getWorldTarget()).add(0,0.5), ImageLibrary.getImage("Grasstop.png"));
                world.addDetail(detail);
            }*/
        }
        else if(mode == EditorMode.ObjectSelect)
        {
            moveObjectMenuTarget(movement);

            if(KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.EditorCreate,inputController))
            {
                changeObjTypeSelected(objectMenuTarget.getX()+objectMenuTarget.getY()*ObjTypesPerLine);
                menuObjectSelect(objectMenuTarget.getX(),objectMenuTarget.getY());
            }
            if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.EditorSwitchObjectGroup,inputController))
            {
                if(editorClassGroup==EditorClassGroup.blocks){
                    changeObjTypeGroup(EditorClassGroup.dynamics);
                }
                else if(editorClassGroup==EditorClassGroup.dynamics){
                    changeObjTypeGroup(EditorClassGroup.mobs);
                }
                else if(editorClassGroup==EditorClassGroup.mobs){
                    changeObjTypeGroup(EditorClassGroup.decorations);
                }
                else if(editorClassGroup==EditorClassGroup.decorations){
                    changeObjTypeGroup(EditorClassGroup.blocks);
                }

            }
        }

        if(panType == PanType.keyboard) {
            cameraPivot.setAdd(new DynamicVector(getWorldTarget().getX() - cameraPivot.getX_dyn(), getWorldTarget().getY() - cameraPivot.getY_dyn()).multiply(8 * delta));
        }
        else if(panType == PanType.mouse){

            double speed = KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.EditorFastPan,inputController) ? mousePanSpeedFast : mousePanSpeedBase;
            DynamicVector cameraPivotAdd = new DynamicVector(mousePanDir.getX_dyn() * delta * speed, mousePanDir.getY_dyn() * delta * speed);
            cameraPivot.setAdd(cameraPivotAdd);
            cameraPivot = view.getMinimumCornerCenterVector(world, cameraPivot);
        }

        if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.EditorObjectSelect,inputController))
        {
            mode = mode==EditorMode.ObjectSelect?EditorMode.World:EditorMode.ObjectSelect;
        }
    }

    private DynamicVector getVectorInWorldBounds(DynamicVector dynamicVector)
    {
        dynamicVector = new DynamicVector(dynamicVector);

        if(dynamicVector.getX_dyn() < 0){dynamicVector.setX_dyn(0);}
        else if(dynamicVector.getX_dyn() >= world.getWorldWidth()){dynamicVector.setX_dyn(world.getWorldWidth()-1);}

        if(dynamicVector.getY() < 0){dynamicVector.setY(0);}
        else if(dynamicVector.getY_dyn() >= world.getWorldHeight()){dynamicVector.setY_dyn(world.getWorldHeight()-1);}

        return dynamicVector;
    }

    private void moveWorldTarget(Vector movement)
    {
        worldTarget.set(Math.min(world.getWorldWidth()-1,Math.max(0,roundScaled(worldTarget.getX_dyn()+gridSize.getX_dyn()*movement.getX_dyn(),gridSize.getX_dyn()))),
                Math.min(world.getWorldHeight()-1,Math.max(0,roundScaled(worldTarget.getY_dyn()+gridSize.getY_dyn()*movement.getY_dyn(),gridSize.getY_dyn()))));
    }

    private double roundScaled(double value,double scale)
    {
        return Math.round(value*(1/scale))*scale;
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
        if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.Right,inputController) || (KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Right,inputController) && time >= moveDelays[0]+delay))
        {
            movement.setX(1);
            moveDelays[0] = time;
        }
        else if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.Left,inputController) || (KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Left,inputController) && time >= moveDelays[1]+delay))
        {
            movement.setX(-1);
            moveDelays[1] = time;
        }
        if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.Up,inputController) || (KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Up,inputController) && time >= moveDelays[2]+delay))
        {
            movement.setY(1);
            moveDelays[2] = time;
        }
        else if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.Down,inputController) || (KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Down,inputController) && time >= moveDelays[3]+delay))
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
