package Controllers;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.awt.event.KeyAdapter;
import java.util.HashMap;

/**
 * Created by Kris on 21-02-2017.
 */
public class KeyboardController extends KeyAdapter implements IUpdate {

    HashMap<KeyCode,Key> keys = new HashMap<>();
    Scene scene;
    boolean anyKeyPressed = false;
    boolean anyKeyJustPressed = false;

    public KeyboardController(Scene scene) {

        this.scene = scene;

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

    public boolean getAnyPressed() {return anyKeyPressed;}
    public boolean getAnyJustPressed() {return anyKeyJustPressed;}

    public void update(double delta)
    {
        anyKeyJustPressed = false;
        anyKeyPressed = false;
        for(Key key : keys.values())
        {
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
            if(justPressed)anyKeyJustPressed=true;
            this.pressed = pressed;
            if(this.pressed)anyKeyPressed=true;
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
