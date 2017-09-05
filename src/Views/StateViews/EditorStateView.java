package Views.StateViews;

import Controllers.Controller;
import Libraries.EditorLibrary;
import States.EditorStates.EditorMode;
import States.EditorStates.EditorState;
import States.GameStates.GameState;
import States.IState;
import Views.View;
import javafx.scene.canvas.GraphicsContext;
import org.omg.CosNaming.IstringHelper;

/**
 * Created by kristoffer on 22-08-2017.
 */
public class EditorStateView extends StateView {

    public EditorStateView(View view) {
        super(view);
    }

    @Override
    public void draw(IState state,double delta)
    {
        EditorState editorState = (EditorState)state;

        view.setWinScale(editorState.getZoomScale(),editorState.getWorld());
        panCamera(editorState.getWorld(), editorState.getCameraPivot(),delta);
        if(editorState.getGridEnabled())drawGrid(editorState.getWorld(),editorState.getGridSize());
        drawBlocks(editorState.getWorld());
        drawDynamics(editorState.getWorld());
        drawDecorations(editorState.getWorld());
        drawDetails(editorState.getWorld());

        drawTarget(editorState.getWorld(), editorState.getWorldTarget(),editorState.getTime(),editorState.getObjTypeSelected());
        drawSpawn(editorState.getWorld());

        if(Controller.debugging()){
            drawDebug();
        }

        if (editorState.getEditorMode() == EditorMode.ObjectSelect) {
            drawTileWindow(10, 4, editorState.getObjectMenuTarget(), editorState.getObjectMenuSelected(),
                    EditorLibrary.getEditorClasses(editorState.getObjTypeGroup()));
        }
    }

    @Override
    public void start(GraphicsContext gc) {
        super.start(gc);
    }

    @Override
    public void end() {

    }

}
