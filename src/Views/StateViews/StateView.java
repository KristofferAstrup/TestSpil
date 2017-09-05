package Views.StateViews;

import Controllers.Controller;
import Libraries.EditorClass;
import Libraries.ImageLibrary;
import States.IState;
import Vectors.DynamicVector;
import Vectors.Vector;
import Views.View;
import Worlds.Backgrounds.BackgroundElement;
import Worlds.Detail;
import Worlds.ParticleSystems.GlobalParticleSystem;
import Worlds.ParticleSystems.ImageParticleSystem;
import Worlds.World;
import Worlds.WorldObjects.DynamicObjects.DynamicObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 * Created by kristoffer on 22-08-2017.
 */
public abstract class StateView {

    View view;
    GraphicsContext gc;

    public StateView(View view){
        this.view = view;
    }

    public abstract void draw(IState state,double delta);

    public void start(GraphicsContext gc)
    {
        this.gc = gc;
    }

    public abstract void end();

    void drawBlocks(World world)
    {
        gc.setFill(Color.BLACK);
        gc.setGlobalAlpha(1);
        for(int x=0;x<world.getBlocks().length;x++){
            for(int y=0;y<world.getBlocks()[x].length;y++){
                if(world.getBlocks()[x][y] != null)
                {
                    Image img = world.getBlocks()[x][y].getImage();
                    double x_pos = x*view.getObjectSize();//-view.getObjectSize()/2;
                    double y_pos = (world.getBlocks()[x].length-y-1)*view.getObjectSize();//-view.getObjectSize()/2;
                    Affine affine = new Affine();
                    affine.appendTranslation(x_pos + view.getCameraPan().getX_dyn(),y_pos + view.getCameraPan().getY_dyn());
                    affine.appendRotation(world.getBlocks()[x][y].getRot(),0,0);
                    gc.setTransform(affine);
                    double width = img.getWidth()*view.getWinScale()/ ImageLibrary.imageLoadScale;
                    double height = img.getHeight()*view.getWinScale()/ImageLibrary.imageLoadScale;
                    gc.drawImage(img,-width/2,-height/2,width,height);
                }
            }
        }
    }

    void drawDecorations(World world)
    {
        gc.setFill(Color.BLACK);
        for(int x=0;x<world.getDecorations().length;x++){
            for(int y=0;y<world.getDecorations()[x].length;y++){
                if(world.getDecorations()[x][y] != null)
                {
                    Image img = world.getDecorations()[x][y].getImage();
                    double x_pos = (x-(y%2)*0.5)*view.getObjectSize();
                    double y_pos = (world.getDecorations()[x].length-y-1)*view.getObjectSize()*0.5-0.5*view.getObjectSize();
                    //System.out.println("DRAW: " + (world.getDecorations()[x].length-y-1));
                    Affine affine = new Affine();
                    affine.appendTranslation(x_pos + view.getCameraPan().getX_dyn(),y_pos + view.getCameraPan().getY_dyn());
                    affine.appendScale(world.getDecorations()[x][y].getFlipped()?-1:1,1);
                    gc.setTransform(affine);
                    double width = img.getWidth()*view.getWinScale()/ImageLibrary.imageLoadScale;
                    double height = img.getHeight()*view.getWinScale()/ImageLibrary.imageLoadScale;
                    gc.drawImage(img,-width/2,-height/2,width,height);
                }
            }
        }
    }

    void drawDynamics(World world)
    {
        gc.setFill(Color.BLACK);

        for(DynamicObject obj : world.getDynamicObjects())
        {
            if(obj.isDestroyed())continue;

            Affine affine = new Affine();

            double width = view.getObjectSize()*obj.getScale().getX_dyn();
            double height = view.getObjectSize()*obj.getScale().getY_dyn();

            double x_pos = obj.getPos().getX_dyn() * view.getObjectSize();
            double y_pos = (world.getWorldHeight() - obj.getPos().getY_dyn() - 1) * view.getObjectSize();
            affine.appendTranslation(x_pos+view.getCameraPan().getX_dyn(), y_pos+view.getCameraPan().getY_dyn());
            if(obj.getRot()!=0)affine.appendRotation(obj.getRot());
            affine.appendScale((obj.getFlipped()?-1:1),1);
            gc.setTransform(affine);

            gc.drawImage(obj.getImage(),-width/2,-height/2,width,height);

            if(Controller.debugging()){
                gc.fillRect(-view.getObjectSize()*obj.getSize().getX_dyn()/2,-view.getObjectSize()*obj.getSize().getY_dyn()/2,view.getObjectSize()*obj.getSize().getX_dyn(),view.getObjectSize()*obj.getSize().getY_dyn());
            }
        }
    }

