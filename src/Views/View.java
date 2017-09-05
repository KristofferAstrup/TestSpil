package Views;

import Controllers.Controller;
import Libraries.EditorClass;
import Libraries.ImageLibrary;
import Libraries.EditorLibrary;
import States.EditorStates.EditorMode;
import States.EditorStates.EditorState;
import States.GameStates.GameState;
import States.IState;
import Vectors.DynamicVector;
import Vectors.Vector;
import Views.Groups.DebugGroup;
import Views.Groups.EndScreenGroup;
import Views.StateViews.EditorStateView;
import Views.StateViews.GameStateView;
import Views.StateViews.StateView;
import Worlds.Backgrounds.BackgroundElement;
import Worlds.ParticleSystems.GlobalParticleSystem;
import Worlds.ParticleSystems.ImageParticleSystem;
import Worlds.World;
import Worlds.Detail;
import Worlds.WorldObjects.DynamicObjects.DynamicObject;
import Controllers.SaveLoadController;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.stage.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumMap;

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
    BorderPane root = new BorderPane();

    boolean targetEffect = false;

    IState.State lastState;
    EnumMap<IState.State,StateView> stateViews;

    public View(Stage theStage)
    {
        theStage.setTitle("TS");
        stage = theStage;

        canvasDim = new Vector(900,600);

        scene = new Scene(root,canvasDim.getX_dyn(),canvasDim.getY_dyn());

        //root.setEffect(new ColorAdjust(0,-0.75,0.0,0)); //Black and White

        stage.setScene( scene );

        canvas = new Canvas( canvasDim.getX(), canvasDim.getY() );

        root.getChildren().add( canvas );
        stage.setWidth(canvasDim.getX()+16);
        stage.setHeight(canvasDim.getY()+40);

        gc = canvas.getGraphicsContext2D();
        paths = SaveLoadController.getFilePathList();

        stateViews = new EnumMap<>(IState.State.class);
        stateViews.put(IState.State.Game,new GameStateView(this));
        stateViews.put(IState.State.Editor,new EditorStateView(this));

    }

    public boolean targetEffect() {return targetEffect;}

    public double getWinScale() {return winScale;}

    public double getObjectSize() {return objectSize;}

    public DynamicVector getCameraPan() {return cameraPan;}

    public DynamicVector getCameraPanBoundaries() {return cameraPanBoundaries;}

    public BorderPane getRoot() {return root;}

    public Scene getScene()
    {
        return scene;
    }

    public void setTargetEffect(boolean effectActive) {targetEffect = effectActive;}

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

    public DynamicVector getWorldPositionFromScreen(World world, DynamicVector screenPos)
    {
        return new DynamicVector((-cameraPan.getX_dyn()+screenPos.getX_dyn())/objectSize,cameraPan.getY_dyn()/objectSize+world.getWorldHeight()-screenPos.getY_dyn()/objectSize-1);
    }

    public DynamicVector getPanDirectionVector(DynamicVector mousePosition, int horizontalZone, int verticalZone)
    {
        DynamicVector dir = new DynamicVector(0,0);
        if(mousePosition.getX() <= horizontalZone){
            dir.setX_dyn((mousePosition.getX_dyn()/horizontalZone)-1);
        }
        else if(mousePosition.getX() >= getCanvasDim().getX()-horizontalZone){
            dir.setX_dyn((mousePosition.getX_dyn()-(getCanvasDim().getX()-horizontalZone))/horizontalZone);
        }
        if(mousePosition.getY() <= verticalZone){
            dir.setY_dyn(1-mousePosition.getY_dyn()/verticalZone);
        }
        else if(mousePosition.getY() >= getCanvasDim().getY()-verticalZone){
            dir.setY_dyn(-((mousePosition.getY_dyn()-(getCanvasDim().getY()-verticalZone))/verticalZone));
        }
        return dir;
    }

    public DynamicVector getMinimumCornerCenterVector(World world, DynamicVector position)
    {
        return new DynamicVector(Math.min(world.getWorldWidth()-getCanvasDim().getX()/(2*getObjectSize()),Math.max(position.getX_dyn(),getCanvasDim().getX()/(2*getObjectSize()))),
                Math.min(world.getWorldHeight()-getCanvasDim().getY()/(2*getObjectSize()),Math.max(position.getY_dyn(),getCanvasDim().getY()/(2*getObjectSize()))));
    }

    public void update(IState state,double delta)
    {
        gc.setTransform(new Affine());

        gc.clearRect(0,0,canvasDim.getX(),canvasDim.getY());
        gc.save();

        if(lastState != state.getState()){
            if(lastState != null) stateViews.get(lastState).end();
            stateViews.get(state.getState()).start(gc);
            lastState = state.getState();
        }
        stateViews.get(state.getState()).draw(state,delta);

        gc.restore();
        stage.show();

        /*for(int i=0;i<paths.length;i++)
        {
            gc.fillText(paths[i],25,25+i*25);
        }*/
    }

    public void setWinScale(double winScale,World world)
    {
        this.winScale = winScale;
        objectSize = objectSizeBase * winScale;
        cameraPanBoundaries = new DynamicVector((world.getWorldWidth())*objectSize-canvasDim.getX(),(world.getWorldHeight())*objectSize-canvasDim.getY());
    }

}
