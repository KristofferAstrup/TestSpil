package Views.StateViews;

import Libraries.ImageLibrary;
import States.GameStates.GameState;
import States.IState;
import Vectors.DynamicVector;
import Views.Groups.EndScreenGroup;
import Views.View;
import Worlds.World;
import Worlds.WorldObjects.DynamicObjects.PhysicObjects.Mobs.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;

import java.text.DecimalFormat;

/**
 * Created by kristoffer on 22-08-2017.
 */
public class GameStateView extends StateView {

    private DecimalFormat timeDecimalFormat = new DecimalFormat("0.0");

    private EndScreenGroup endScreenGroup;

    private final double finalSpotRadius = 1.75;

    private double playerEndAnimationTimer;
    private final double playerEndAnimationTime = 2.75;
    private final double playerEndAnimationTransition = 1.5;

    private double startTransitionTimer;
    private final double startTransitionTime = 0.5;

    private double startBannerTimer;
    private final double startBannerTime = 1;
    private final double bannerHeight = 100;
    private final double startBannerTransition = 0.25;

    public GameStateView(View view) {
        super(view);
    }

    @Override
    public void draw(IState state,double delta)
    {
        GameState gameState = (GameState)state;

        view.setWinScale(2,gameState.getWorld());
        panCamera(gameState.getWorld(),gameState.getPlayer().getPos());
        drawSky(gameState.getWorld());
        drawBackground(gameState.getWorld());
        drawBlocks(gameState.getWorld());
        drawDynamics(gameState.getWorld());
        if(gameState.getLevelComplete())drawEndPlayer(gameState.getWorld(),gameState.getPlayer(),playerEndAnimationTimer);
        drawDecorations(gameState.getWorld());
        drawGlobalParticleSystem(gameState.getWorld());
        drawImageParticleSystem(gameState.getWorld());
        drawDetails(gameState.getWorld());
        if(startBannerTimer>0){
            drawStartBanner(startBannerTimer);
            startBannerTimer -= delta;
        }
        if(startTransitionTimer>0){
            drawStartTransition(startTransitionTimer);
            startTransitionTimer -= delta;
        }

        Affine a = new Affine();
        a.appendTranslation(50,50);
        gc.setTransform(a);

        gc.setFont(new Font("Verdana",24));
        gc.fillText(timeDecimalFormat.format(gameState.getTime()),0,0);

        if(gameState.getLevelComplete()){
            if(!view.getRoot().getChildren().contains(endScreenGroup))
            {
                view.getRoot().getChildren().add(endScreenGroup);

                double radius = Math.sqrt(Math.pow(view.getCanvasDim().getX_dyn(),2)+Math.pow(view.getCanvasDim().getY_dyn(),2));
                endScreenGroup.getEndSpot().setRadius(radius);

                DynamicVector goalPos = gameState.getWorld().getGoal().getPos();
                DynamicVector pos = new DynamicVector(goalPos.getX_dyn() * view.getObjectSize()+view.getCameraPan().getX_dyn(), (gameState.getWorld().getWorldHeight() - goalPos.getY_dyn() - 1) * view.getObjectSize()+view.getCameraPan().getY_dyn());

                endScreenGroup.getEndSpot().setCenterX(pos.getX_dyn());
                endScreenGroup.getEndSpot().setCenterY(pos.getY_dyn());
            }

            /*double radius = Math.sqrt(Math.pow(canvasDim.getX_dyn(),2)+Math.pow(canvasDim.getY_dyn(),2));
            double time = 1;
            endScreenGroup.getEndSpot().setRadius(Math.max(0,radius*Math.pow(1-gameState.getTime()/time,2)));*/

            endScreenGroup.getEndSpot().setRadius(endScreenGroup.getEndSpot().getRadius() + (finalSpotRadius*view.getObjectSize()-endScreenGroup.getEndSpot().getRadius())*5*delta);

            if(playerEndAnimationTimer > 0){
                playerEndAnimationTimer -= delta;
                if(playerEndAnimationTimer < 0){
                    playerEndAnimationTimer = 0;
                }
            }

        }

    }

    @Override
    public void start(GraphicsContext gc) {
        super.start(gc);
        endScreenGroup = new EndScreenGroup(view.getCanvasDim());
        playerEndAnimationTimer = playerEndAnimationTime;
        startTransitionTimer = startTransitionTime;
        startBannerTimer = startBannerTime;
    }

    @Override
    public void end() {
        view.getRoot().getChildren().remove(endScreenGroup);
    }

    private void drawEndPlayer(World world,Player player, double timer)
    {
        gc.setTransform(new Affine());

        double width = view.getObjectSize()*player.getScale().getX_dyn();
        double height = view.getObjectSize()*player.getScale().getY_dyn();

        DynamicVector goalPos = world.getGoal().getPos();
        DynamicVector pos = new DynamicVector(goalPos.getX_dyn() * view.getObjectSize()+view.getCameraPan().getX_dyn(), (world.getWorldHeight() - goalPos.getY_dyn() - 1) * view.getObjectSize()+view.getCameraPan().getY_dyn());
        pos.setAdd(0,-height/2+view.getObjectSize()*world.getGoal().getSize().getY_dyn()/2.0);

        if(timer > playerEndAnimationTransition)
        {
            gc.drawImage(ImageLibrary.getImage("char_win"),pos.getX_dyn()-width/2,pos.getY_dyn()-height/2,width,height);
        }
        else
        {
            double scale = timer/playerEndAnimationTransition;
            int imageIndex = (int)(scale*10)%2;

            gc.setEffect(new ColorAdjust(0,0,scale-1,0));
            gc.drawImage(ImageLibrary.getImage("char_back_walk" + imageIndex),pos.getX_dyn()-width/2,pos.getY_dyn()-height/2-(1-scale)*view.getObjectSize()*0.45,width,height);
            gc.setEffect(null);
        }

    }

    private void drawStartBanner(double timer)
    {
        gc.setTransform(new Affine());
        gc.setFill(Color.BLACK);

        double y = 0;
        if(timer < startBannerTransition){
            y = -bannerHeight*Math.pow(1-timer/startBannerTransition,2.5);
        }

        gc.fillRect(0,y,view.getCanvasDim().getX_dyn(),bannerHeight);

    }

    private void drawStartTransition(double timer)
    {
        gc.setTransform(new Affine());
        gc.setGlobalAlpha(timer/startTransitionTime);
        gc.setFill(Color.BLACK);

        gc.fillRect(0,0,view.getCanvasDim().getX_dyn(),view.getCanvasDim().getY_dyn());

        gc.setGlobalAlpha(1);
    }

}
