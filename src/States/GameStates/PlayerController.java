package States.GameStates;

import Controllers.InputController;
import Controllers.KeyboardController;
import Controllers.MouseController;
import Libraries.KeybindLibrary;
import Vectors.DynamicVector;
import Vectors.Vector;
import Views.View;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Player;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Projectiles.Dagger;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Projectiles.Fist;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Projectiles.Projectile;
import javafx.scene.input.KeyCode;
import Worlds.*;
import javafx.scene.input.MouseButton;

/**
 * Created by Kris on 21-02-2017.
 */
public class PlayerController {

    InputController inputController;
    View view;

    private double moveAcc = 20;
    private double deAccGround = 20;
    private double deAccAir = 8;
    private double moveSpeed = 4;
    private double sprintSpeed = 8;
    private double jumpSpeed = 7;
    private double jumpTimeTotal = 0.275;
    private double jumpTime = 0;
    private boolean sprinting;
    private Player player;
    private World world;

    private Projectile fist;
    private boolean fistLaunched = false;
    private double fistSpeedBase = 8;
    private double fistSpeedCharge = 8;
    private double chargeTime = 0;
    private double chargeTimeBound = 1;

    public PlayerController(Player player, World world, InputController inputController, View view)
    {
        this.inputController = inputController;
        this.world = world;
        this.player = player;
        this.view = view;
    }

    public void update(double delta)
    {
        if(!player.getAlive()){return;}

        sprinting = KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Sprint,inputController);
        double maxSpeed = sprinting?sprintSpeed:moveSpeed;

        if(KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Fire,inputController))
        {
            if ((fist == null) && !fistLaunched) {
                chargeTime += delta;
            }
            else if(fist != null)
            {
                player.getPos().set(fist.getPos());
                player.getSpeed().set(fist.getSpeed());
                world.destroyWorldObject(fist);
                fist = null;
            }
        }
        else if(chargeTime > 0)
        {
            DynamicVector mousePos = view.getWorldPositionFromScreen(world, inputController.getMouseController().getMousePosition());
            double angle = -Vector.angle(player.getPos(), mousePos);
            fist = new Fist(world, player.getPos(), angle, fistSpeedBase+fistSpeedCharge*Math.min(chargeTime,chargeTimeBound)/chargeTimeBound);
            world.addDynamicObject(fist);
            chargeTime = 0;
            fistLaunched = true;
        }
        else if((fist == null || fist.isDestroyed()) && player.getBlockedDirs().get(Dir.Down))
        {
            fistLaunched = false;
            fist = null;
        }


        if(KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Right,inputController) && (player.getSpeed().getX_dyn() <= maxSpeed || !player.getBlockedDirs().get(Dir.Down)))
        {
            if(player.getSpeed().getX_dyn() < maxSpeed) {
                player.getSpeed().setX_dyn(Math.min(maxSpeed,player.getSpeed().getX_dyn()+moveAcc*delta));

                player.setBraking(player.getSpeed().getX_dyn() < 0);
            }
        }
        else if(KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Left,inputController) && (player.getSpeed().getX_dyn() >= -maxSpeed || !player.getBlockedDirs().get(Dir.Down)))
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

        if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.Jump,inputController) && player.getBlockedDirs().get(Dir.Left) && !player.getBlockedDirs().get(Dir.Down))
        {
            jumpTime = jumpTimeTotal;
            player.getSpeed().setX_dyn(moveSpeed);
        }
        else if(KeybindLibrary.getKeybindJustPressed(KeybindLibrary.KeybindType.Jump,inputController) && player.getBlockedDirs().get(Dir.Right) && !player.getBlockedDirs().get(Dir.Down))
        {
            jumpTime = jumpTimeTotal;
            player.getSpeed().setX_dyn(-moveSpeed);
        }
        else if(player.getBlockedDirs().get(Dir.Down))
        {
            jumpTime = jumpTimeTotal;
        }
        else if(!KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Jump,inputController) || player.getBlockedDirs().get(Dir.Up))
        {
            jumpTime = 0;
        }
        if(KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Jump,inputController) && jumpTime > 0)
        {
            jumpTime -= delta;
            player.getSpeed().setY_dyn(jumpSpeed);
        }

        if(player.getBlockedDirs().get(Dir.Right) && !player.getBlockedDirs().get(Dir.Down)){player.setFlipped(true);}
        else if(player.getBlockedDirs().get(Dir.Left) && !player.getBlockedDirs().get(Dir.Down)){player.setFlipped(false);}
        else if(KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Right,inputController)){player.setFlipped(false);}
        else if(KeybindLibrary.getKeybindPressed(KeybindLibrary.KeybindType.Left,inputController)){player.setFlipped(true);}

    }

}
