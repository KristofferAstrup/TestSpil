package Views.Groups;

import Controllers.Controller;
import Vectors.Vector;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DebugGroup extends Group {

    private final int prefWidth = 350;
    private final int prefInputHeight = 30;
    //private final int prefButtonWidth = 75;
    private final int textBorder = 8;

    final private Rectangle rectangle = new Rectangle();
    //final private Button submitButton = new Button("Submit");
    final private Label outputLabel = new Label("");
    final private TextField textField = new TextField();

    public DebugGroup() {

        getChildren().add(rectangle);
        getChildren().add(textField);
        //getChildren().add(submitButton);
        getChildren().add(outputLabel);

    }

    public void updateLayout(Vector bounds)
    {
        rectangle.setWidth(prefWidth);
        rectangle.setHeight(bounds.getY());
        rectangle.setFill(Color.BLACK);

        textField.setPrefWidth(prefWidth);
        textField.setPrefHeight(prefInputHeight);
        textField.setLayoutX(0);
        textField.setLayoutY(bounds.getY() - prefInputHeight);

        //submitButton.setPrefWidth(prefButtonWidth);
        //submitButton.setPrefHeight(prefInputHeight);
        //submitButton.setLayoutX(prefWidth-prefButtonWidth);
        //submitButton.setLayoutY(bounds.getY() - prefInputHeight);

        outputLabel.setPrefWidth(prefWidth-textBorder*2);
        outputLabel.setPrefHeight(bounds.getY()-prefInputHeight-textBorder*2);
        outputLabel.setLayoutX(textBorder);
        outputLabel.setLayoutY(textBorder);
        outputLabel.setWrapText(true);
        outputLabel.setAlignment(Pos.BOTTOM_LEFT);
        outputLabel.setStyle("-fx-text-fill: green;");
    }

    public void updateOutPutLabel()
    {
        String outputText = "";
        for(int i=0;i<Controller.getDebugController().getOutput().size();i++)
        {
            outputText += Controller.getDebugController().getOutput().get(i);
        }
        /*for(int i=Controllers.getDebugController().getOutput().size()-1;i>=0;i--)
        {
            outputText += Controllers.getDebugController().getOutput().get(i) + "\n";
        }*/
        outputLabel.setText(outputText);
    }
    
    //public Button getSubmitButton(){return submitButton;}
    public TextField getTextField(){return textField;}
    public Label getOutputLabel(){return outputLabel;}

}
