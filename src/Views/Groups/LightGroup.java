package Views.Groups;

/**
 * Created by kristoffer on 22-08-2017.
 */
public class LightGroup {
    /*BorderPane p = new BorderPane();
        p.setPrefSize(900,600);

        String msg = "Spytstegte brunkager i sursødsovs";

        HBox letters = new HBox(0);

        for(int i=0;i<msg.length();i++){
            Text letter = new Text(msg.charAt(i) + "");
            letter.setFill(Color.DARKRED);
            letters.getChildren().add(letter);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(1),letter);
            tt.setDelay(Duration.millis(i*50));
            tt.setToY(15);
            tt.setToX(2);
            tt.setAutoReverse(true);
            tt.setCycleCount(TranslateTransition.INDEFINITE);
            tt.play();
        }

        //p.getChildren().add(letters);
        p.setCenter(letters);
        root.getChildren().add(p);

        System.out.println("WIDTH: " + canvas.getWidth());*/



        /*RadialGradient radialGradient = new RadialGradient(0,
                .0,
                150,
                150,
                100,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, new Color(1, 0.5725, 0.5961, 1)),
                new Stop(1, new Color(1,1,1,0)));

        Circle circle = new Circle(150,150,100);
        circle.setFill(radialGradient);

        RadialGradient radialGradient2 = new RadialGradient(0,
                .0,
                450,
                300,
                150,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(0.1, Color.WHITE),
                new Stop(1, new Color(1,1,1,0)));

        Circle circle2 = new Circle(450,300,150);
        circle2.setFill(radialGradient2);

        Rectangle rectangle = new Rectangle(0,0,900,900);
        rectangle.setFill(new Color(0, 0, 0, 0.95));

        Group g = new Group();
        g.setBlendMode(BlendMode.MULTIPLY);
        g.getChildren().add(rectangle);
        g.getChildren().add(circle);
        g.getChildren().add(circle2);

        root.getChildren().add( g );*/
}
