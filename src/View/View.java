package View;

import Controller.Controller;
import Factories.ObjType;
import Libraries.EditorClass;
import Libraries.ImageLibrary;
import Libraries.EditorLibrary;
import State.EditorState.EditorMode;
import State.EditorState.EditorState;
import State.GameState.GameState;
import State.IState;
import Vectors.DynamicVector;
import Vectors.Vector;
import World.Background.BackgroundElement;
import World.ParticleSystem.GlobalParticleSystem;
import World.ParticleSystem.ImageParticleSystem;
import World.World;
import World.Detail;
import World.WorldObject.DynamicObject.DynamicObject;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.*;

import java.util.ArrayList;

import static javafx.application.Application.launch;

public class View {

    Vector canvasDim;
    Canvas canvas;
    Scene scene;
    Stage stage;
    GraphicsContext gc;
    double objectSizeBase = 16;
    double objectSize;
    DynamicVector cameraPan = new DynamicVector(0,0);
    DynamicVector cameraPanBoundaries;
    double winScale;
    ArrayList<DebugGroup> debugGroups = new ArrayList<>();
    boolean debugGroupsVisible = false;

    String[] paths;
    Group root = new Group();

    public View(Stage theStage)
    {
        theStage.setTitle("TS");
        stage = theStage;

        scene = new Scene( root );

        stage.setScene( scene );

        canvasDim = new Vector(900,600);
        canvas = new Canvas( canvasDim.getX(), canvasDim.getY() );

        root.getChildren().add( canvas );
        stage.setWidth(canvasDim.getX()+16);
        stage.setHeight(canvasDim.getY()+40);

        gc = canvas.getGraphicsContext2D();
        //paths = SaveLoadController.getFilePathList();
    }

    public Scene getScene()
    {
        return scene;
    }

    public void addDebugGroup(DebugGroup debugGroup)
    {
        debugGroup.updateLayout(canvasDim);
        debugGroups.add(debugGroup);
        root.getChildren().add(debugGroup);
    }

    public void setVisibleDebugGroup(boolean visible)
    {
        debugGroupsVisible = visible;
        for(DebugGroup debugGroup : debugGroups) {
            debugGroup.setVisible(debugGroupsVisible);
        }
    }

    public boolean getVisibleDebugGroup(){return debugGroupsVisible;}

    public Vector getCanvasDim(){return canvasDim;}

    public Vector getWorldPositionFromScreen(World world, Vector screenPos)
    {
        return new DynamicVector(Math.round((-cameraPan.getX_dyn()+screenPos.getX())/objectSize),cameraPan.getY_dyn()/objectSize+world.getWorldHeight()-screenPos.getY()/objectSize-1);
    }

    public void update(IState state)
    {

        gc.clearRect(0,0,canvasDim.getX(),canvasDim.getY());
        gc.save();
        if(state.getClass() == GameState.class)
        {
            drawGameState((GameState)state);
        }
        else if(state.getClass() == EditorState.class)
        {
            drawEditorState((EditorState)state);
        }
        gc.restore();
        stage.show();

        /*for(int i=0;i<paths.length;i++)
        {
            gc.fillText(paths[i],25,25+i*25);
        }*/
    }

    private void drawGameState(GameState gameState)
    {
        setWinScale(2,gameState.getWorld());
        panCamera(gameState.getWorld(),gameState.getPlayer().getPos());
        drawSky(gameState.getWorld());
        drawBackground(gameState.getWorld());
        drawBlocks(gameState.getWorld());
        drawDynamics(gameState.getWorld());
        drawGlobalParticleSystem(gameState.getWorld());
        drawImageParticleSystem(gameState.getWorld());
        drawDetails(gameState.getWorld());

        gc.setTransform(new Affine());
        gc.setStroke(Color.LIGHTBLUE);
        gc.setGlobalAlpha(0.6);

        for(GlobalParticleSystem globalParticleSystem : gameState.getWorld().getGlobalParticleSystems())
            for(DynamicVector dynamicVector : globalParticleSystem.getParticles())
            {
                double x = dynamicVector.getX_dyn()*canvasDim.getX();
                double y = canvasDim.getY()-dynamicVector.getY_dyn()*canvasDim.getY();
                gc.strokeLine(x,y,
                        x-globalParticleSystem.getSpeed().getX_dyn()*25,
                        y+globalParticleSystem.getSpeed().getY_dyn()*25);
            }

    }