    void drawGlobalParticleSystem(World world)
    {
        gc.setTransform(new Affine());
        gc.setStroke(Color.LIGHTBLUE);
        gc.setGlobalAlpha(0.6);
        double x;
        double y;

        for(GlobalParticleSystem globalParticleSystem : world.getGlobalParticleSystems())
            for(DynamicVector dynamicVector : globalParticleSystem.getParticles())
            {
                x = dynamicVector.getX_dyn()*view.getCanvasDim().getX();
                y = view.getCanvasDim().getY()-dynamicVector.getY_dyn()*view.getCanvasDim().getY();
                gc.strokeLine(x,y,
                        x-globalParticleSystem.getSpeed().getX_dyn()*25,
                        y+globalParticleSystem.getSpeed().getY_dyn()*25);
            }
    }

    void drawImageParticleSystem(World world)
    {
        for(ImageParticleSystem.Particle particle : world.getImageParticleSystem().getParticles())
        {
            double width = view.getWinScale()*(particle.getImage().getWidth()/ImageLibrary.imageLoadScale)/2;
            double height = view.getWinScale()*(particle.getImage().getHeight()/ImageLibrary.imageLoadScale)/2;
            double x_pos = particle.getPos().getX_dyn() * view.getObjectSize();
            double y_pos = (world.getWorldHeight() - particle.getPos().getY_dyn() - 1) * view.getObjectSize();
            gc.setGlobalAlpha(Math.min(0.20,particle.getLifetime())*5);
            Affine affine = new Affine();
            affine.appendTranslation(x_pos+view.getCameraPan().getX_dyn(),y_pos+view.getCameraPan().getY_dyn());
            affine.appendRotation(particle.getRot());
            gc.setTransform(affine);
            System.out.println(width);
            gc.drawImage(particle.getImage(),-width*0.5,-height*0.5,width,height);
        }
    }

    void drawDetails(World world)
    {
        gc.setGlobalAlpha(1);
        for(Detail detail : world.getDetails())
        {
            if(!detail.getVisible())continue;
            Affine affine = new Affine();
            double width = view.getWinScale()*detail.getImage().getWidth()/ImageLibrary.imageLoadScale;
            double height = view.getWinScale()*detail.getImage().getHeight()/ImageLibrary.imageLoadScale;
            double x_pos = detail.getPos().getX_dyn() * view.getObjectSize();
            double y_pos = (world.getWorldHeight() - detail.getPos().getY_dyn() - 1) * view.getObjectSize();
            affine.appendTranslation(x_pos+view.getCameraPan().getX_dyn(),y_pos+view.getCameraPan().getY_dyn());
            gc.setTransform(affine);
            gc.drawImage(detail.getImage(),-width/2d,-height/2d,width,height);
        }
    }

    void drawBackground(World world)
    {
        DynamicVector pan = getCameraInv();
        DynamicVector objectPan = new DynamicVector(pan.getX_dyn()/view.getObjectSize(),pan.getY_dyn()/view.getObjectSize());
        Affine affine = new Affine();
        gc.setTransform(affine);

        for(BackgroundElement backgroundElement : world.getBackgroundElements())
        {
            double width = view.getWinScale()*backgroundElement.getImage().getWidth()/ImageLibrary.imageLoadScale;
            double height = view.getWinScale()*backgroundElement.getImage().getHeight()/ImageLibrary.imageLoadScale;
            double x = backgroundElement.getPivot().getX_dyn()*view.getObjectSize()-objectPan.getX_dyn()*view.getObjectSize()*backgroundElement.getMoveScale().getX_dyn();
            double y = view.getCanvasDim().getY()-backgroundElement.getPivot().getY_dyn()*view.getObjectSize()+objectPan.getY_dyn()*view.getObjectSize()*backgroundElement.getMoveScale().getY_dyn()-height;
            if(backgroundElement.getRepeatX())
            {
                gc.drawImage(backgroundElement.getImage(),x,y,width,height);
            }
            else
            {
                for(double _x=x;_x<view.getCanvasDim().getX();_x+=width)
                {
                    gc.drawImage(backgroundElement.getImage(),_x,y,width,height);
                }
            }
        }

        /*
        for(double i=100-objectPan.getX_dyn()*view.getObjectSize();i<view.getCanvasDim().getX();i+=img.getWidth()) {
            gc.drawImage(img, i, view.getCanvasDim().getY()-img.getHeight());
        }
        */
    }

