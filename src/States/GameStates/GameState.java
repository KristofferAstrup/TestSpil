package States.GameStates;

import Controllers.*;
import Libraries.KeybindLibrary;
import States.IState;
import Vectors.DynamicVector;
import Vectors.Vector;
import Views.View;
import Worlds.Dir;
import Worlds.ParticleSystems.GlobalParticleSystem;
import Worlds.World;
import Worlds.WorldObjects.DynamicObjects.DynamicObject;
import Worlds.WorldObjects.DynamicObjects.Goal;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Player;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Projectiles.Dagger;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Kris on 08-02-2017.
 */
public class GameState implements IState {

    public static double time;
    private World world;
    private Player player;
    private PlayerController playerController;
    private InputController inputController;
    private View view;

    private boolean playerWait = false;
    private boolean levelComplete = false;

    private ArrayList<DynamicObject> destroyedDynamicObjects;

    public GameState(String filename){
        throw new NotImplementedException();
    }

    public GameState(InputController inputController,View view)
    {
        this.inputController = inputController;
        this.view = view;
    }

    public State getState(){return State.Game;}

    public void startState(){
        reset();
    }

    public void endState(){
        world.getWorldObjects().remove(player);
        world.getDynamicObjects().remove(player);
        reset();
    }

    public void reset()
    {
        Controller.getWorld().reset();
        time = 0;
        playerWait = true;
        this.world = new World(Controller.getWorld());
        getWorld().endInit(); //COULD PROBABLY BE RELOCATED TO PARTS OF THE CONTROLLER, AS IT IS THE ONE THAT CREATES THE WORLD.
        player = new Player(world,new DynamicVector(world.getPlayerSpawnPoint().getX_dyn(),world.getPlayerSpawnPoint().getY_dyn()));
        playerController = new PlayerController(player,world,inputController,view);
        world.addPlayer(player);
        destroyedDynamicObjects = new ArrayList<>();
        world.worldStart();
        levelComplete = false;
    }

    public World getWorld()
    {
        return world;
    }

    public Player getPlayer()
    {
        return player;
    }

    public double getTime() {return time;}

    public boolean getPlayerWait() {return playerWait;}

    public boolean getLevelComplete() {return levelComplete;}

    public void update(double delta)
    {
        if(playerWait || levelComplete){
            if(KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Left,inputController) ||
                    KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Right,inputController) ||
                    KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Up,inputController) ||
                    KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Down,inputController) ||
                    KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Jump,inputController)){
                playerWait = false;
            }
        }
        else
        {
            time += delta;

            if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.Reset,inputController))
            {
                reset();
            }
            playerController.update(delta);
            for(DynamicObject dobj : world.getDynamicObjects())
            {
                if(!dobj.isDestroyed())dobj.update(delta);
                else{destroyedDynamicObjects.add(dobj);}
            }
            if(destroyedDynamicObjects.size()>0)
            {
                for(DynamicObject dynamicObject : destroyedDynamicObjects){
                    world.deleteDynamicObject(dynamicObject);
                }
                destroyedDynamicObjects = new ArrayList<>(1);
            }

            //SHOULD BE REWRITTEN!!
            if(!levelComplete && player.getBlockedDirs().get(Dir.Down) && world.getGoal().getPos().dist(world.getPlayerTarget(world.getGoal()).getPos().add(Goal.enterOffset)) < Goal.enterDistance)
            {
                completeLevel();
                player.getPos().set(world.getGoal().getPos().add(0,player.getSize().getY_dyn()/2-world.getGoal().getSize().getY_dyn()/2));
                player.setDestroyed(true);
            }
        }
        for(GlobalParticleSystem globalParticleSystem : world.getGlobalParticleSystems())
        {
            globalParticleSystem.update(delta);
        }
        world.getImageParticleSystem().update(delta);
    }

    private void completeLevel()
    {
        levelComplete = true;
    }

}
