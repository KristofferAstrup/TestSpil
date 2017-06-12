package Libraries;

import Controller.SaveLoadController;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.print.URIException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.security.CodeSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Kris on 28-02-2017.
 */
public class ImageLibrary {

    private static HashMap<String,Image> imageLibrary;

    public final static int imageLoadScale = 4;

    private static int pathStartIndex = ".\\src\\Resources".length();

    public static void init()
    {
        initImageLibrary();
    }

    private static void initImageLibrary()
    {
       /*try {
            File file = new File("resources/Images");
            for(String s : file.list()){
                System.out.println(s);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }*/

        imageLibrary = new HashMap<>();
        loadImages("/Images/Mobs/Bat/bat_0.png#" +
                "/Images/Mobs/Bat/bat_1.png#" +
                "/Images/Mobs/Bat/bat_2.png#" +
                "/Images/Mobs/Bat/bat_3.png#" +
                //"/Images/Bricks/Bricks_E.png#" +
                // /Images/Bricks/Bricks_I.png#" +
                "/Images/Blocks/Bricks/Bricks_O.png#" +
                // /Images/Bricks/Bricks_Z.png#" +
                "/Images/Mobs/Char/char_airborne_down.png#" +
                "/Images/Mobs/Char/char_airborne_stale.png#" +
                "/Images/Mobs/Char/char_airborne_up.png#" +
                "/Images/Mobs/Char/char_brake.png#" +
                "/Images/Mobs/Char/char_idle.png#" +
                "/Images/Mobs/Char/char_run_0.png#" +
                "/Images/Mobs/Char/char_run_1.png#" +
                "/Images/Mobs/Char/char_run_2.png#" +
                "/Images/Mobs/Char/char_run_3.png#" +
                "/Images/Mobs/Char/char_wallslide.png#" +
                "/Images/DirtBgn.png#" +
                "/Images/Blocks/DirtBlock/Dirt_C.png#" +
                "/Images/Blocks/DirtBlock/Dirt_E.png#" +
                "/Images/Blocks/DirtBlock/Dirt_O.png#" +
                "/Images/Blocks/DirtBlock/Dirt_Q.png#" +
                "/Images/Blocks/DirtBlock/Dirt_S.png#" +
                "/Images/Blocks/DirtBlock/Dirt_U.png#" +
                "/Images/Blocks/DirtBlock/Dirt_Z.png#" +
                "/Images/Blocks/DirtBlock/DirtChunk_0.png#" +
                "/Images/Div/Fluff.png#" +
                "/Images/Div/Fluff2.png#" +
                "/Images/Div/Black.png#" +
                "/Images/Div/Red.png#" +
                "/Images/Div/Yellow.png#" +
                "/Images/Div/White.png#" +
                "/Images/Div/Blue.png#" +
                "/Images/Mobs/Pig/pig_0.png#" +
                "/Images/Mobs/Pig/pig_1.png#" +
                "/Images/Mobs/Pig/pig_2.png#" +
                "/Images/Mobs/Pig/pig_3.png#" +
                "/Images/Mobs/Pig/pig_4.png#" +

                "/Images/Mobs/Boss/BossBody.png#" +
                "/Images/Mobs/Boss/BossEye.png#" +
                "/Images/Mobs/Boss/BossEyeCharged.png#" +
                "/Images/Mobs/Boss/BossEyeLidsFull.png#" +
                "/Images/Mobs/Boss/BossEyeLidsHalf.png#" +
                "/Images/Mobs/Boss/BossEyeLidsQuarter.png#" +
                "/Images/Mobs/Boss/BossEyeLight.png#" +
                "/Images/Mobs/Boss/BossEyeSmallPupil.png#" +

                "/Images/Background/cloud0.png#" +
                "/Images/Background/cloud1.png#" +
                "/Images/Background/cloud2.png#" +
                "/Images/Background/cloud3.png#" +
                "/Images/Background/cloud4.png#" +
                "/Images/Background/Hills.png#" +
                "/Images/Background/Ocean.png#"+
                "/Images/Projectiles/Dagger.png"

        );
        /*
        imageLibrary = new HashMap<>();
        URL url = ImageLibrary.class.getResource("/Images");
        File[] files = new File(url.getFile()).listFiles();
        loadImages(files,"/Images/");
        */
    }

    private static void loadImages(String string)
    {

        //SaveLoadController.saveTextFileAbsolute("C:/Users/kristoffer/Documents/Java/log/test.txt","---");

        /*try {
            CodeSource src = ImageLibrary.class.getProtectionDomain().getCodeSource();
            if (src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null)
                        break;
                    String name = e.getName();
                    //if (name.startsWith(""))
                    {
                        System.out.println(name);
                    }
                }
            }
        }
        catch(IOException exception){
            exception.printStackTrace();
        }*/


        try {
            ImageLibrary lib = new ImageLibrary();
            String[] paths = string.split("#");
            for(String s : paths)
            {
                //System.out.println(s);
                URL url = lib.getClass().getResource(s);
                Image image = new Image(url.toURI().toString());
                String name = s.substring(s.lastIndexOf("/")+1);
                imageLibrary.put(name,getScaledImage(image,imageLoadScale));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void loadImages(File[] files,String path)
    {
        for (int i = 0; i < files.length; i++) {
            if(files[i].isDirectory())
            {
                loadImages(files[i].listFiles(),path + files[i].getName() + "/");
            }
            if (files[i].isFile()) {
                Image img = new Image(files[i].toURI().toString());
                imageLibrary.put(files[i].getName(),getScaledImage(img,imageLoadScale));
            }
        }
    }

    private static Image getScaledImage(Image img,int scale)
    {
        PixelReader reader = img.getPixelReader();
        int w = (int)(img.getWidth()*scale);
        int h = (int)(img.getHeight()*scale);
        WritableImage imgwr = new WritableImage(w,h);
        PixelWriter writer = imgwr.getPixelWriter();
        for(int x=0;x<w;x++)
        {
            for(int y=0;y<h;y++)
            {
                writer.setColor(x,y,reader.getColor((int)Math.floor(x/scale),(int)Math.floor(y/scale)));
            }
        }
        return imgwr;
    }

    private static String getCollectiveFilePath(File[] files,String path)
    {
        String s = "";
        for (int i = 0; i < files.length; i++) {
            if(files[i].isDirectory())
            {
                s += getCollectiveFilePath(files[i].listFiles(),path + files[i].getName() + "/");
            }
            if (files[i].isFile()) {
                s += files[i].toString().substring(pathStartIndex) + "#";
            }
        }
        return s;
    }

    public static Image getImage(String name)
    {
        return imageLibrary.get(name);
    }

}
