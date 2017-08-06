package Controllers;

import Libraries.ImageLibrary;
import States.IState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import Views.View;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static javafx.application.Application.launch;

/**
 * Created by Kris on 08-02-2017.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(final Stage theStage)
    {
        ImageLibrary.init();
        View view = new View(theStage);
        Controller controller = new Controller(view);
        controller.changeState(IState.State.Editor);
        AnimationTimer timer = new AnimationTimer()
        {
            long lastNanoTime = 0;
            public void handle(long currentNanoTime)
            {
                double delta = ((double)(currentNanoTime-lastNanoTime))/1000000000.0;
                if(delta <1) {
                    controller.update(delta);
                }
                lastNanoTime = currentNanoTime;
            }
        };

        theStage.setOnCloseRequest(event -> {
            timer.stop();
        });

        timer.start();

        theStage.close();
    }


    /*static int globalFps = 0;
    long lastLoopTime = System.nanoTime();
    final int TARGET_FPS = 60;
    final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
    boolean gameRunning = true;
    int fps = 0;
    double lastFpsTime = 0;
    int constFps = 0;*/

    /*public void init(){
        controller.init();
        update();
    }

    public void update(){

            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);

            lastFpsTime += updateLength;
            fps++;

            if (lastFpsTime >= 1000000000)
            {
                constFps = fps;
                System.out.println("(FPS: "+fps+")");
                lastFpsTime = 0;
                fps = 0;
            }

            if(constFps > 0) {
                globalFps = constFps;
                controller.update(1.0 / ((double) constFps));
            }

            try
            {
                Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
                update();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
    }*/


    String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
        URL dirURL = clazz.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
        /* A file path: easy enough */
            return new File(dirURL.toURI()).list();
        }

        if (dirURL == null) {
        /*
         * In case of a jar file, we can't actually find a directory.
         * Have to assume the same jar as clazz.
         */
            String me = clazz.getName().replace(".", "/")+".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }

        if (dirURL.getProtocol().equals("jar")) {
        /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
            while(entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(path)) { //filter according to the path
                    String entry = name.substring(path.length());
                    int checkSubdir = entry.indexOf("/");
                    if (checkSubdir >= 0) {
                        // if it is a subdirectory, we just return the directory name
                        entry = entry.substring(0, checkSubdir);
                    }
                    result.add(entry);
                }
            }
            return result.toArray(new String[result.size()]);
        }

        throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
    }

}
