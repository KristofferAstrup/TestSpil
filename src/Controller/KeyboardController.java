package Controller;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * Created by Kris on 21-02-2017.
 */
public class KeyboardController extends KeyAdapter implements IUpdate {

    HashMap<KeyCode,Key> keys = new HashMap<>();
    Scene scene;

    public KeyboardController(Scene scene) {

        this.scene = scene;
        /*for(KeyCode keyCode : new KeyCode[]{KeyCode.D,KeyCode.A,KeyCode.S,KeyCode.SHIFT,KeyCode.SPACE})
        {
            keys.put(keyCode,new Key());
        }*/

        scene.setOnKeyPressed(e -> {
            if(keys.containsKey(e.getCode())){
                keys.get(e.getCode()).updatePressed(true);
            }
            else
            {
                keys.put(e.getCode(),new Key());
                keys.get(e.getCode()).updatePressed(true);
            }
        });

        scene.setOnKeyReleased(e -> {
            if(keys.containsKey(e.getCode())){
                keys.get(e.getCode()).updatePressed(false);
            }
        });
    }

    public boolean getKeyPressed(KeyCode keyCode)
    {
        if(keys.containsKey(keyCode))
        {
            return keys.get(keyCode).getPressed();
        }
        return false;
    }

    public boolean getKeyJustPressed(KeyCode keyCode)
    {
        if(keys.containsKey(keyCode))
        {
            return keys.get(keyCode).getJustPressed();
        }
        return false;
    }

    public void update(double delta)
    {
        //if(scene.getOnKeyPressed()<KeyEvent.VK_SHIFT>)
        //System.out.println(keys.get(KeyEvent.VK_O).getPressed() + " | " + keys.get(KeyEvent.VK_O).getJustPressed());
        for(Key key : keys.values())
        {
            //System.out.println(key.)
            key.update(delta);
        }
    }

    public class Key{
        private boolean justPressed;
        private boolean pressed;
        private double pressDuration;
        private Key()
        {
            justPressed = false;
            pressed = false;
            pressDuration = 0;
        }
        private void updatePressed(boolean pressed)
        {
            justPressed = !this.pressed && pressed;
            this.pressed = pressed;
        }
        private void update(double delta)
        {
            justPressed = false;
            if(pressed)
            {
                pressDuration += delta;
            }
            else
            {
                pressDuration = 0;
            }
        }
        public boolean getJustPressed()
        {
            return justPressed;
        }
        public boolean getPressed()
        {
            return pressed;
        }
    }

}
