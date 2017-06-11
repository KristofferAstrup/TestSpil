package Vectors;

import java.awt.geom.Dimension2D;
import java.io.Serializable;

/**
 * Created by Kris on 08-02-2017.
 */
public class Vector implements Serializable {
    private int x;
    private int y;

    public Vector()
    {
        //Default ctor for reflection in serializing
    }

    public Vector(int _x, int _y){
        x = _x;
        y = _y;
    }

    public Vector(Vector vec)
    {
        x = vec.getX();
        y = vec.getY();
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void set(Vector pos)
    {
        x = pos.getX();
        y = pos.getY();
    }

    public void set(int x,int y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(DynamicVector pos)
    {
        x = (int)Math.round(pos.getX_dyn());
        y = (int)Math.round(pos.getY_dyn());
    }

    public void set(Dimension2D dim)
    {
        x = (int)Math.round(dim.getWidth());
        y = (int)Math.round(dim.getHeight());
    }

    public Vector add(Vector vector)
    {
        return new Vector(x+vector.getX(),y+vector.getY());
    }

    public Vector add(int x,int y)
    {
        return new Vector(this.x+x,this.y+y);
    }

    public Vector mulitply(int d)
    {
        return new Vector(x*d,y*d);
    }

    public double dist(Vector v)
    {
        Vector dv = new Vector(v.getX()-getX(),v.getX()-getX());
        return Math.sqrt(dv.getX()*dv.getX()+dv.getY()*dv.getY());
    }

    public double dist(DynamicVector v)
    {
        DynamicVector dv = new DynamicVector(v.getX_dyn()-getX(),v.getY_dyn()-getY());
        return Math.sqrt(dv.getX_dyn()*dv.getX_dyn()+dv.getY_dyn()*dv.getY_dyn());
    }

    public DynamicVector normal()
    {
        return new DynamicVector(-y,x);
    }

    public DynamicVector normalInv() //Should REALLY be rewritten!
        {
        return normal().normal().normal();
    }

    public double length()
    {
        return Math.sqrt(x*x+y*y);
    }

    public DynamicVector getDynamicVector()
    {
        return new DynamicVector(x,y);
    }

    @Override
    public boolean equals(Object _b){
        Vector b = (Vector)_b;
        return b.x == x && b.y == y;
    }

    @Override
    public String toString()
    {
        return "X: " + x + " Y: " + y;
    }

}

