package World;

import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import Vectors.Vector;
import World.Background.BackgroundElement;
import World.WorldObject.Block.Block;
import World.WorldObject.Block.BlockedDirs;
import World.WorldObject.Block.BlockedOrientation;
import World.WorldObject.Block.DirtBlock;
import World.WorldObject.DynamicObject.DynamicObject;
import World.WorldObject.DynamicObject.PhysicObject.Mob.Player;
import World.WorldObject.WorldObject;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kris on 19-02-2017.
 */
public class World implements Serializable {

    private Color skyColor = new Color(0.3,0.6,1,1); //new Color(0.88,0.4,1,1);
    private ArrayList<BackgroundElement> backgroundElements;
    private ArrayList<WorldObject> worldObjects;
    private Block[][] blocks;
    private ArrayList<DynamicObject> dynamicObjects;
    private int worldHeight;
    private int worldWidth;
    private double gravity;
    private DynamicVector playerSpawnPoint;
    public ParticleSystem rain;

    private double windForce = 0.25;

    transient private Player player;

    public World(int width,int height)
    {
        worldHeight = height;
        worldWidth = width;
        worldObjects = new ArrayList<>();
        blocks = new Block[width][height];
        dynamicObjects = new ArrayList<>();
        gravity = 9.82*2;
        playerSpawnPoint = new DynamicVector(4,4);
        backgroundElements = new ArrayList<BackgroundElement>(){{
            add(new BackgroundElement(ImageLibrary.getImage("Hills.png"), new DynamicVector(0, 0), new DynamicVector(0.08, 0.05), false));
            add(new BackgroundElement(ImageLibrary.getImage("Ocean.png"), new DynamicVector(0, 0), new DynamicVector(0.1, 0.06), false));
        }};

        rain = new ParticleSystem(new DynamicVector(0,1),new DynamicVector(1,1),100,new DynamicVector(-0.125,-0.75));
    }

    public void init(){
        for(WorldObject worldObject : worldObjects)
        {
            worldObject.init();
            worldObject.reset();
        }
    }

    public double getWindForce(){return windForce;}

    public void reset(){
        ArrayList<WorldObject> temporaryWorldObjects = new ArrayList<>();
        for(WorldObject obj : getWorldObjects())
        {
            if(obj.isTemporary()){temporaryWorldObjects.add(obj); continue;}
            obj.reset();
        }
        for(WorldObject obj :temporaryWorldObjects)
        {
            deleteWorldObject(obj);
        }
        rain.reset();
    }

    public void endInit()
    {
        updateImageOnAllBlocks();
    }

    public DynamicVector getPlayerSpawnPoint(){return playerSpawnPoint;}

    public void setGravity(double d){gravity = d;}

    public double getGravity(){return gravity;}

    public Color getSkyColor(){return skyColor;}

    public ArrayList<BackgroundElement> getBackgroundElements(){return backgroundElements;}

    public void removeBackgroundElement(int index){backgroundElements.remove(index);}

    public void addBackgroundElement(BackgroundElement backgroundElement){backgroundElements.add(backgroundElement);}
    public void addBackgroundElement(BackgroundElement backgroundElement,int index){backgroundElements.add(index,backgroundElement);}

    public int getWorldHeight(){
        return worldHeight;
    }

    public int getWorldWidth(){
        return worldWidth;
    }

    public boolean checkIfEmpty(Vector pos){return checkIfEmpty(pos.getX(),pos.getY());}
    public boolean checkIfEmpty(int x,int y)
    {
        if(x < 0 || y < 0 || x > worldWidth-1 || y > worldHeight-1){return true;}
        return blocks[x][y] == null;
    }

    public void addWorldObject(WorldObject worldObject){

        if(worldObject instanceof Block){
            addBlock((Block)worldObject);
        }
        else if(worldObject instanceof DynamicObject)
        {
            addDynamicObject((DynamicObject)worldObject);
        }
    }

    public void setPlayerSpawnPoint(DynamicVector vector)
    {
        playerSpawnPoint= new DynamicVector(vector);
    }

    public boolean addBlock(Block block){
        if(checkIfEmpty(block.getPos())) {
            blocks[block.getPos().getX()][block.getPos().getY()] = block;
            worldObjects.add(block);
            return true;
        }
        return false;
    }

    public boolean addBlock(Block block, boolean updateImages)
    {
        boolean succes = addBlock(block);
        if(succes && updateImages)
        {
            updateImageOnBlockCluster(block.getPos());
        }
        return succes;
    }

