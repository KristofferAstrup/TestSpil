package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;

/**
 * Created by Kris on 08-02-2017.
 */
public class ViewOld extends JPanel {
/*
    private JFrame window;
    private IState.GameState.IState.GameState gameState;
    private Dimension screenSize;
    private double screenRelation;
    private Vectors.DynamicVector currentTarget;
    private int objectSizeBase = 32;
    private int objectSize;
    private double tranScaleBase = 2;
    private double tranScale;
    private double drawScale = 1;
    private double zoomScale;
    private double wantedZoomScale = 1;

    private Vectors.DynamicVector cameraPan = new Vectors.DynamicVector(0,0);

    public View.ViewOld()
    {
        screenSize = new Dimension(600,400);
        screenRelation = screenSize.getWidth()/screenSize.getHeight();

        window = new JFrame(":)");
        window.setLayout(new BorderLayout());
        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e)
            {
                updateZoomScale();
            }
        });

        setPreferredSize(screenSize);
        window.getContentPane().add(this);

        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        System.out.println(window.getContentPane().getWidth() + " | " + window.getContentPane().getHeight());

        currentTarget = new Vectors.DynamicVector(0,0);

        setDrawScale(drawScale);
        updateZoomScale();
    }

    public JFrame getWindow()
    {
        return window;
    }

    private void updateZoomScale()
    {
        if(window.getContentPane().getHeight()*screenRelation <= window.getContentPane().getWidth()) {
            zoomScale = wantedZoomScale*window.getContentPane().getWidth() / screenSize.getWidth();
        }
        if(window.getContentPane().getHeight()*screenRelation > window.getContentPane().getWidth()) {
            zoomScale = wantedZoomScale*window.getContentPane().getHeight() / screenSize.getHeight();
        }
    }

    public void setDrawScale(double drawScale)
    {
        this.drawScale = drawScale;
        tranScale = tranScaleBase * drawScale;
        objectSize = (int)(objectSizeBase * drawScale);
    }

    public void setZoomScale(double zoomScale)
    {
        this.zoomScale = zoomScale;
    }

    public double getZoomScale()
    {
        return zoomScale;
    }

    public void update(IState.GameState.IState.GameState gameState) {
        this.gameState = gameState;
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        if(gameState == null){return;}
        if(gameState.getWorld() == null){return;}

        g2d.scale(zoomScale,zoomScale);

        panCamera(gameState.getPlayer().getPos(),g2d);

        setBackground(new Color(222, 255, 253));
        //drawBackground(g2d);
        drawDynamicObjects(g2d);
        drawBlocks(g2d);
        drawGUI(gameState.getPlayer().getPos(),g2d);
    }

    private void drawBackground(Graphics2D g2d)
    {
        AffineTransform trans = new AffineTransform();
        Image bgn = new ImageIcon("Resources/Images/DirtBgn.png").getImage();
        for(int x=0;x<gameState.getWorld().getBlocks().length;x++){
            //System.out.println(gameState.getWorldObjects()[x].length);
            for(int y=0;y<gameState.getWorld().getBlocks()[x].length;y++){
                int x_pos = x*objectSize-objectSize/2;
                int y_pos = (gameState.getWorld().getBlocks()[x].length-y-1)*objectSize-objectSize/2;
                trans = new AffineTransform();
                trans.translate(x_pos,y_pos);
                trans.scale(tranScale,tranScale);
                g2d.drawImage(bgn,trans,this);
            }
        }
    }

    private void drawBlocks(Graphics2D g2d)
    {
        AffineTransform trans = new AffineTransform();
        for(int x=0;x<gameState.getWorld().getBlocks().length;x++){
            //System.out.println(gameState.getWorldObjects()[x].length);
            for(int y=0;y<gameState.getWorld().getBlocks()[x].length;y++){
                if(gameState.getWorld().getBlocks()[x][y] != null) {

                    Image img = gameState.getWorld().getBlocks()[x][y].getImage();
                    trans = new AffineTransform();
                    double rot = Math.toRadians(gameState.getWorld().getBlocks()[x][y].getRot());
                    int x_pos = x*objectSize-objectSize/2;
                    int y_pos = (gameState.getWorld().getBlocks()[x].length-y-1)*objectSize-objectSize/2;
                    trans.translate(x_pos,y_pos);
                    trans.rotate(rot,objectSize/2,objectSize/2);
                    trans.scale(tranScale,tranScale);

                    g2d.drawImage(img,trans,this);
                }
            }
        }
    }

    private void drawDynamicObjects(Graphics2D g2d)
    {
        AffineTransform trans = new AffineTransform();
        for(World.WorldObject.DynamicObject.DynamicObject obj : gameState.getWorld().getDynamicObjects())
        {
            trans = new AffineTransform();
            double x_pos = obj.getPos().getX_dyn() * objectSize - objectSize / 2.0;
            double y_pos = (gameState.getWorld().getWorldHeight() - obj.getPos().getY_dyn() - 1) * objectSize - objectSize / 2.0;
            trans.translate(x_pos-(obj.getFlipped()?-objectSize:0), y_pos);
            trans.scale(tranScale*(obj.getFlipped()?-1:1),tranScale);
            g2d.drawImage(obj.getImage(), trans, this);
        }
    }


    private void drawGUI(Vectors.DynamicVector target,Graphics2D g2d)
    {
        AffineTransform trans = new AffineTransform();
        double x = -cameraPan.getX_dyn();
        double y = -cameraPan.getY_dyn();
        trans.translate(x,y);
        trans.setToScale(1,1);
        g2d.setTransform(trans);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,window.getContentPane().getWidth(),50);
        g2d.setColor(Color.WHITE);
        g2d.drawString("player_x = " + gameState.getPlayer().getPos().getX(),25,25);
        g2d.drawString("player_y = " + gameState.getPlayer().getPos().getY(),25,50);
        //g2d.drawString("fps = " + Controller.Main.globalFps,150,15);
    }

    private void panCamera(Vectors.DynamicVector target,Graphics2D g2d)
    {
        //currentTarget.add(target.subtract(currentTarget));

        double winScale = 1.0/zoomScale;

        Vectors.DynamicVector panTarget = getPanTarget(target);

        double transx = -panTarget.getX_dyn()+window.getContentPane().getWidth()*winScale/2-objectSize/2;
        double transy = -panTarget.getY_dyn()+window.getContentPane().getHeight()*winScale/2-objectSize/2;

        cameraPan.setX_dyn(Math.max(Math.min(0,transx),-gameState.getWorld().getWorldWidth()*objectSize+objectSize+window.getContentPane().getWidth()*winScale));
        cameraPan.setY_dyn(Math.max(Math.min(0,transy),-gameState.getWorld().getWorldHeight()*objectSize+objectSize+window.getContentPane().getHeight()*winScale));

        g2d.translate(cameraPan.getX_dyn(), cameraPan.getY_dyn());
    }

    private Vectors.DynamicVector getPanTarget(Vectors.DynamicVector target)
    {
        return new Vectors.DynamicVector(target.getX_dyn() * objectSize - objectSize / 2.0,
        (gameState.getWorld().getWorldHeight() - target.getY_dyn() - 1) * objectSize - objectSize / 2.0);
    }
*/
}
