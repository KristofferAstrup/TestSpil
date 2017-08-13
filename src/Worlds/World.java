package Worlds;

import Vectors.DynamicVector;
import Vectors.Vector;
import Worlds.Backgrounds.BackgroundElement;
import Worlds.ParticleSystems.GlobalParticleSystem;
import Worlds.ParticleSystems.ImageParticleSystem;
import Worlds.WorldObjects.Blocks.Block;
import Worlds.WorldObjects.Blocks.BlockedDirs;
import Worlds.WorldObjects.Blocks.BlockedOrientation;
import Worlds.WorldObjects.Blocks.DirtBlock;
import Worlds.WorldObjects.Decorations.Decoration;
import Worlds.WorldObjects.Decorations.DecorationFace;
import Worlds.WorldObjects.DynamicObjects.DynamicObject;
import Worlds.WorldObjects.DynamicObjects.Goal;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Mob;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Player;
import Worlds.WorldObjects.WorldObject;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kris on 19-02-2017.
 */
public class World implements Serializable {

    private ColorWrapper skyColor = new ColorWrapper(0.3,0.6,1,1); //new Color(0.88,0.4,1,1);
    private ArrayList<BackgroundElement> backgroundElements;
    private ArrayList<WorldObject> worldObjects;
    private Block[][] blocks;
    private Decoration[][] decorations;
    private ArrayList<DynamicObject> dynamicObjects;
    private ArrayList<Mob> mobs;
    private int worldHeight;
    private int worldWidth;
    private double gravity;
    private DynamicVector playerSpawnPoint;
    private ArrayList<GlobalParticleSystem> globalParticleSystems;
    private ImageParticleSystem imageParticleSystem;
    private ArrayList<Detail> details;
    private Goal goal;

    private double windForce = 0.25;

    transient private Player player;

    public World(int width,int height)
    {
        worldHeight = height;
        worldWidth = width;
        worldObjects = new ArrayList<>();
        blocks = new Block[width][height];
        decorations = new Decoration[width+1][height*2+1];
        dynamicObjects = new ArrayList<>();
        mobs = new ArrayList<>();
        gravity = 9.82*2;
        details = new ArrayList<>();
        playerSpawnPoint = new DynamicVector(4,4);
        backgroundElements = new ArrayList<BackgroundElement>(){{
            add(new BackgroundElement("Hills.png", new DynamicVector(0, 0), new DynamicVector(0.08, 0.05), false));
            add(new BackgroundElement("Ocean.png", new DynamicVector(0, 0), new DynamicVector(0.1, 0.06), false));
        }};

        globalParticleSystems = new ArrayList<>();
        globalParticleSystems.add(new GlobalParticleSystem(this,new DynamicVector(0,1),new DynamicVector(1,1),100,new DynamicVector(-0.125,-0.75)));
        imageParticleSystem = new ImageParticleSystem(this,new DynamicVector(0,-gravity));
    }

    public World(World world)
    {
        worldHeight = world.worldHeight;
        worldWidth = world.worldWidth;
        worldObjects = new ArrayList<>(world.worldObjects);
        blocks = new Block[worldWidth][worldHeight];
        for(int x=0;x<worldWidth;x++)
        {
            for(int y=0;y<worldHeight;y++)
            {
                blocks[x][y] = world.blocks[x][y];
                if(blocks[x][y]!=null)blocks[x][y].setWorld(this);
            }
        }
        decorations = new Decoration[worldWidth+1][worldHeight*2+1];
        for(int x=0;x<worldWidth+1;x++)
        {
            for(int y=0;y<worldHeight*2+1;y++)
            {
                decorations[x][y] = world.decorations[x][y];
                if(decorations[x][y]!=null)decorations[x][y].setWorld(this);
            }
        }
        dynamicObjects = new ArrayList<>(world.dynamicObjects);
        for(DynamicObject dynamicObject : dynamicObjects)
        {
            dynamicObject.setWorld(this);
        }
        mobs = new ArrayList<>(world.mobs);
        details = new ArrayList<>(world.details);
        gravity = world.gravity;
        playerSpawnPoint = world.playerSpawnPoint;
        backgroundElements = new ArrayList<>(world.backgroundElements);
        globalParticleSystems = new ArrayList<>(world.getGlobalParticleSystems());
        for(GlobalParticleSystem globalParticleSystem : globalParticleSystems)
        {
            globalParticleSystem.setWorld(this);
        }
        imageParticleSystem = world.getImageParticleSystem();
    }