    public void deleteWorldObject(WorldObject worldObject){
        if(worldObject instanceof Block){
            deleteBlock((Block)worldObject);
        }
        else if(worldObject instanceof DynamicObject)
        {
            deleteDynamicObject((DynamicObject)worldObject);
        }
    }

    public void deleteBlock(Block block){deleteBlock(block.getPos());}

    public void deleteBlock(Vector pos){
        if(!checkIfEmpty(pos.getX(),pos.getY())){
            worldObjects.remove(blocks[pos.getX()][pos.getY()]);
            blocks[pos.getX()][pos.getY()] = null;
            updateImageOnBlockCluster(pos);
            //throw new RuntimeException("A block a was attempted deleted at: " + pos.toString() + ", but was not found in the blocks 2D-array!");
        }
    }

    public void addPlayer(Player player)
    {
        this.player = player;
        addDynamicObject(player);
    }

    public void addDynamicObject(DynamicObject dynamicObject) {
        worldObjects.add(dynamicObject);
        dynamicObjects.add(dynamicObject);
        //dynamicObject.init();
        //dynamicObject.reset();
    }

    public void deleteDynamicObjects(Vector pos)
    {
        ArrayList<DynamicObject> dyns = new ArrayList<>();
        for(DynamicObject dyn : dynamicObjects){
            if(dyn.getPos().dist(pos) < 1)
            {
                dyns.add(dyn);
            }
        }
        for(DynamicObject dyn : dyns)
        {
            deleteDynamicObject(dyn);
        }
    }

    public void deleteDynamicObject(DynamicObject dynamicObject)
    {
        dynamicObjects.remove(dynamicObject);
        worldObjects.remove(dynamicObject);
    }

    public ArrayList<WorldObject> getWorldObjects(){
        return worldObjects;
    }

    public Block[][] getBlocks(){
        return blocks;
    }

    public ArrayList<DynamicObject> getDynamicObjects(){return dynamicObjects;}

    public BlockedDirs getBlockedDirs(Vector pos) {
        HashMap<Dir,Block> blockedDirs = new HashMap<Dir, Block>();
        for(Dir dir : Dir.getValues()) {
            Vector target = new Vector(pos.getX()+dir.getVector().getX(),pos.getY()+dir.getVector().getY());
            blockedDirs.put(dir,checkIfEmpty(target)?null:blocks[target.getX()][target.getY()]);
        }
        return new BlockedDirs(blockedDirs);
    }

    public BlockedOrientation getBlockedOrientation(Block block,BlockedDirs blockedDirs) {
        boolean up = sameBlockType(block, blockedDirs.getBlock(Dir.Up));
        boolean left = sameBlockType(block, blockedDirs.getBlock(Dir.Left));
        boolean right = sameBlockType(block, blockedDirs.getBlock(Dir.Right));
        boolean down = sameBlockType(block, blockedDirs.getBlock(Dir.Down));
        return new BlockedOrientation(right,down,left,up);
    }

    public void updateImageOnBlockCluster(Vector blockPos)
    {
        if(!checkIfEmpty(blockPos)) {
            blocks[blockPos.getX()][blockPos.getY()].updateImage();
        }
        for(Dir dir : Dir.getValues())
        {
            int x = blockPos.getX()+dir.getVector().getX();
            int y = blockPos.getY()+dir.getVector().getY();
            if(!checkIfEmpty(x,y))
            {
                blocks[x][y].updateImage();
            }
        }
    }

    public void updateImageOnAllBlocks()
    {
        for(Block[] blockRow : blocks)
        {
            for(Block block : blockRow)
            {
                if(block != null) {
                    block.updateImage();
                }
            }
        }
    }

    private boolean sameBlockType(Block block, Block target)
    {
        if(target == null) {
            return false;
        }
        if(target.getClass().equals(block.getClass())) {
            return true;
        }
        return false;
    }

    public void fillEdges(int depth)
    {
        for(int i=0;i<getWorldWidth();i++){
            addBlock(new DirtBlock(this,new Vector(i,0)));
            addBlock(new DirtBlock(this,new Vector(i,getWorldHeight()-1)));
        }
        for(int i=0;i<getWorldHeight();i++){
            addBlock(new DirtBlock(this,new Vector(0,i)));
            addBlock(new DirtBlock(this,new Vector(getWorldWidth()-1,i)));
        }
    }

    public Player getPlayerTarget(DynamicObject obj)
    {
        return player;
    }

    private int booleanToInt(boolean b){return b?1:0;}

}
