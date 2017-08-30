package Views.Groups;

import Vectors.DynamicVector;
import Vectors.Vector;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Created by kristoffer on 22-08-2017.
 */
public class EndScreenGroup extends Group {

    Font endTitleFont = Font.font("Impact", FontWeight.BOLD,48);
    Font endBottomFont = Font.font("Tahoma",FontWeight.LIGHT,20);
    Font endTimeFont = Font.font("Tahoma",FontWeight.BOLD,48);
    Font endGradeFont = Font.font("Impact",FontWeight.BLACK,70);

    Circle endSpot;
    Rectangle endDark;
    Text endTime;
    Text endGrade;

    public EndScreenGroup(Vector canvasDim)
    {
        RadialGradient radialGradient = new RadialGradient(0,
                .0,
                450,
                300,
                150,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(1, Color.WHITE),
                new Stop(1, new Color(1,1,1,0)));

        endSpot = new Circle(450,300,100);
        endSpot.setFill(radialGradient);

        endDark = new Rectangle(0,0,canvasDim.getX_dyn(),canvasDim.getY_dyn());
        endDark.setFill(new Color(0, 0, 0, 1));

        Text endTitle = new Text(275,100,"LEVEL COMPLETE");
        endTitle.setFont(endTitleFont);
        endTitle.setFill(Color.WHITE);
        //endTitle.setTextAlignment(TextAlignment.CENTER);

        endTime = new Text(75,100,"25,2");
        endTime.setFont(endTimeFont);
        endTime.setFill(Color.WHITE);

        endGrade = new Text(canvasDim.getX_dyn()-150,120,"S");
        endGrade.setFont(endGradeFont);
        endGrade.setFill(Color.WHITE);

        Rectangle endGradeRectangle = new Rectangle(canvasDim.getX_dyn()-150-20,120-70,50+25,65+25);
        endGradeRectangle.setFill(Color.TRANSPARENT);
        endGradeRectangle.setStroke(Color.WHITE);
        endGradeRectangle.setStrokeWidth(10);

        Text endBottomText = new Text(325,canvasDim.getY_dyn()-80,"Press any key to continue");
        endBottomText.setFont(endBottomFont);
        endBottomText.setFill(Color.WHITE);

        setBlendMode(BlendMode.MULTIPLY);
        getChildren().add(endDark);
        getChildren().add(endSpot);
        getChildren().add(endTitle);
        getChildren().add(endTime);
        getChildren().add(endGrade);
        getChildren().add(endGradeRectangle);
        getChildren().add(endBottomText);
    }

    public Circle getEndSpot() {
        return endSpot;
    }

    public Rectangle getEndDark() {
        return endDark;
    }

    public Text getEndTimeText() {
        return endTime;
    }

    public Text getEndGrade() {
        return endGrade;
    }

}
