package World.WorldObject.Block;

import World.Dir;

import java.util.HashMap;

public class BlockedOrientation {

    int blocks;
    HashMap<Dir,Boolean> blockedDirs;

    public BlockedOrientation(boolean right,boolean down,boolean left,boolean up)
    {
        blocks = (right?1:0)+(down?1:0)+(left?1:0)+(up?1:0);
        blockedDirs = new HashMap<Dir,Boolean>(){{put(Dir.Right,right);put(Dir.Down,down);put(Dir.Left,left);put(Dir.Up,up);}};
    }

    public boolean getBlocked(Dir dir){
        return blockedDirs.get(dir);
    }

    public int getBlocks()
    {
        return blocks;
    }

    @Override
    public String toString()
    {
        return "Right:" + blockedDirs.get(Dir.Right) + " Down:" + blockedDirs.get(Dir.Down) + " Left:" + blockedDirs.get(Dir.Left) + " Up:" + blockedDirs.get(Dir.Up);
    }

}
