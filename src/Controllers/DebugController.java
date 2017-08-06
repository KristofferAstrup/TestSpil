package Controllers;

import Factories.EditorClassGroup;
import Libraries.EditorClass;
import Libraries.EditorLibrary;
import States.EditorStates.EditorState;
import States.GameStates.GameState;
import States.IState;
import Vectors.Vector;
import Worlds.World;
import Worlds.WorldObjects.WorldObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DebugController {

    private Controller controller;
    private KeyboardController keyboardController;
    private ArrayList<String> output;
    private ArrayList<String> input;
    private int inputIndex = 0;
    private final String seperator = "-----------------";

    private HashMap<String,WorldObject> debugWorldObjects;

    public DebugController(Controller controller,KeyboardController keyboardController)
    {
        this.controller = controller;
        this.keyboardController = keyboardController;
        output = new ArrayList<>();
        input = new ArrayList<>();
    }

    public void resetDebugWorldObjects(){debugWorldObjects = new HashMap<>();}

    private DebugCommand[] debugCommands = new DebugCommand[]{

            //----WORLDOBJECT HANDLING----//
            new DebugCommand("create (?<name>[A-Za-z]*) (?<xpos>[0-9]*) (?<ypos>[0-9]*)", (matcher) -> {
                EditorClass editorClass = EditorLibrary.findEditorClass(matcher.group("name"));
                if(editorClass == null || !Controller.tryParseInt(matcher.group("xpos")) || !Controller.tryParseInt(matcher.group("ypos"))) return false;
                EditorState.createWorldObject(editorClass.getClasss(),Controller.getWorld(),new Vector(Integer.parseInt(matcher.group("xpos")),Integer.parseInt(matcher.group("ypos"))));
                ////VIRKER IKKE, MANGLER AT ADDE WORLDOBJECTET TIL WORLD, MEN DEN HER KLASSE KENDER IKKE WORLD I DETTE KONTEKST!!
                return true;
            },"create [name] [xpos] [ypos]","Creates a given Worldobject at a given position"),

            new DebugCommand("create (?<debugname>[A-Za-z0-9]*) (?<objname>[A-Za-z]*) (?<xpos>[0-9]*) (?<ypos>[0-9]*)", (matcher) -> {
                EditorClass editorClass = EditorLibrary.findEditorClass(matcher.group("objname"));
                if(editorClass == null || !Controller.tryParseInt(matcher.group("xpos")) || !Controller.tryParseInt(matcher.group("ypos"))) return false;
                WorldObject wo = EditorState.createWorldObject(editorClass.getClasss(),Controller.getWorld(),new Vector(Integer.parseInt(matcher.group("xpos")),Integer.parseInt(matcher.group("ypos"))));
                debugWorldObjects.put(matcher.group("debugname"),wo);
                return true;
            },"create [debugname] [objname] [xpos] [ypos]","Creates a given Worldobject at a given position and saves it via given name"),

            new DebugCommand("delete (?<objgroupname>[A-Za-z]*) (?<xpos>[0-9]*) (?<ypos>[0-9]*)", (matcher) -> {
                EditorClassGroup objTypeGroup = EditorLibrary.findEditorClassGroup(matcher.group("objgroupname"));
                if(objTypeGroup == null || !Controller.tryParseInt(matcher.group("xpos")) || !Controller.tryParseInt(matcher.group("ypos"))) return false;
                EditorState.deleteObj(Controller.getWorld(),new Vector(Integer.parseInt(matcher.group("xpos")),Integer.parseInt(matcher.group("ypos"))),objTypeGroup);
                return true;
            },"delete [objgroupname] [xpos] [ypos]","Creates a given WorldObjects at a given position"),

            new DebugCommand("delete (?<debugname>[A-Za-z]*)", (matcher) -> {
                if(!debugWorldObjects.containsKey(matcher.group("debugname"))){return false;}
                //EditorStates.deleteObj(debugWorldObjects.get(matcher.group("debugname")));
                return true;
            },"delete [objgroupname] [xpos] [ypos]","Creates a given WorldObjects at a given position"),

            new DebugCommand("set timescale (?<scale>[0-9.]*)", (matcher) -> {
                if(!Controller.tryParseDouble(matcher.group("scale"))){return false;}
                Controller.setTimeScale(Double.parseDouble(matcher.group("scale")));
                return true;
            },"set timescale [scale]","Sets the timescale of states"),

            new DebugCommand("teleport (?<xpos>[0-9]*) (?<ypos>[0-9]*)", (matcher) -> {
                if(!Controller.tryParseInt(matcher.group("xpos")) || !Controller.tryParseInt(matcher.group("ypos"))) return false;
                if(controller.getCurrentState() == IState.State.Game && controller.getStates().containsKey(IState.State.Game))
                {
                    ((GameState)(controller.getStates().get(IState.State.Game))).getPlayer().getPos().set(
                            new Vector(Integer.parseInt(matcher.group("xpos")),Integer.parseInt(matcher.group("ypos"))));
                }
                else if(controller.getCurrentState() == IState.State.Editor && controller.getStates().containsKey(IState.State.Editor))
                {
                    ((EditorState)(controller.getStates().get(IState.State.Editor))).getWorldTarget().set(
                            new Vector(Integer.parseInt(matcher.group("xpos")),Integer.parseInt(matcher.group("ypos"))));
                }
                return true;
            },"teleport [xpos] [ypos]","Sets the position of the Player or Cursor"),

            new DebugCommand("teleport (?<debugname>[A-Za-z0-9]*) (?<xpos>[0-9]*) (?<ypos>[0-9]*)", (matcher) -> {
                if(!debugWorldObjects.containsKey(matcher.group("debugname")) || !Controller.tryParseInt(matcher.group("xpos")) || !Controller.tryParseInt(matcher.group("ypos"))) return false;
                debugWorldObjects.get(matcher.group("debugname")).getPos().set(new Vector(Integer.parseInt(matcher.group("xpos")),Integer.parseInt(matcher.group("ypos"))));
                return true;
            },"teleport [debugname] [xpos] [ypos]","Sets the position of the WorldObjects associated with the given debugname"),

            //----WORLD HANDLING----//
            new DebugCommand("reset", (matcher) -> {
                Controller.getWorld().reset();
                return true;
            },"reset","Resets the world"),

            //----WORLD HANDLING----//
            new DebugCommand("set gravity (?<value>[0-9.]*)", (matcher) -> {
                if(!Controller.tryParseDouble(matcher.group("value"))){return false;}
                Controller.getWorld().setGravity(Double.parseDouble(matcher.group("value")));
                return true;
            },"set gravity [value]","Sets the gravity of the world"),


            //----FILE HANDLING----//
            new DebugCommand("save (?<filename>[A-Za-z0-9.]*)", (matcher) -> {
                if(!SaveLoadController.validateFilename(matcher.group("filename"))){return false;}
                SaveLoadController.saveFile(matcher.group("filename"),Controller.getWorld());
                return true;
            },"save [filename]","Saves the current world to a file with a given filename"),

            new DebugCommand("load (?<filename>[A-Za-z0-9.]*)", (matcher) -> {
                if(!SaveLoadController.validateFilename(matcher.group("filename"))){return false;}
                Controller.setWorld((World)SaveLoadController.loadFile(matcher.group("filename")));
                controller.changeState(controller.getCurrentState());
                return true;
            },"load [filename]","Loads a world via the given filename"),

            //----CONSOLE HANDLING----//
            new DebugCommand("clear", (matcher) -> {
                output = new ArrayList<>();
                return true;
            },"clear","Clears the console"),

            new DebugCommand("help", (matcher) -> {
                listDebugCommands(false);
                return true;
            },"help","Lists all commands"),

            new DebugCommand("help tooltip", (matcher) -> {
                listDebugCommands(true);
                return true;
            },"help tooltip","Lists all commands with tooltips"),

            new DebugCommand("help (?<commandname>[A-Za-z])", (matcher) -> {
                return showCommandHelp(matcher.group("commandname"));
            },"help [commandname]","Display the format and the tooltip for a command"),

            new DebugCommand("input", (matcher) -> {
                output.add("\n" + seperator + "\n");
                for(String s : input)
                {
                    output.add(s + "\n");
                }
                output.add(seperator + "\n");
                return true;
            },"input","Lists all input")
    };

    private void listDebugCommands(boolean tooltip)
    {
        output.add("\n" + seperator + "\n");
        for(DebugCommand debugCommand : debugCommands)
        {
            showCommandHelp(debugCommand,tooltip);
        }
        output.add(seperator + "\n");
    }

    private boolean showCommandHelp(String commandname)
    {
        boolean anyfound = false;
        for(DebugCommand debugCommand : debugCommands)
        {
            if(debugCommand.getFormat().contains(commandname)) {
                showCommandHelp(debugCommand, true);
                anyfound = true;
            }
        }
        return anyfound;
    }

    private void showCommandHelp(DebugCommand debugCommand,boolean tooltip)
    {
        String s = "-" + debugCommand.getFormat() + " \n";
        if(tooltip){s += debugCommand.getTooltip() + "\n";}
        output.add(s);
    }

    public void parseCommand(String commandline)
    {
        input.add(commandline);
        if(commandline.startsWith("*"))
        {output = new ArrayList<>(); commandline = commandline.substring(1);
        if(commandline.length()==1)return;}
        String commandlineS = commandline.toLowerCase();

        DebugCommand debugCommand = findMatch(commandlineS);
        output.add("\n" + commandline);
        if(debugCommand != null)
        {
            Matcher matcher = debugCommand.getMatcher(commandlineS);
            matcher.matches();
            if(debugCommand.execute(matcher))
            {
                output.add("\n");
            }else{
                output.add("\nParameters incorrect, expected: " + debugCommand.getFormat() + "\n");
            }
        }
        else{
            output.add("\nCommand not found.\n");
        }
    }

    public String getInput(){if(input.size() == 0)return null; return input.get(inputIndex);}
    public void moveInputIndex(int i)
    {
        if(input.size() == 0){return;}
        inputIndex += i;
        if(inputIndex < 0){inputIndex = 0;}
        else if(inputIndex >= input.size()){inputIndex = input.size()-1;}
    }
    public void resetInputIndex(){inputIndex = input.size();} //Intentionally 1 too big.
    public void resetInput(){input = new ArrayList<>();}

    private DebugCommand findMatch(String commandline)
    {
        for(DebugCommand debugCommand : debugCommands)
        {
            Matcher m = debugCommand.getMatcher(commandline);
            if(m.matches()){
                return debugCommand;
            }
        }
        return null;
    }

    public ArrayList<String> getOutput()
    {
        return output;
    }

    public class DebugCommand
    {
        private Pattern pattern;
        private IExecuteable iExecuteable;
        private String format;
        private String tooltip;
        public DebugCommand(String regex,IExecuteable iExecuteable,String format,String tooltip)
        {
            this.pattern = Pattern.compile(regex);
            this.iExecuteable = iExecuteable;
            this.format = format;
            this.tooltip = tooltip;
        }
        public Pattern getPattern(){return pattern;}
        public boolean execute(Matcher matcher){return iExecuteable.execute(matcher);}
        public String getFormat(){return format;}
        public String getTooltip(){return tooltip;}
        public Matcher getMatcher(String commandline){return pattern.matcher(commandline);}
    }

}
