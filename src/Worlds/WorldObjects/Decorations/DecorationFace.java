package Worlds.WorldObjects.Decorations;

public class DecorationFace
{

    public Position position;
    public Face face;

    public DecorationFace(){};

    public DecorationFace(Face face){
        this(face,Position.Center);
    }

    public DecorationFace(Face face,Position position){
        this.face = face;
        this.position = position;
    }

    public enum Position {
        Solo,
        Left,
        Right,
        Center
    }

    public enum Face {
        Up,
        Down,
        Left,
        Right,
        Horizontal,
        Vertical,
        None
    }

    @Override
    public String toString()
    {
        return "Position: " + position + ", Face: " + face;
    }

}
