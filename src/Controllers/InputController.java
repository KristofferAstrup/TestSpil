package Controllers;

import javafx.scene.*;

public class InputController {

    KeyboardController keyboardController;
    MouseController mouseController;

    public InputController(Scene scene)
    {
        keyboardController = new KeyboardController(scene);
        mouseController = new MouseController(scene);
    }

    public void update(double delta)
    {
        keyboardController.update(delta);
        mouseController.update(delta);
    }

    public KeyboardController getKeyboardController() {
        return keyboardController;
    }

    public void setKeyboardController(KeyboardController keyboardController) {
        this.keyboardController = keyboardController;
    }

    public MouseController getMouseController() {
        return mouseController;
    }

    public void setMouseController(MouseController mouseController) {
        this.mouseController = mouseController;
    }

}