    public void init(){
        skyColor.init();
        for(BackgroundElement backgroundElement : backgroundElements)
        {
            backgroundElement.init();
        }
        for(WorldObject worldObject : worldObjects)
        {
            worldObject.init();
            worldObject.reset();
        }
        for(GlobalParticleSystem globalParticleSystem : globalParticleSystems)
        {
            globalParticleSystem.init();
        }
        System.out.println("WORLD INIT");
    }

    public void worldStart()
    {
        for(WorldObject worldObject : worldObjects)
        {
            worldObject.worldStart();
        }
    }

    public ArrayList<GlobalParticleSystem> getGlobalParticleSystems(){return globalParticleSystems;}

    public ImageParticleSystem getImageParticleSystem(){return imageParticleSystem;}

    public double getWindForce(){return windForce;}

    public void reset(){
        ArrayList<WorldObject> temporaryWorldObjects = new ArrayList<>();
        for(WorldObject obj : getWorldObjects())
        {
            obj.reset();
        }
        for(GlobalParticleSystem globalParticleSystem : globalParticleSystems)
        {
            globalParticleSystem.reset();
        }
        imageParticleSystem.reset();
        System.out.println("WORLD RESET");
    }

    public void endInit()
    {
        updateImageOnAllBlocks();
    }

    public DynamicVector getPlayerSpawnPoint(){return playerSpawnPoint;}

    public void setGravity(double d){gravity = d;}

    public double getGravity(){return gravity;}

    public ArrayList<Detail> getDetails(){return details;}

    public Color getSkyColor(){return skyColor.getColor();}

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

    public boolean checkIfEmptyBlock(Vector pos){return checkIfEmptyBlock(pos.getX(),pos.getY());} //FEJL HER, NULLPOINTER
    public boolean checkIfEmptyBlock(int x, int y)
    {
        if(outsideBlockBoundary(x,y)){return true;}
        return blocks[x][y] == null || blocks[x][y].isDestroyed();
    }

    public boolean checkIfEmptyDecoration(Vector pos){
        System.out.println(decorations.length + ":" + decorations[0].length);
        return checkIfEmptyDecoration(pos.getX(),pos.getY());
    }
    public boolean checkIfEmptyDecoration(int x, int y)
    {
        if(outsideDecorationBoundary(x,y)){return true;}
        return decorations[x][y] == null || decorations[x][y].isDestroyed();
    }

    public void addWorldObject(WorldObject worldObject){

        boolean success = false;

        if(worldObject instanceof Block)
        {
            success = addBlock((Block)worldObject,true);
        }
        else if(worldObject instanceof Decoration)
        {
            success = addDecoration((Decoration)worldObject);
        }
        else if(worldObject instanceof DynamicObject)
        {
            addDynamicObject((DynamicObject)worldObject);

            if(worldObject instanceof Goal)
            {
                if(goal != null){deleteDynamicObject(goal);}
                goal = (Goal)worldObject;
            }

            success = true;
        }

        if(success){
            System.out.println("Added WorldObjects of type: " + worldObject.getClass().getName());
        }
    }

    public void addDetail(Detail detail)
    {
        details.add(detail);
    }

    public void deleteDetail(Detail detail)
    {
        details.remove(detail);
    }

    public boolean outsideBlockBoundary(Vector pos){
        return outsideBlockBoundary(pos.getX(),pos.getY());
    }

    public boolean outsideBlockBoundary(int x, int y){
        return x < 0 || y < 0 || x > worldWidth-1 || y > worldHeight-1;
    }

    public boolean outsideDecorationBoundary(int x,int y){
        return x < 0 || y < 0 || x > worldWidth+1-1 || y > worldHeight*2+1-1;
    }

    public void setPlayerSpawnPoint(DynamicVector vector)
    {
        playerSpawnPoint= new DynamicVector(vector);
    }