    private void drawEditorState(EditorState editorState) {
        setWinScale(editorState.getZoomScale(),editorState.getWorld());
        panCamera(editorState.getWorld(), editorState.getCameraPivot());
        //panCamera(editorState.getWorld(),new DynamicVector(0,-editorState.getWorld().getWorldHeight()));
        drawGrid(editorState.getWorld());
        drawBlocks(editorState.getWorld());
        drawDynamics(editorState.getWorld());
        drawDetails(editorState.getWorld());

        drawTarget(editorState.getWorld(), editorState.getWorldTarget(),editorState.getObjTypeSelected());
        drawSpawn(editorState.getWorld());

        if(Controller.debugging()){
            drawDebug();
        }

        if (editorState.getEditorMode() == EditorMode.ObjectSelect) {
            drawTileWindow(10, 4, editorState.getObjectMenuTarget(), editorState.getObjectMenuSelected(),
                    EditorLibrary.getEditorClasses(editorState.getObjTypeGroup()));
        }
    }

    public void setWinScale(double winScale,World world)
    {
        this.winScale = winScale;
        objectSize = objectSizeBase * winScale;
        cameraPanBoundaries = new DynamicVector((world.getWorldWidth())*objectSize-canvasDim.getX(),(world.getWorldHeight())*objectSize-canvasDim.getY());
    }

    public DynamicVector getPanDirectionVector(DynamicVector mousePosition,int horizontalZone,int verticalZone)
    {
        DynamicVector dir = new DynamicVector(0,0);
        if(mousePosition.getX() <= horizontalZone){
            dir.setX_dyn((mousePosition.getX_dyn()/horizontalZone)-1);
        }
        else if(mousePosition.getX() >= canvasDim.getX()-horizontalZone){
            dir.setX_dyn((mousePosition.getX_dyn()-(canvasDim.getX()-horizontalZone))/horizontalZone);
        }
        if(mousePosition.getY() <= verticalZone){
            dir.setY_dyn(1-mousePosition.getY_dyn()/verticalZone);
        }
        else if(mousePosition.getY() >= canvasDim.getY()-verticalZone){
            dir.setY_dyn(-((mousePosition.getY_dyn()-(canvasDim.getY()-verticalZone))/verticalZone));
        }
        return dir;
    }

    public DynamicVector getMinimumCornerCenterVector(World world,DynamicVector position)
    {
        return new DynamicVector(Math.min(world.getWorldWidth()-canvasDim.getX()/(2*objectSize),Math.max(position.getX_dyn(),canvasDim.getX()/(2*objectSize))),
                Math.min(world.getWorldHeight()-canvasDim.getY()/(2*objectSize),Math.max(position.getY_dyn(),canvasDim.getY()/(2*objectSize))));
    }

    private void drawBlocks(World world)
    {
        gc.setFill(Color.BLACK);
        for(int x=0;x<world.getBlocks().length;x++){
            for(int y=0;y<world.getBlocks()[x].length;y++){
                if(world.getBlocks()[x][y] != null)
                {
                    Image img = world.getBlocks()[x][y].getImage();
                    double x_pos = x*objectSize;//-objectSize/2;
                    double y_pos = (world.getBlocks()[x].length-y-1)*objectSize;//-objectSize/2;
                    Affine affine = new Affine();
                    affine.appendTranslation(x_pos + cameraPan.getX_dyn(),y_pos + cameraPan.getY_dyn());
                    affine.appendRotation(world.getBlocks()[x][y].getRot(),0,0);
                    gc.setTransform(affine);
                    double width = img.getWidth()*winScale/ImageLibrary.imageLoadScale;
                    double height = img.getHeight()*winScale/ImageLibrary.imageLoadScale;
                    gc.drawImage(img,-width/2,-height/2,width,height);
                }
            }
        }
    }

    private void drawDynamics(World world)
    {
        gc.setFill(Color.BLACK);

        for(DynamicObject obj : world.getDynamicObjects())
        {
            Affine affine = new Affine();

            double width = objectSize*obj.getScale().getX_dyn();
            double height = objectSize*obj.getScale().getY_dyn();

            double x_pos = obj.getPos().getX_dyn() * objectSize;
            double y_pos = (world.getWorldHeight() - obj.getPos().getY_dyn() - 1) * objectSize;
            affine.appendTranslation(x_pos+cameraPan.getX_dyn(), y_pos+cameraPan.getY_dyn());
            if(obj.getRot()!=0)affine.appendRotation(obj.getRot());
            affine.appendScale((obj.getFlipped()?-1:1),1);
            gc.setTransform(affine);

            gc.drawImage(obj.getImage(),-width/2,-height/2,width,height);

            if(Controller.debugging()){
                gc.fillRect(-objectSize*obj.getSize().getX_dyn()/2,-objectSize*obj.getSize().getY_dyn()/2,objectSize*obj.getSize().getX_dyn(),objectSize*obj.getSize().getY_dyn());
            }
        }
    }