    void drawSky(World world)
    {
        gc.setFill(world.getSkyColor());
        gc.fillRect(0,0,view.getCanvasDim().getX(),view.getCanvasDim().getY());
    }

    void panCamera(World world, DynamicVector target,double delta)
    {
        DynamicVector panTarget = getPanTarget(world,target);

        double transx = -panTarget.getX_dyn()+view.getCanvasDim().getX()/2;
        double transy = -panTarget.getY_dyn()+view.getCanvasDim().getY()/2;

        //Reason for it being view.getObjectSize()/2 as the min is that blocks are drawn from their center, meaning they would be halfed, if view.getObjectSize()/2 was not there.
        view.getCameraPan().setX_dyn(Math.max(Math.min(view.getObjectSize()/2,view.getCameraPan().getX_dyn()+(transx-view.getCameraPan().getX_dyn())*7*delta),-view.getCameraPanBoundaries().getX()+view.getObjectSize()/2+view.getObjectSize()));
        view.getCameraPan().setY_dyn(Math.max(Math.min(view.getObjectSize()/2,view.getCameraPan().getY_dyn()+(transy-view.getCameraPan().getY_dyn())*7*delta),-view.getCameraPanBoundaries().getY()+view.getObjectSize()/2));
    }

    DynamicVector getPanTarget(World world, DynamicVector target)
    {
        return new DynamicVector(target.getX_dyn() * view.getObjectSize() - view.getObjectSize() / 2.0,
                (world.getWorldHeight() - target.getY_dyn() - 1) * view.getObjectSize() - view.getObjectSize() / 2.0);
    }

    void drawGrid(World world,DynamicVector gridSize)
    {
        Affine affine = new Affine();
        affine.appendTranslation(view.getCameraPan().getX_dyn(),view.getCameraPan().getY_dyn());
        gc.setTransform(affine);
        gc.setGlobalAlpha(0.25);
        double gridsPerUnitX = (1/gridSize.getX_dyn());
        double gridsPerUnitY = (1/gridSize.getY_dyn());
        for(int x=0;x<world.getWorldWidth()*gridsPerUnitX;x++)
        {
            for(int y=0;y<world.getWorldHeight()*gridsPerUnitY;y++)
            {
                //gc.setGlobalAlpha((x%gridsPerUnitX==0 && y%gridsPerUnitY==0)?1:0.2);
                gc.setFill(Color.GRAY);
                gc.strokeRect((x*view.getObjectSize())*gridSize.getX_dyn()-view.getObjectSize()/2,(y*view.getObjectSize())*gridSize.getY_dyn()-view.getObjectSize()/2,view.getObjectSize()*gridSize.getX_dyn(),view.getObjectSize()*gridSize.getY_dyn());
            }
        }
    }

    void drawTarget(World world, DynamicVector target, double time, EditorClass editorClass)
    {
        gc.setGlobalAlpha(0.6+0.2*Math.sin(Math.toRadians(time*90)));
        Affine affine = new Affine();
        double x = target.getX_dyn()*view.getObjectSize() + view.getCameraPan().getX_dyn();
        double y = (world.getWorldHeight()-1-target.getY_dyn())*view.getObjectSize() + view.getCameraPan().getY_dyn();
        affine.appendTranslation(x,y);
        gc.setTransform(affine);

        if(view.targetEffect())
        {
            gc.setFill(Color.GREY);
            double width = 2.5;
            gc.setLineWidth(width*2);

            int frame = ((int)(time*8)) % 4;
            System.out.println(frame);
            switch(frame)
            {
                case 0:
                    gc.strokeLine(-view.getObjectSize()*0.5,-view.getObjectSize()*0.5+width,-view.getObjectSize()*0.5,view.getObjectSize()*0.5-width);
                    break;
                case 1:
                    gc.strokeLine(-view.getObjectSize()*0.5+width,view.getObjectSize()*0.5,view.getObjectSize()*0.5-width,view.getObjectSize()*0.5);
                    break;
                case 2:
                    gc.strokeLine(view.getObjectSize()*0.5,view.getObjectSize()*0.5-width,view.getObjectSize()*0.5,-view.getObjectSize()*0.5+width);
                    break;
                case 3:
                    gc.strokeLine(view.getObjectSize()*0.5-width,-view.getObjectSize()*0.5,-view.getObjectSize()*0.5+width,-view.getObjectSize()*0.5);
                    break;
            }
        }
        else
        {
            gc.drawImage(editorClass.getImage(),-view.getObjectSize()/2.0,-view.getObjectSize()/2.0,view.getObjectSize(),view.getObjectSize());
        }
    }

