package View;

import Controller.Controller;
import Controller.DebugController;
import Controller.SaveLoadController;
import Factories.ObjType;
import Libraries.ImageLibrary;
import Libraries.ObjTypeLibrary;
import State.EditorState.EditorMode;
import State.EditorState.EditorState;
import State.GameState.GameState;
import State.IState;
import Vectors.DynamicVector;
import Vectors.Vector;
import World.Background.BackgroundElement;
import World.World;
import World.WorldObject.DynamicObject.DynamicObject;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.stage.*;

import java.lang.reflect.Array;
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
    boolean debugGroupsVisisble = false;

    String[] paths;
    Group root = new Group();

    public View(Stage theStage)
    {
        theStage.setTitle("TestSpil");
        stage = theStage;
        //stage.setFullScreen(true);

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
        debugGroupsVisisble = visible;
        for(DebugGroup debugGroup : debugGroups) {
            debugGroup.setVisible(debugGroupsVisisble);
        }
    }

    public boolean getVisibleDebugGroup(){return debugGroupsVisisble;}

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

        gc.setTransform(new Affine());
        gc.setStroke(Color.LIGHTBLUE);
        gc.setGlobalAlpha(0.6);

         for(DynamicVector dynamicVector : gameState.getWorld().rain.getParticles())
        {
            double x = dynamicVector.getX_dyn()*canvasDim.getX();
            double y = canvasDim.getY()-dynamicVector.getY_dyn()*canvasDim.getY();
            gc.strokeLine(x,y,
                    x-gameState.getWorld().rain.getSpeed().getX_dyn()*25,
                    y+gameState.getWorld().rain.getSpeed().getY_dyn()*25);
        }

    }

    private void drawEditorState(EditorState editorState) {
        setWinScale(2,editorState.getWorld());
        panCamera(editorState.getWorld(), editorState.getWorldTarget().getDynamicVector());
        drawGrid(editorState.getWorld());
        drawBlocks(editorState.getWorld());
        drawDynamics(editorState.getWorld());

        drawTarget(editorState.getWorld(), editorState.getWorldTarget(),editorState.getObjTypeSelected());
        drawSpawn(editorState.getWorld());

        if (editorState.getEditorMode() == EditorMode.ObjectSelect) {
            drawTileWindow(10, 4, editorState.getObjectMenuTarget(), editorState.getObjectMenuSelected(),
                    ObjTypeLibrary.getObjTypes(editorState.getObjTypeGroup()));
        }
    }

    public void setWinScale(double winScale,World world)
    {
        this.winScale = winScale;
        objectSize = objectSizeBase * winScale;
        cameraPanBoundaries = new DynamicVector((world.getWorldWidth())*objectSize-canvasDim.getX(),(world.getWorldHeight())*objectSize-canvasDim.getY());
    }

    private void drawBlocks(World world)
    {
        for(int x=0;x<world.getBlocks().length;x++){
            for(int y=0;y<world.getBlocks()[x].length;y++){
                if(world.getBlocks()[x][y] != null)
                {
                    Image img = world.getBlocks()[x][y].getImage();
                    double x_pos = x*objectSize;
                    double y_pos = (world.getBlocks()[x].length-y-1)*objectSize;
                    Affine affine = new Affine();
                    affine.appendTranslation(x_pos + cameraPan.getX_dyn(),y_pos + cameraPan.getY_dyn());
                    affine.appendRotation(world.getBlocks()[x][y].getRot(),objectSize/2.0,objectSize/2.0);
                    gc.setTransform(affine);
                    gc.drawImage(img,0,0,objectSize,objectSize);
                }
            }
        }
    }

    private void drawDynamics(World world)
    {
        for(DynamicObject obj : world.getDynamicObjects())
        {
            Affine affine = new Affine();
            double x_pos = obj.getPos().getX_dyn() * objectSize;
            double y_pos = (world.getWorldHeight() - obj.getPos().getY_dyn() - 1) * objectSize;
            affine.appendTranslation(x_pos-(obj.getFlipped()?-objectSize:0)+cameraPan.getX_dyn(), y_pos+cameraPan.getY_dyn());
            affine.appendScale((obj.getFlipped()?-1:1),1);
            gc.setTransform(affine);
            gc.drawImage(obj.getImage(),0,0,objectSize,objectSize);
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

        cameraPan.setX_dyn(Math.max(Math.min(0,transx),-cameraPanBoundaries.getX()));
        cameraPan.setY_dyn(Math.max(Math.min(0,transy),-cameraPanBoundaries.getY()));
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
                gc.strokeRect(x*objectSize,y*objectSize,objectSize,objectSize);
            }
        }
    }

    private void drawTarget(World world,Vector target,ObjType objType)
    {
        gc.setGlobalAlpha(0.6+0.2*Math.sin(Math.toRadians(EditorState.time*90)));
        Affine affine = new Affine();
        affine.appendTranslation(target.getX()*objectSize + cameraPan.getX_dyn(),(world.getWorldHeight()-1-target.getY())*objectSize + cameraPan.getY_dyn());
        gc.setTransform(affine);
        gc.drawImage(objType.getIcon(),0,0,objectSize,objectSize);
    }

    private void drawSpawn(World world)
    {
        gc.setGlobalAlpha(1);//gc.setGlobalAlpha(0.6+0.2*Math.sin(Math.toRadians(EditorState.time*90)));
        Affine affine = new Affine();
        DynamicVector target = world.getPlayerSpawnPoint();
        affine.appendTranslation(target.getX()*objectSize + cameraPan.getX_dyn(),(world.getWorldHeight()-1-target.getY())*objectSize + cameraPan.getY_dyn());
        gc.setTransform(affine);
        gc.drawImage(ImageLibrary.getImage("char_idle.png"),0,0,objectSize,objectSize);
    }

    private void drawTileWindow(int elPerLine, int elLines, Vector target, Vector selected, ObjType[] objTypes)
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
                if(elementsDrawn >= objTypes.length){return;}

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
                gc.drawImage(objTypes[elementsDrawn].getIcon(),
                        x+elDist+(elDist+elWidth)*w+elBoxDist,
                        y+elDist+(elDist+elHeight)*h+elBoxDist,
                        elWidth-elBoxDist*2,
                        elHeight-elBoxDist*2);
                elementsDrawn += 1;
            }
        }
    }

    private DynamicVector getCameraPanInv() {
        return new DynamicVector(-cameraPan.getX_dyn(), cameraPan.getY_dyn() + cameraPanBoundaries.getY_dyn());
    }

}
