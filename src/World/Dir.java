package World;

import Vectors.Vector;

/**
 * Created by Kris on 21-02-2017.
 */
public class Dir {

    static public Dir Right = new Dir(0,"Right",new Vector(1,0));
    static public Dir Down = new Dir(1,"Down",new Vector(0,-1));
    static public Dir Left = new Dir(2,"Left", new Vector(-1,0));
    static public Dir Up = new Dir(3,"Up",new Vector(0,1));

    private int i;
    private String name;
    private Vector vector;
    private Dir[] crossDirs;

    public static void init()
    {
        Right.setCrossDirs(new Dir[]{Up,Down});
        Down.setCrossDirs(new Dir[]{Right,Left});
        Left.setCrossDirs(new Dir[]{Up,Down});
        Up.setCrossDirs(new Dir[]{Right,Left});
    }

    public static Dir[] getValues()
    {
        return new Dir[]{Right,Down,Left,Up};
    }

    private void setCrossDirs(Dir[] crossDirs)
    {
        this.crossDirs = crossDirs;
    }

    private Dir(int i,String name,Vector vector)
    {
        this.i = i;
        this.name = name;
        this.vector = vector;
    }

    public int getInt()
    {
        return i;
    }

    public String getName()
    {
        return name;
    }

    public Dir[] getCrossDirs()
    {
        return crossDirs;
    }

    public Vector getVector()
    {
        return vector;
    }

    @Override
    public boolean equals(Object obj)
    {
        return i == ((Dir) obj).getInt();
    }

    @Override
    public String toString()
    {
        return name;
    }

}
