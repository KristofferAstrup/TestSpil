package Libraries;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import java.util.HashMap;
import Controllers.*;

/**
 * Created by kristoffer on 04-09-2017.
 */
public class KeybindLibrary {

    public static enum KeybindType
    {
        Left,
        Right,
        Up,
        Down,
        Jump,
        Fire,
        Sprint,
        Reset,
        SwitchState,
        SwitchDebug,
        SwitchDebugConsole,
        SwitchGrid,
        EditorFastPan,
        EditorDelete,
        EditorCreate,
        EditorSetSpawn,
        EditorFill,
        EditorSwitchObjectGroup,
        EditorObjectSelect
    }

    private static HashMap<KeybindType,IKeyBind[]> keybinds = new HashMap<KeybindType,IKeyBind[]>(){{
        put(KeybindType.Left,new IKeyBind[]{new KeyboardKeybind(KeyCode.A),new KeyboardKeybind(KeyCode.LEFT)});
        put(KeybindType.Right,new IKeyBind[]{new KeyboardKeybind(KeyCode.D),new KeyboardKeybind(KeyCode.RIGHT)});
        put(KeybindType.Up,new IKeyBind[]{new KeyboardKeybind(KeyCode.W),new KeyboardKeybind(KeyCode.UP)});
        put(KeybindType.Down,new IKeyBind[]{new KeyboardKeybind(KeyCode.S),new KeyboardKeybind(KeyCode.DOWN)});
        put(KeybindType.Jump,new IKeyBind[]{new KeyboardKeybind(KeyCode.SPACE)});
        put(KeybindType.Fire,new IKeyBind[]{new MouseKeybind(MouseButton.PRIMARY)});
        put(KeybindType.Sprint,new IKeyBind[]{new KeyboardKeybind(KeyCode.SHIFT)});
        put(KeybindType.Reset,new IKeyBind[]{new KeyboardKeybind(KeyCode.R)});
        put(KeybindType.SwitchState,new IKeyBind[]{new KeyboardKeybind(KeyCode.TAB)});
        put(KeybindType.SwitchDebug,new IKeyBind[]{new KeyboardKeybind(KeyCode.F5)});
        put(KeybindType.SwitchDebugConsole,new IKeyBind[]{new KeyboardKeybind(KeyCode.F12)});
        put(KeybindType.SwitchDebugConsole,new IKeyBind[]{new KeyboardKeybind(KeyCode.F12)});
        put(KeybindType.EditorFastPan,new IKeyBind[]{new KeyboardKeybind(KeyCode.ALT)});
        put(KeybindType.SwitchGrid,new IKeyBind[]{new KeyboardKeybind(KeyCode.G)});
        put(KeybindType.EditorDelete,new IKeyBind[]{new KeyboardKeybind(KeyCode.BACK_SPACE),new MouseKeybind(MouseButton.MIDDLE)});
        put(KeybindType.EditorCreate,new IKeyBind[]{new KeyboardKeybind(KeyCode.SPACE),new MouseKeybind(MouseButton.PRIMARY)});
        put(KeybindType.EditorSetSpawn,new IKeyBind[]{new KeyboardKeybind(KeyCode.ENTER)});
        put(KeybindType.EditorFill,new IKeyBind[]{new KeyboardKeybind(KeyCode.F)});
        put(KeybindType.EditorSwitchObjectGroup,new IKeyBind[]{new KeyboardKeybind(KeyCode.ALT)});
        put(KeybindType.EditorObjectSelect,new IKeyBind[]{new KeyboardKeybind(KeyCode.SHIFT)});
    }};

    public static boolean getKeybindPressed(KeybindType keybindType,InputController inputController){
        boolean succes = false;
        for(int i=0;i<keybinds.get(keybindType).length;i++){
            if(keybinds.get(keybindType)[i].getKeybindPressed(inputController))
                succes = true;
        }
        return succes;
    }

    public static boolean getKeybindJustPressed(KeybindType keybindType,InputController inputController){
        boolean succes = false;
        for(int i=0;i<keybinds.get(keybindType).length;i++){
            if(keybinds.get(keybindType)[i].getKeybindJustPressed(inputController))
                succes = true;
        }
        return succes;
    }

    private static interface IKeyBind{
        boolean getKeybindPressed(InputController inputController);
        boolean getKeybindJustPressed(InputController inputController);
    }

    private static class KeyboardKeybind implements IKeyBind
    {
        KeyCode keyCode;

        public KeyboardKeybind(KeyCode keyCode){
            this.keyCode = keyCode;
        }

        public boolean getKeybindPressed(InputController inputController){
            return inputController.getKeyboardController().getKeyPressed(keyCode);
        }

        public boolean getKeybindJustPressed(InputController inputController){
            return inputController.getKeyboardController().getKeyJustPressed(keyCode);
        }

    }

    private static class MouseKeybind implements IKeyBind
    {
        MouseButton mouseButton;

        public MouseKeybind(MouseButton mouseButton){
            this.mouseButton = mouseButton;
        }

        public boolean getKeybindPressed(InputController inputController){
            return inputController.getMouseController().getButtonPressed(mouseButton);
        }

        public boolean getKeybindJustPressed(InputController inputController){
            return inputController.getMouseController().getButtonJustPressed(mouseButton);
        }

    }

}
