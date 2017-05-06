package ballthrower.interactive;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;

import static ballthrower.PanelLauncher.WAIT;

/**
 * Created by User on 02/05/2017.
 */
public class BallAdder extends Rectangle {

    public BallAdder(ArrayList<Ball> balls, Pane pane) {
        setFill(Color.BLUE);
        setX(0);
        setY(0);
        setWidth(100);
        setHeight(10);
        setOnDragDetected((e) ->
                new Thread(() -> {
                    while (this.isPressed()) {
                        setX(MouseInfo.getPointerInfo().getLocation().getX());
                        try {
                            Thread.sleep(WAIT);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }).start());
        setOnMouseClicked((e)-> balls.add(new Ball(pane, 200, 200)));
    }
}