    private void drawGlobalParticleSystem(World world)
    {
        gc.setTransform(new Affine());
        gc.setStroke(Color.LIGHTBLUE);
        gc.setGlobalAlpha(0.6);
        double x;
        double y;

        for(GlobalParticleSystem globalParticleSystem : world.getGlobalParticleSystems())
            for(DynamicVector dynamicVector : globalParticleSystem.getParticles())
            {
                x = dynamicVector.getX_dyn()*canvasDim.getX();
                y = canvasDim.getY()-dynamicVector.getY_dyn()*canvasDim.getY();
                gc.strokeLine(x,y,
                        x-globalParticleSystem.getSpeed().getX_dyn()*25,
                        y+globalParticleSystem.getSpeed().getY_dyn()*25);
            }
    }

    private void drawImageParticleSystem(World world)
    {
        gc.setTransform(new Affine());

        for(ImageParticleSystem.Particle particle : world.getImageParticleSystem().getParticles())
        {
            double width = particle.getImage().getWidth()/ImageLibrary.imageLoadScale;
            double height = particle.getImage().getHeight()/ImageLibrary.imageLoadScale;
            double x_pos = particle.getPos().getX_dyn() * objectSize + cameraPan.getX_dyn()-width/2;
            double y_pos = (world.getWorldHeight() - particle.getPos().getY_dyn() - 1) * objectSize + cameraPan.getY_dyn()-height/2;
            gc.setGlobalAlpha(Math.min(0.20,particle.getLifetime())*5);
            gc.drawImage(particle.getImage(),x_pos,y_pos,width,height);
        }
    }

    private void drawDetails(World world)
    {
        gc.setGlobalAlpha(1);
        for(Detail detail : world.getDetails())
        {
            if(!detail.getVisible())continue;
            Affine affine = new Affine();
            double width = winScale*detail.getImage().getWidth()/ImageLibrary.imageLoadScale;
            double height = winScale*detail.getImage().getHeight()/ImageLibrary.imageLoadScale;
            double x_pos = detail.getPos().getX_dyn() * objectSize;
            double y_pos = (world.getWorldHeight() - detail.getPos().getY_dyn() - 1) * objectSize;
            affine.appendTranslation(x_pos+cameraPan.getX_dyn(),y_pos+cameraPan.getY_dyn());
            gc.setTransform(affine);
            gc.drawImage(detail.getImage(),-width/2d,-height/2d,width,height);
        }
    }

    private void drawBackground(World world)
    {
        DynamicVector pan = getCameraPanInv();
        DynamicVector objectPan = new DynamicVector(pan.getX_dyn()/objectSize,pan.getY_dyn()/objectSize);
        Affine affine = new Affine();
        gc.setTransform(affine);

        for(BackgroundElement backgroundElement : world.getBackgroundElements())
        {
            double width = winScale*backgroundElement.getImage().getWidth()/ImageLibrary.imageLoadScale;
            double height = winScale*backgroundElement.getImage().getHeight()/ImageLibrary.imageLoadScale;
            double x = backgroundElement.getPivot().getX_dyn()*objectSize-objectPan.getX_dyn()*objectSize*backgroundElement.getMoveScale().getX_dyn();
            double y = canvasDim.getY()-backgroundElement.getPivot().getY_dyn()*objectSize+objectPan.getY_dyn()*objectSize*backgroundElement.getMoveScale().getY_dyn()-height;
            if(backgroundElement.getRepeatX())
            {
                gc.drawImage(backgroundElement.getImage(),x,y,width,height);
            }
            else
            {
                for(double _x=x;_x<canvasDim.getX();_x+=width)
                {
                    gc.drawImage(backgroundElement.getImage(),_x,y,width,height);
                }
            }
        }

        /*
        for(double i=100-objectPan.getX_dyn()*objectSize;i<canvasDim.getX();i+=img.getWidth()) {
            gc.drawImage(img, i, canvasDim.getY()-img.getHeight());
        }
        */
    }

    private void drawSky(World world)
    {
        gc.setFill(world.getSkyColor());
        gc.fillRect(0,0,canvasDim.getX(),canvasDim.getY());
    }

    private void panCamera(World world, DynamicVector target)
    {
        DynamicVector panTarget = getPanTarget(world,target);

        double transx = -panTarget.getX_dyn()+canvasDim.getX()/2;
        double transy = -panTarget.getY_dyn()+canvasDim.getY()/2;

        //Reason for it being objectSize/2 as the min is that blocks are drawed from their center, meaning they would be halfed, if objectSize/2 was not there.
        cameraPan.setX_dyn(Math.max(Math.min(objectSize/2,transx),-cameraPanBoundaries.getX()+objectSize/2));
        cameraPan.setY_dyn(Math.max(Math.min(objectSize/2,transy),-cameraPanBoundaries.getY()+objectSize/2));
    }