    public boolean addBlock(Block block){
        if(checkIfEmptyBlock(block.getPos())) {
            blocks[block.getPos().getX()][block.getPos().getY()] = block;
            worldObjects.add(block);

            updateDecorations(block.getPos());

            block.init();
            block.reset();
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

    public boolean addDecoration(Decoration decoration){
        if(checkIfEmptyDecoration(decoration.getPos()) && getFace(decoration.getPos().getDynamicVector()).face != DecorationFace.Face.None) {
            decorations[decoration.getPos().getX()][decoration.getPos().getY()] = decoration;
            worldObjects.add(decoration);
            decoration.updateImage();
            decoration.init();
            decoration.reset();
            return true;
        }
        return false;
    }

    public void deleteDecoration(Vector pos)
    {
        deleteDecoration(pos,true);
    }

    public void deleteDecoration(Decoration decoration)
    {
        deleteDecoration(decoration.getPos(),true);
    }

    public void deleteDecoration(Decoration decoration,boolean updateImages)
    {
        deleteDecoration(decoration.getPos(),updateImages);
    }

    public void deleteDecoration(Vector pos,boolean updateImages)
    {
        if(!checkIfEmptyDecoration(pos.getX(),pos.getY())){
            worldObjects.remove(decorations[pos.getX()][pos.getY()]);
            decorations[pos.getX()][pos.getY()].die();
            decorations[pos.getX()][pos.getY()] = null;
        }
    }

    public void deleteWorldObject(WorldObject worldObject){
        if(worldObject instanceof Block){
            deleteBlock((Block)worldObject);
        }
        else if(worldObject instanceof DynamicObject)
        {
            deleteDynamicObject((DynamicObject)worldObject);
        }
        else
        {
            throw new RuntimeException("The attempted deleted WorldObjects is instance of any recognized class!");
        }
    }

    public void deleteBlock(Block block){deleteBlock(block,false);}

    public void deleteBlock(Block block,boolean updateImage){deleteBlock(block.getPos(),updateImage);}

    public void deleteBlock(Vector pos){
        deleteBlock(pos,false);
    }

    public void deleteBlock(Vector pos,boolean updateImage){
        if(!checkIfEmptyBlock(pos.getX(),pos.getY())){
            worldObjects.remove(blocks[pos.getX()][pos.getY()]);
            blocks[pos.getX()][pos.getY()].die();
            blocks[pos.getX()][pos.getY()] = null;

            updateDecorations(pos);

            if(updateImage)updateImageOnBlockCluster(pos);
            //throw new RuntimeException("A block a was attempted deleted at: " + pos.toString() + ", but was not found in the blocks 2D-array!");
        }
    }

    public void updateDecorations(Vector blockPos)
    {
        Vector decorationPos = blockPos.multiply(1,2);
        updateDecoration(decorationPos); //Down
        updateDecoration(decorationPos.add(0,2)); //Up
        updateDecoration(decorationPos.add(0,1)); //Left
        updateDecoration(decorationPos.add(1,1)); //Right
    }

    private void updateDecoration(Vector decorationPos)
    {
        Decoration decoration = decorations[decorationPos.getX()][decorationPos.getY()];
        if(decoration == null)return;
        updateDecoration(decoration);
    }

    private void updateDecoration(Decoration decoration)
    {
        DecorationFace decorationFace = getFace(decoration.getPos());
        if(decorationFace.face == DecorationFace.Face.None){
            deleteDecoration(decoration);
        } else {
            decoration.updateImage();
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
        if(dynamicObject instanceof Mob){
            mobs.add((Mob)dynamicObject);
        }
        dynamicObject.init();
        dynamicObject.reset();
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
        if(dynamicObject instanceof Mob){
            mobs.remove(dynamicObject);
        }
    }

    public ArrayList<WorldObject> getWorldObjects(){
        return worldObjects;
    }

    public Block[][] getBlocks(){
        return blocks;
    }

    public Decoration[][] getDecorations(){return decorations;}

    public Block getBlock(Vector v){
        return getBlock(v.getX(),v.getY());
    }

    public Block getBlock(int x,int y){
        return blocks[x][y];
    }

    public ArrayList<DynamicObject> getDynamicObjects(){return dynamicObjects;}

    public BlockedDirs getBlockedDirs(Vector pos) {
        HashMap<Dir,Block> blockedDirs = new HashMap<Dir, Block>();
        for(Dir dir : Dir.getValues()) {
            Vector target = new Vector(pos.getX()+dir.getVector().getX(),pos.getY()+dir.getVector().getY());
            blockedDirs.put(dir, checkIfEmptyBlock(target)?null:blocks[target.getX()][target.getY()]);
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
        if(!checkIfEmptyBlock(blockPos)) {
            blocks[blockPos.getX()][blockPos.getY()].updateImage();
        }
        for(Dir dir : Dir.getValues())
        {
            int x = blockPos.getX()+dir.getVector().getX();
            int y = blockPos.getY()+dir.getVector().getY();
            if(!checkIfEmptyBlock(x,y))
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

    public DecorationFace getFace(Vector pos){

        DecorationFace decorationFace = new DecorationFace();

        if(pos.getY()%2==0) //Horizontal
        {
            if(checkIfEmptyBlock(pos.getX(),pos.getY()/2)){
                if(checkIfEmptyBlock(pos.getX(),pos.getY()/2-1)) {

                    decorationFace.face = DecorationFace.Face.None;
                    decorationFace.position = null;

                } else {

                    decorationFace.face = DecorationFace.Face.Up;
                    decorationFace.position = getDecorationFacePosition(pos,1,0, XY.x);
                }
            }
            else if(checkIfEmptyBlock(pos.getX(),pos.getY()/2-1)){

                decorationFace.face = DecorationFace.Face.Down;
                decorationFace.position = getDecorationFacePosition(pos,1,1, XY.x);

            }
            else
            {
                decorationFace.face = DecorationFace.Face.Horizontal;
                DecorationFace.Position posUpper = getDecorationFacePosition(pos,1,1, XY.x); //Ordinalet (nr) i Position Enumet svarer til hvor mange blokke der er nær decorationen, dvs. den vælger den med færrest hvilket oversætter til en prioritering af Solo>Left>Right>Center
                DecorationFace.Position posLower = getDecorationFacePosition(pos,1,0, XY.x);
                decorationFace.position = posUpper.ordinal() > posLower.ordinal() ? posUpper : posLower;
            }
        }
        else                //Vertical
        {
            if(checkIfEmptyBlock(pos.getX(),(pos.getY()-1)/2)){
                if(checkIfEmptyBlock(pos.getX()-1,(pos.getY()-1)/2)) {

                    decorationFace.face = DecorationFace.Face.None;
                    decorationFace.position = null;

                } else {

                    decorationFace.face = DecorationFace.Face.Right;
                    decorationFace.position = getDecorationFacePosition(pos,0,1, XY.y);
                }
            }
            else if(checkIfEmptyBlock(pos.getX()-1,(pos.getY()-1)/2)){

                decorationFace.face = DecorationFace.Face.Left;
                decorationFace.position = getDecorationFacePosition(pos,-1,1, XY.x);

            }
            else
            {
                decorationFace.face = DecorationFace.Face.Vertical;
                DecorationFace.Position posUpper = getDecorationFacePosition(pos,-1,1, XY.y); //Ordinalet (nr) i Position Enumet svarer til hvor mange blokke der er nær decorationen, dvs. den vælger den med færrest hvilket oversætter til en prioritering af Solo>Left>Right>Center
                DecorationFace.Position posLower = getDecorationFacePosition(pos,0,1, XY.y);
                decorationFace.position = posUpper.ordinal() > posLower.ordinal() ? posUpper : posLower;
            }
        }

        if(decorationFace.face == null)
        {
            throw new RuntimeException("Was unable to find a matching DecorationFace!");
        }

        return decorationFace;
    }

    private DecorationFace.Position getDecorationFacePosition(Vector pos,int dx,int dy,XY xy)
    {
        int y = (pos.getY()-(xy==XY.y?1:0))/2;
        int x = pos.getX();
        int rdx = (xy==XY.x?-dx:dx);
        int rdy = (xy==XY.y?-dy:dy);
        if(checkIfEmptyBlock(x+dx+dy,y+dx+dy))
        {
            if(checkIfEmptyBlock(x+rdx+rdy,y+rdx+rdy))
            {
                return DecorationFace.Position.Solo;
            }
            else
            {
                return DecorationFace.Position.Right;
            }
        }
        else if(checkIfEmptyBlock(x+rdx+rdy,y+rdx+rdy))
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

    public void destroyWorldObject(WorldObject worldObject)
    {
        worldObject.setDestroyed(true);
        if(worldObject instanceof Block){
            deleteBlock((Block)worldObject);
        }
        worldObject.delete();
    }

    public Mob getClosestMob(DynamicVector pos,double maxDist){
        double closestDist = Double.POSITIVE_INFINITY;
        double dist;
        Mob closestMob = null;
        for(Mob mob : mobs){
            if(!mob.getAlive())continue;
            dist = mob.getPos().dist(pos) - mob.getSize().getX_dyn()/2;
            if(dist < closestDist){
                closestDist = dist;
                closestMob = mob;
            }
        }
        if(closestDist > maxDist){return null;}
        return closestMob;
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
