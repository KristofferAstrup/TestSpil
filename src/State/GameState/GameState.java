package State.GameState;

import Controller.*;
import State.IState;
import Vectors.DynamicVector;
import Vectors.Vector;
import World.ParticleSystem.GlobalParticleSystem;
import World.World;
import World.WorldObject.Block.DirtBlock;
import World.WorldObject.DynamicObject.DynamicObject;
import World.WorldObject.DynamicObject.PhysicObject.Mob.Bat;
import World.WorldObject.DynamicObject.PhysicObject.Mob.Pig;
import World.WorldObject.DynamicObject.PhysicObject.Mob.Player;
import World.WorldObject.WorldObject;
import javafx.scene.input.KeyCode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

/**
 * Created by Kris on 08-02-2017.
 */
public class GameState implements IState {

    public static double time = 0;
    private World world;
    private Player player;
    private PlayerController playerController;
    private KeyboardController keyboardController;

    private ArrayList<DynamicObject> destroyedDynamicObjects;

    public GameState(String filename){
        throw new NotImplementedException();
    }

    public GameState(KeyboardController keyboardController)
    {
        this.keyboardController = keyboardController;
    }

    public State getState(){return State.Game;}

    public void startState(){
        this.world = new World(Controller.getWorld());
        getWorld().endInit(); //COULD PROBABLY BE RELOCATED TO PARTS OF THE CONTROLLER, AS IT IS THE ONE THAT CREATES THE WORLD.
        player = new Player(world,new DynamicVector(world.getPlayerSpawnPoint().getX_dyn(),world.getPlayerSpawnPoint().getY_dyn()));
        playerController = new PlayerController(player,world,keyboardController);
        world.addPlayer(player);
        destroyedDynamicObjects = new ArrayList<>();
    }

    public void endState(){
        world.getWorldObjects().remove(player);
        world.getDynamicObjects().remove(player);
        reset();
    }

    public void reset()
    {
        Controller.getWorld().reset();
    }

    public World getWorld()
    {
        return world;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void update(double delta)
    {
        time += delta;
        if(keyboardController.getKeyPressed(KeyCode.R))
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
            destroyedDynamicObjects = new ArrayList<>();
        }
        for(GlobalParticleSystem globalParticleSystem : world.getGlobalParticleSystems())
        {
            globalParticleSystem.update(delta);
        }
    }

}
