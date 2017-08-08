package Libraries;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

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
        imageLibrary = new HashMap<>();
        loadImages("/Images/Mobs/Bat/bat_0.png#" +
                "/Images/Mobs/Bat/bat_1.png#" +
                "/Images/Mobs/Bat/bat_2.png#" +
                "/Images/Mobs/Bat/bat_3.png#" +

                "/Images/Blocks/Bricks/Bricks_O.png#" +
                "/Images/Blocks/Bricks/BrickChunk_0.png#" +
                "/Images/Blocks/Bricks/BrickChunk_1.png#" +

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
                "/Images/Blocks/DirtBlock/Dirt_Z.png#" +
                "/Images/Blocks/DirtBlock/Dirt_E.png#" +
                "/Images/Blocks/DirtBlock/Dirt_O.png#" +
                "/Images/Blocks/DirtBlock/Dirt_Q.png#" +
                "/Images/Blocks/DirtBlock/Dirt_S.png#" +
                "/Images/Blocks/DirtBlock/Dirt_U.png#" +
                "/Images/Blocks/DirtBlock/Dirt_C.png#" +
                "/Images/Blocks/DirtBlock/DirtChunk_0.png#" +

                "/Images/Blocks/Plankwall/Plankwall_C.png#" +
                "/Images/Blocks/Plankwall/Plankwall_E.png#" +
                "/Images/Blocks/Plankwall/Plankwall_O.png#" +
                "/Images/Blocks/Plankwall/Plankwall_S.png#" +
                "/Images/Blocks/Plankwall/Plankwall_U.png#" +
                "/Images/Blocks/Plankwall/Plankwall_Z.png#" +
                "/Images/Blocks/Plankwall/Plankwall_Z_1.png#" +
                "/Images/Blocks/Plankwall/Plankwall_Z_2.png#" +

                "/Images/Blocks/Stone/Stone0.png#" +
                "/Images/Blocks/Stone/Stone1.png#" +
                "/Images/Blocks/Stone/Stone2.png#" +
                "/Images/Blocks/Stone/Stone3.png#" +
                "/Images/Blocks/Stone/Stone4.png#" +
                "/Images/Blocks/Stone/Stone5.png#" +

                "/Images/Blocks/Woodlog.png#" +

                "/Images/Decoration/Grass/grass_up_center0.png#" +
                "/Images/Decoration/Grass/grass_up_center1.png#" +
                "/Images/Decoration/Grass/grass_up_center2.png#" +
                "/Images/Decoration/Grass/grass_up_center3.png#" +
                "/Images/Decoration/Grass/grass_left.png#" +
                "/Images/Decoration/Grass/grass_down.png#" +

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

                "/Images/Blocks/Dirttop.png#" +
                "/Images/Blocks/Grasstop.png#" +

                "/Images/Background/cloud0.png#" +
                "/Images/Background/cloud1.png#" +
                "/Images/Background/cloud2.png#" +
                "/Images/Background/cloud3.png#" +
                "/Images/Background/cloud4.png#" +
                "/Images/Background/Hills.png#" +
                "/Images/Background/Ocean.png#"+
                "/Images/Projectiles/Dagger.png#"+

                "/Images/Gate.png"

        );

    }

    private static void loadImages(String string)
    {
        try {
            ImageLibrary lib = new ImageLibrary();
            String[] paths = string.split("#");
            for(String s : paths)
            {
                URL url = lib.getClass().getResource(s);
                Image image = new Image(url.toURI().toString());
                String name = s.substring(s.lastIndexOf("/")+1,s.length()-4);
                System.out.println(name + " | " + url);
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
        if(name.endsWith(".png"))name = name.substring(0,name.length()-4);
        return imageLibrary.get(name);
    }

}