    void drawSpawn(World world)
    {
        gc.setGlobalAlpha(1);//gc.setGlobalAlpha(0.6+0.2*Math.sin(Math.toRadians(EditorStates.time*90)));
        Affine affine = new Affine();
        DynamicVector target = world.getPlayerSpawnPoint();
        affine.appendTranslation(target.getX()*view.getObjectSize() + view.getCameraPan().getX_dyn() - view.getObjectSize()/2,(world.getWorldHeight()-1-target.getY())*view.getObjectSize() + view.getCameraPan().getY_dyn()- view.getObjectSize()/2);
        gc.setTransform(affine);
        gc.drawImage(ImageLibrary.getImage("char_idle.png"),0,0,view.getObjectSize(),view.getObjectSize());
    }

    void drawTileWindow(int elPerLine, int elLines, Vector target, Vector selected, EditorClass[] editorClasses)
    {
        gc.setTransform(new Affine());
        gc.setGlobalAlpha(1);
        gc.setFill(Color.GRAY);
        double x = view.getCanvasDim().getX()/6;
        double y = view.getCanvasDim().getY()/6;
        double width = 2*view.getCanvasDim().getX()/3;
        double height = 2*view.getCanvasDim().getY()/3;
        gc.fillRect(x,y,width,height);
        double elDist = 16;
        double elBoxDist = 4;
        double targetWidth = 4;
        double selectedWidth = 6;
        //int elPerLine = 10;
        //int elLines = 4;
        double elWidth = (width-elDist)/elPerLine-elDist;
        double elHeight = elWidth;
        //double elHeight = (height-elDist)/elLines-elDist;
        //Color[] colors = new Color[]{Color.GREEN,Color.RED,Color.YELLOW,Color.BLACK,Color.BLUE,Color.ORANGE};
        //Vector target = new Vector(2,2);
        int elementsDrawn = 0;
        for(int h=0;h<elLines;h++) {
            for (int w = 0; w < elPerLine; w++) {
                if(elementsDrawn >= editorClasses.length){return;}

                //gc.setFill(colors[w+h*elPerLine]);
                if(h==selected.getY()&& w==selected.getX()){
                    gc.setFill(Color.ORANGE);
                    gc.fillRect(x+elDist-selectedWidth+(elDist+elWidth)*w,y+elDist-selectedWidth+(elDist+elHeight)*h,elWidth+selectedWidth*2,elHeight+selectedWidth*2);
                }
                if(h==target.getY()&& w==target.getX()){
                    gc.setFill(Color.YELLOW);
                    gc.fillRect(x+elDist-targetWidth+(elDist+elWidth)*w,y+elDist-targetWidth+(elDist+elHeight)*h,elWidth+targetWidth*2,elHeight+targetWidth*2);
                }
                gc.setFill(Color.WHITE);
                gc.fillRect(x+elDist+(elDist+elWidth)*w,y+elDist+(elDist+elHeight)*h,elWidth,elHeight);
                gc.drawImage(editorClasses[elementsDrawn].getImage(),
                        x+elDist+(elDist+elWidth)*w+elBoxDist,
                        y+elDist+(elDist+elHeight)*h+elBoxDist,
                        elWidth-elBoxDist*2,
                        elHeight-elBoxDist*2);
                elementsDrawn += 1;
            }
        }
    }

    void drawDebug()
    {

    }

    DynamicVector getCameraInv() {
        return new DynamicVector(-view.getCameraPan().getX_dyn(), view.getCameraPan().getY_dyn() + view.getCameraPanBoundaries().getY_dyn());
    }

}
