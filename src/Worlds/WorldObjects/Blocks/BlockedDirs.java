package Worlds.WorldObjects.Blocks;

import Worlds.Dir;

import java.util.HashMap;

public class BlockedDirs {

    private HashMap<Dir,Block> blockedDirs = new HashMap<>();

    public BlockedDirs(HashMap<Dir,Block> blockedDirs)
    {
        this.blockedDirs = blockedDirs;
    }

    public BlockedDirs(Block right,Block down,Block left,Block up)
    {
        blockedDirs = new HashMap<Dir,Block>(){{put(Dir.Right,right);put(Dir.Down,down);put(Dir.Left,left);put(Dir.Up,up);}};
    }

    public Block getBlock(Dir dir){
        return blockedDirs.get(dir);
    }

}
