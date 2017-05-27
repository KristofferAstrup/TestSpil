package State.GameState;

import Controller.Controller;
import Controller.KeyboardController;
import Libraries.ImageLibrary;
import Vectors.DynamicVector;
import World.ParticleSystem.ImageParticleSystem;
import World.WorldObject.DynamicObject.PhysicObject.Mob.Player;
import World.WorldObject.DynamicObject.PhysicObject.Projectiles.Dagger;
import javafx.scene.input.KeyCode;
import World.*;

/**
 * Created by Kris on 21-02-2017.
 */
public class PlayerController {

    KeyboardController keyboardController;

    double moveAcc = 20;
    double deAccGround = 20;
    double deAccAir = 8;
    double moveSpeed = 6;
    double sprintSpeed = 11;
    double jumpSpeed = 7;
    double jumpTimeTotal = 0.275;
    double jumpTime = 0;
    boolean sprinting;
    private Player player;
    private World world;

    public PlayerController(Player player,World world, KeyboardController keyboardController)
    {
        this.keyboardController = keyboardController;
        this.world = world;
        this.player = player;
    }

    public void update(double delta)
    {
        if(!player.getAlive()){return;}

        sprinting = keyboardController.getKeyPressed(KeyCode.SHIFT);
        double maxSpeed = sprinting?sprintSpeed:moveSpeed;

        if(keyboardController.getKeyJustPressed(KeyCode.A))
        {
            Dagger dagger = new Dagger(world,player.getPos(),player.getFlipped()?180:0);
            world.addDynamicObject(dagger);
        }

        if(keyboardController.getKeyPressed(KeyCode.RIGHT) && (player.getSpeed().getX_dyn() <= maxSpeed || !player.getBlockedDirs().get(Dir.Down)))
        {
            if(player.getSpeed().getX_dyn() < maxSpeed) {
                player.getSpeed().setX_dyn(Math.min(maxSpeed,player.getSpeed().getX_dyn()+moveAcc*delta));

                player.setBraking(player.getSpeed().getX_dyn() < 0);
            }
        }
        else if(keyboardController.getKeyPressed(KeyCode.LEFT) && (player.getSpeed().getX_dyn() >= -maxSpeed || !player.getBlockedDirs().get(Dir.Down)))
        {
            if(player.getSpeed().getX_dyn() > -maxSpeed) {
                player.getSpeed().setX_dyn(Math.max(-maxSpeed,player.getSpeed().getX_dyn()-moveAcc*delta));

                player.setBraking(player.getSpeed().getX_dyn() > 0);
            }
        }
        else{
            if(player.getSpeed().getX_dyn() != 0)
            {
                double deAccLocal = player.getBlockedDirs().get(Dir.Down)?deAccGround:deAccAir;

                player.getSpeed().setX_dyn(player.getSpeed().getX_dyn() - delta * deAccLocal * (player.getSpeed().getX_dyn()>0?1:-1));
                if(Math.abs(player.getSpeed().getX_dyn()) < deAccLocal*delta*2)
                {
                    player.getSpeed().setX(0);
                    player.setBraking(false);
                }
                else if(Math.abs(player.getSpeed().getX_dyn()) > moveSpeed)
                {
                    player.setBraking(true);
                }
            }
        }

        if(keyboardController.getKeyJustPressed(KeyCode.SPACE) && player.getBlockedDirs().get(Dir.Left) && !player.getBlockedDirs().get(Dir.Down))
        {
            jumpTime = jumpTimeTotal;
            player.getSpeed().setX_dyn(moveSpeed);
        }
        else if(keyboardController.getKeyJustPressed(KeyCode.SPACE) && player.getBlockedDirs().get(Dir.Right) && !player.getBlockedDirs().get(Dir.Down))
        {
            jumpTime = jumpTimeTotal;
            player.getSpeed().setX_dyn(-moveSpeed);
        }
        else if(player.getBlockedDirs().get(Dir.Down))
        {
            jumpTime = jumpTimeTotal;
        }
        else if(!keyboardController.getKeyPressed(KeyCode.SPACE) || player.getBlockedDirs().get(Dir.Up))
        {
            jumpTime = 0;
        }
        if(keyboardController.getKeyPressed(KeyCode.SPACE) && jumpTime > 0)
        {
            jumpTime -= delta;
            player.getSpeed().setY_dyn(jumpSpeed);
        }

        if(player.getBlockedDirs().get(Dir.Right) && !player.getBlockedDirs().get(Dir.Down)){player.setFlipped(true);}
        else if(player.getBlockedDirs().get(Dir.Left) && !player.getBlockedDirs().get(Dir.Down)){player.setFlipped(false);}
        else if(keyboardController.getKeyPressed(KeyCode.RIGHT)){player.setFlipped(false);}
        else if(keyboardController.getKeyPressed(KeyCode.LEFT)){player.setFlipped(true);}

    }

}