    private DynamicVector getPanTarget(World world, DynamicVector target)
    {
        return new DynamicVector(target.getX_dyn() * objectSize - objectSize / 2.0,
                (world.getWorldHeight() - target.getY_dyn() - 1) * objectSize - objectSize / 2.0);
    }

    private void drawGrid(World world)
    {
        Affine affine = new Affine();
        affine.appendTranslation(cameraPan.getX_dyn(),cameraPan.getY_dyn());
        gc.setTransform(affine);
        for(int x=0;x<world.getWorldWidth();x++)
        {
            for(int y=0;y<world.getWorldHeight();y++)
            {
                gc.setFill(Color.GRAY);
                gc.strokeRect(x*objectSize-objectSize/2,y*objectSize-objectSize/2,objectSize,objectSize);
            }
        }
    }

    private void drawTarget(World world,Vector target,EditorClass editorClass)
    {
        gc.setGlobalAlpha(0.6+0.2*Math.sin(Math.toRadians(EditorState.time*90)));
        Affine affine = new Affine();
        affine.appendTranslation(target.getX()*objectSize + cameraPan.getX_dyn() - objectSize/2,(world.getWorldHeight()-1-target.getY())*objectSize + cameraPan.getY_dyn() - objectSize/2);
        gc.setTransform(affine);
        gc.drawImage(editorClass.getImage(),0,0,objectSize,objectSize);
    }

    private void drawSpawn(World world)
    {
        gc.setGlobalAlpha(1);//gc.setGlobalAlpha(0.6+0.2*Math.sin(Math.toRadians(EditorState.time*90)));
        Affine affine = new Affine();
        DynamicVector target = world.getPlayerSpawnPoint();
        affine.appendTranslation(target.getX()*objectSize + cameraPan.getX_dyn() - objectSize/2,(world.getWorldHeight()-1-target.getY())*objectSize + cameraPan.getY_dyn()- objectSize/2);
        gc.setTransform(affine);
        gc.drawImage(ImageLibrary.getImage("char_idle.png"),0,0,objectSize,objectSize);
    }

    private void drawTileWindow(int elPerLine, int elLines, Vector target, Vector selected, EditorClass[] editorClasses)
    {
        gc.setTransform(new Affine());
        gc.setGlobalAlpha(1);
        gc.setFill(Color.GRAY);
        double x = canvasDim.getX()/6;
        double y = canvasDim.getY()/6;
        double width = 2*canvasDim.getX()/3;
        double height = 2*canvasDim.getY()/3;
        gc.fillRect(x,y,width,height);
        double elDist = 16;
        double elBoxDist = 4;
        double targetWidth = 4;
        double selectedWidth = 6;
        //int elPerLine = 10;
        //int elLines = 4;
        double elWidth = (width-elDist)/elPerLine-elDist;
        double elHeight = elWidth;
        //double elHeight = (height-elDist)/elLines-elDist;
        //Color[] colors = new Color[]{Color.GREEN,Color.RED,Color.YELLOW,Color.BLACK,Color.BLUE,Color.ORANGE};
        //Vector target = new Vector(2,2);
        int elementsDrawn = 0;
        for(int h=0;h<elLines;h++) {
            for (int w = 0; w < elPerLine; w++) {
                if(elementsDrawn >= editorClasses.length){return;}

                //gc.setFill(colors[w+h*elPerLine]);
                if(h==selected.getY()&& w==selected.getX()){
                    gc.setFill(Color.ORANGE);
                    gc.fillRect(x+elDist-selectedWidth+(elDist+elWidth)*w,y+elDist-selectedWidth+(elDist+elHeight)*h,elWidth+selectedWidth*2,elHeight+selectedWidth*2);
                }
                if(h==target.getY()&& w==target.getX()){
                    gc.setFill(Color.YELLOW);
                    gc.fillRect(x+elDist-targetWidth+(elDist+elWidth)*w,y+elDist-targetWidth+(elDist+elHeight)*h,elWidth+targetWidth*2,elHeight+targetWidth*2);
                }
                gc.setFill(Color.WHITE);
                gc.fillRect(x+elDist+(elDist+elWidth)*w,y+elDist+(elDist+elHeight)*h,elWidth,elHeight);
                gc.drawImage(editorClasses[elementsDrawn].getImage(),
                        x+elDist+(elDist+elWidth)*w+elBoxDist,
                        y+elDist+(elDist+elHeight)*h+elBoxDist,
                        elWidth-elBoxDist*2,
                        elHeight-elBoxDist*2);
                elementsDrawn += 1;
            }
        }
    }

    private void drawDebug()
    {

    }

    private DynamicVector getCameraPanInv() {
        return new DynamicVector(-cameraPan.getX_dyn(), cameraPan.getY_dyn() + cameraPanBoundaries.getY_dyn());
    }

}
