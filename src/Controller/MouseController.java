package Controller;

import Vectors.DynamicVector;
import Vectors.Vector;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.awt.event.MouseAdapter;
import java.util.HashMap;

/**
 * Created by kristoffer on 20-06-2017.
 */
public class MouseController extends MouseAdapter {

    private HashMap<MouseButton,Button> buttons = new HashMap<>();
    private Scene scene;
    private final DynamicVector lastMousePos = new DynamicVector(0,0);
    private final DynamicVector deltaMousePos = new DynamicVector(0,0);
    private int scrollDir = 0;

    public MouseController(Scene scene) {

        this.scene = scene;

        scene.setOnMousePressed(e -> {
            if(buttons.containsKey(e.getButton())){
                buttons.get(e.getButton()).updatePressed(true);
            }
            else
            {
                buttons.put(e.getButton(),new Button());
                buttons.get(e.getButton()).updatePressed(true);
            }
        });

        scene.setOnMouseReleased(e -> {
            if(buttons.containsKey(e.getButton())){
                buttons.get(e.getButton()).updatePressed(false);
            }
        });

        scene.setOnMouseDragged(e -> {
            trackMouse(e);
        });

        scene.setOnMouseMoved(e -> {
            trackMouse(e);
        });

        scene.setOnScroll(e -> {
            scrollDir = (int)(e.getDeltaY()/Math.abs(e.getDeltaY()));
        });

    }

    private void trackMouse(MouseEvent e)
    {
        if(lastMousePos.getX_dyn() != Double.POSITIVE_INFINITY){
            deltaMousePos.set(lastMousePos.getX_dyn()-e.getX(),lastMousePos.getY_dyn()-e.getY());
        }
        lastMousePos.set(e.getX(),e.getY());
    }

    public boolean getButtonPressed(MouseButton mouseButton)
    {
        if(buttons.containsKey(mouseButton))
        {
            return buttons.get(mouseButton).getPressed();
        }
        return false;
    }

    public boolean getButtonJustPressed(MouseButton mouseButton)
    {
        if(buttons.containsKey(mouseButton))
        {
            return buttons.get(mouseButton).getJustPressed();
        }
        return false;
    }

    public int getMouseScrollDir(){return scrollDir;}

    public DynamicVector getMousePosition()
    {
        return lastMousePos;
    }

    public DynamicVector getMouseMovement()
    {
        return deltaMousePos;
    }

    public void update(double delta)
    {
        for(Button btn : buttons.values())
        {
            btn.update(delta);
        }
        deltaMousePos.set(0,0);
        scrollDir = 0;
    }

    public class Button{
        private boolean justPressed;
        private boolean pressed;
        private double pressDuration;
        private Button()
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
