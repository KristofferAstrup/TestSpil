package Controller;

import Vectors.Vector;

import java.io.*;
import java.util.ArrayList;

public class SaveLoadController {

    private static final SaveLoadController saveLoadController = new SaveLoadController();

    private static String rootFolder = "TestSpil";
    
    private static String suffix = ".boi";

    public static void saveFile(String filename,Object obj)
    {
        String path = getOSpath() + rootFolder;
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try{
            FileOutputStream fileOut = new FileOutputStream( path + "/" + filename + suffix);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(obj);
            out.close();
            fileOut.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static boolean checkFileExists(String filename)
    {
        String path = getOSpath() + rootFolder + "/" + filename + suffix;
        File file = new File(path);
        return file.exists();
    }

    public static String[] getFilePathList()
    {
        String path = getOSpath() + rootFolder;
        File directory = new File(path);
        if (!directory.exists()) {
            return null;
        }
        return directory.list();
    }

    public static boolean validateFilename(String filename)
    {
        return true;
    }

    public static Object loadFile(String filename)
    {
        String path = getOSpath() + rootFolder;
        File directory = new File(path);
        if (!directory.exists()) {
            return null;
        }

        Object obj = null;
        try{
            FileInputStream fileIn = new FileInputStream(path + "/" + filename + suffix);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            obj = in.readObject();
            in.close();
            fileIn.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return obj;
    }

    public static boolean deleteFile(String filename)
    {
        String path = getOSpath() + rootFolder;
        File directory = new File(path);
        if (directory.exists()) {
            try {
                File file = new File(path + "/" + filename + suffix);
                boolean success = file.delete();
                return success;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void saveTextFile(String filename,String s)
    {
        String path = getOSpath() + rootFolder;
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try
        {
            File file = new File(path + "/" + filename + ".txt");
            FileWriter fw = new FileWriter(file);
            fw.write(s);
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    private static String getOSpath(){
        String path = System.getenv("APPDATA") + "\\";
        String os = System.getProperty("os.name").toUpperCase();

        if (os.contains("WIN")) {
            path = System.getenv("APPDATA") + "\\";
            System.out.println("Found windows");
        }
        if (os.contains("MAC")) {
            path = System.getProperty("user.home") + "/Library/Application " + "Support";
            System.out.println("Found mac");
        }
        if (os.contains("NUX")) {
            path = System.getProperty("user.dir") + ".";
            System.out.println("Found linux");
        }
        return path;
    }

}
