package Controllers;

import States.EditorStates.EditorState;
import States.GameStates.GameState;
import States.IState;
import Worlds.World;
import Worlds.Dir;
import Views.*;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Random;

public class Controller {

    public static Random random;
    private View view;
    private IState.State state;
    private HashMap<IState.State,IState> states;
    private KeyboardController keyboardController;
    private MouseController mouseController;
    private boolean gameRunning = false;
    private static World world;
    private static boolean editing = true;
    private static boolean debugging = false;
    private static boolean consoleOpen = false;
    private static DebugController debugController;
    private static double timescale = 1;

    public Controller(View view)
    {
        Dir.init();
        this.view = view;
        keyboardController = new KeyboardController(this.view.getScene());
        mouseController = new MouseController(this.view.getScene());
        debugController = new DebugController(this,keyboardController);
        random = new Random();

        state = IState.State.None;
        world = new World(60,30);
        states = new HashMap<IState.State,IState>(){
        {
            put(IState.State.Game, new GameState(keyboardController));
            put(IState.State.Editor, new EditorState(keyboardController,mouseController,view));
        }};

        DebugGroup debugGroup = new DebugGroup();
        debugGroup.getTextField().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.F5)
                {
                    consoleOpen = !consoleOpen;
                    view.setVisibleDebugGroup(consoleOpen);
                }
                if(event.getCode() == KeyCode.F12)
                {
                    toggleDebugging();
                }
                if(event.getCode() == KeyCode.ENTER)
                {
                    Controller.getDebugController().parseCommand(debugGroup.getTextField().getText());
                    debugController.resetInputIndex();
                    debugGroup.getTextField().clear();
                    debugGroup.updateOutPutLabel();
                }
                if(event.getCode() == KeyCode.UP)
                {
                    debugController.moveInputIndex(-1);
                    debugGroup.getTextField().setText(debugController.getInput());
                    //debugGroup.getTextField().selectPositionCaret(debugGroup.getTextField().getText().length()-1);
                }
                else if(event.getCode() == KeyCode.DOWN)
                {
                    debugController.moveInputIndex(1);
                    debugGroup.getTextField().setText(debugController.getInput());
                    //debugGroup.getTextField().positionCaret(debugGroup.getTextField().getText().length()-1);
                }
            }
        });
        view.addDebugGroup(debugGroup);
        view.setVisibleDebugGroup(false);
    }

    public void changeState(IState.State newState)
    {
        gameRunning = true;
        debugController.resetDebugWorldObjects();
        if(state != IState.State.None){
            states.get(state).endState();
        }
        state = newState;
        states.get(state).startState();
    }

    public IState.State getCurrentState()
    {
        return state;
    }

    public HashMap<IState.State,IState> getStates()
    {
        return states;
    }

    public void update(double delta)
    {
        if(gameRunning) {

            if(keyboardController.getKeyJustPressed(KeyCode.TAB) && editing)
            {
                changeState(state==IState.State.Game?IState.State.Editor:IState.State.Game);
            }
            if(keyboardController.getKeyJustPressed(KeyCode.F5) && debugging)
            {
                consoleOpen = !consoleOpen;
                view.setVisibleDebugGroup(consoleOpen);
            }
            if(keyboardController.getKeyJustPressed(KeyCode.F12))
            {
                toggleDebugging();
            }

            //if(debugging){debugController.update(delta);}
            states.get(state).update(delta*timescale);
            view.update(states.get(state));
            keyboardController.update(delta);
            mouseController.update(delta);
        }
    }

    private void toggleDebugging()
    {
        debugging = !debugging;
        if(!debugging)
        {
            consoleOpen = false;
            view.setVisibleDebugGroup(consoleOpen);
        }
    }

    public static boolean getEditing(){return editing;}

    public static void setEditing(boolean _editing){editing = _editing;}

    public static void setTimeScale(double _timeScale){timescale = _timeScale;}

    public static DebugController getDebugController(){return debugController;}

    public static World getWorld()
    {
        return world;
    }

    public static void setWorld(World _world)
    {
        world = _world;
        world.init();
        world.endInit();
    }

    public static boolean tryParseInt(String value){
        try{Integer.parseInt(value); return true;}
        catch(NumberFormatException e){return false;}
    }

    public static boolean tryParseDouble(String value){
        try{Double.parseDouble(value); return true;}
        catch(NumberFormatException e){return false;}
    }

    public static boolean debugging(){return debugging;}

    public static boolean consoleOpen(){return consoleOpen;}

}
