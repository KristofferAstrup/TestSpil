package Vectors;

import World.WorldObject.DynamicObject.DynamicObject;

import java.awt.geom.Dimension2D;
import java.io.Serializable;

/**
 * Created by Kris on 13-02-2017.
 */
public class DynamicVector extends Vector implements Serializable {

    private double x_dyn;
    private double y_dyn;

    public DynamicVector(double x, double y)
    {
        super((int)Math.round(x),(int)Math.round(y));
        x_dyn = x;
        y_dyn = y;
    }

    public DynamicVector(int x, int y)
    {
        super(x,y);
        x_dyn = x;
        y_dyn = y;
    }

    public DynamicVector(DynamicVector vec)
    {
        super(vec.getX(),vec.getY());
        x_dyn = vec.x_dyn;
        y_dyn = vec.y_dyn;
    }

    public DynamicVector(Vector vec)
    {
        setX(vec.getX());
        setY(vec.getY());
    }

    @Override
    public void setX(int x)
    {
        super.setX(x);
        x_dyn = x;
    }

    @Override
    public void setY(int y)
    {
        super.setY(y);
        y_dyn = y;
    }

    public void setX_dyn(double x)
    {
        super.setX((int)Math.round(x));
        x_dyn = x;
    }

    public void setY_dyn(double y)
    {
        super.setY((int)Math.round(y));
        y_dyn = y;
    }

    public void setAdd(DynamicVector vec)
    {
        setX_dyn(getX_dyn()+vec.getX_dyn());
        setY_dyn(getY_dyn()+vec.getY_dyn());
    }

    public double getX_dyn()
    {
        return x_dyn;
    }

    public double getY_dyn()
    {
        return y_dyn;
    }

    public DynamicVector subtract(DynamicVector vec)
    {
        return new DynamicVector(x_dyn-vec.getX_dyn(),y_dyn-vec.getY_dyn());
    }

    public DynamicVector add(DynamicVector vec)
    {
        return new DynamicVector(x_dyn+vec.getX_dyn(),y_dyn+vec.getY_dyn());
    }

    public DynamicVector add(double x,double y){return new DynamicVector(x_dyn+x,y_dyn+y);}

    public DynamicVector multiply(DynamicVector vec)
    {
        return new DynamicVector(x_dyn*vec.getX_dyn(),y_dyn*vec.getY_dyn());
    }

    public DynamicVector multiply(double d)
    {
        return new DynamicVector(x_dyn*d,y_dyn*d);
    }

    public DynamicVector multiply(double xMultiplier,double yMultiplier){return new DynamicVector(x_dyn*xMultiplier,y_dyn*yMultiplier);}

    @Override
    public DynamicVector normal()
    {
        return new DynamicVector(-y_dyn,x_dyn);
    }

    @Override
    public DynamicVector normalInv()
    {
        return new DynamicVector(y_dyn,x_dyn);
    }

    @Override
    public double length()
    {
        return Math.sqrt(x_dyn*x_dyn+y_dyn*y_dyn);
    }

    public DynamicVector normalize()
    {
        double length = length();
        return new DynamicVector(x_dyn/length,y_dyn/length);
    }

    @Override
    public void set(Vector pos)
    {
        super.set(pos);
        x_dyn = pos.getX();
        y_dyn = pos.getY();
    }

    @Override
    public void set(DynamicVector pos)
    {
        super.set(pos);
        x_dyn = pos.getX_dyn();
        y_dyn = pos.getY_dyn();
    }

    @Override
    public void set(Dimension2D dim)
    {
        super.set(dim);
        x_dyn = dim.getWidth();
        y_dyn = dim.getHeight();
    }

    @Override
    public double dist(Vector v)
    {
        DynamicVector dv = new DynamicVector(v.getX()-getX_dyn(),v.getY()-getY_dyn());
        return Math.sqrt(dv.getX_dyn()*dv.getX_dyn()+dv.getY_dyn()*dv.getY_dyn());
    }

    @Override
    public double dist(DynamicVector v)
    {
        DynamicVector dv = new DynamicVector(v.getX_dyn()-getX_dyn(),v.getY_dyn()-getY_dyn());
        return Math.sqrt(dv.getX_dyn()*dv.getX_dyn()+dv.getY_dyn()*dv.getY_dyn());
    }

    @Override
    public boolean equals(Object _b){
        if(_b instanceof DynamicVector)
        {
            return ((DynamicVector)_b).x_dyn == x_dyn && ((DynamicVector)_b).y_dyn == y_dyn;
        }
        else if(_b instanceof Vector) {
            return ((Vector)_b).getX() == getX() && ((Vector)_b).getX() == getY();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "X: " + x_dyn + " Y: " + y_dyn;
    }

}
