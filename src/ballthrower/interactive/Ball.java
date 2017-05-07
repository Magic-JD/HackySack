package ballthrower.interactive;

import ballthrower.PanelLauncher;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.Random;

import static ballthrower.PanelLauncher.WAIT;
import static ballthrower.PanelLauncher.WIDTH;

/**
 * Created by User on 01/03/2017.
 */
public class Ball extends Circle {

    private static final double REVERSAL_VELOCITY = -0.8;
    public static final double BOUNCE_SLOW = 0.8;
    private Pane pane;
    private Color paint;
    private double radius;
    private double xVelocity;
    private double yVelocity;
    private double gravity;
    private String text;

    public Ball(Pane pane, int x, int y) {
        this.pane = pane;
        xVelocity = 0;
        yVelocity = 0;
        gravity = 0;
        paint = randomColour();
        radius = 50;
        text = "Hello there!";
        init(x, y);
    }

    /**
     * Generates the ball from a string sent over from the server.
     * @param s the string description of the ball.
     * @param pane the pane that contains the balls.
     */
    public Ball(String s, Pane pane) {
        this.pane = pane;
        String[] components = s.split(" ");
        double y = Double.parseDouble(components[0]);
        double x = radiusProperty().doubleValue();
        paint = Color.color(Float.parseFloat(components[1]), Float.parseFloat(components[2]), Float.parseFloat(components[3]));
        this.radius = Double.parseDouble(components[4]);
        this.xVelocity = Double.parseDouble(components[5]);
        this.yVelocity = Double.parseDouble(components[6]);
        this.gravity = Double.parseDouble(components[7]);
        this.text = components[8];
        init(x, y);
    }

    private void init(double x, double y){
        setCenterX(x);
        setCenterY(y);
        setRadius(radius);
        setFill(paint);
        Platform.runLater(() -> pane.getChildren().add(this));
        setOnMousePressed(e -> {
            Runnable runnable = () -> {
                while(isPressed()){
                    runClickedOnUpdater();
                }
            };
            new Thread(runnable).start();
        });
    }


    private void runClickedOnUpdater() {
        Point point = MouseInfo.getPointerInfo().getLocation();
        setCenterY(point.getY());
        setCenterX(point.getX());
        try {
            Thread.sleep(WAIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Point newPoint = MouseInfo.getPointerInfo().getLocation();
        calculateVelocity(point, newPoint);
    }

    private Color randomColour() {
        Random r = new Random();
        return Color.color(r.nextFloat(), r.nextFloat(), r.nextFloat());
    }

    public void incrementColorSelection() {
        paint = randomColour();
        setFill(paint);
    }

    public void moveXwards(double xShift) {
        setCenterX(getCenterX()+ xShift);
    }

    public void moveYwards(double yShift) {
        setCenterY(getCenterY()+ yShift);    }

    public void redraw() {
        if (getCenterX() <= radius) {
            setCenterX(radius);
            xVelocity *= REVERSAL_VELOCITY;
        }
        if (getCenterY() <= radius) {
            setCenterY(radius);
            yVelocity *= REVERSAL_VELOCITY;
        }
        if (getCenterY() >= PanelLauncher.HEIGHT - radius) {
            setCenterY(PanelLauncher.HEIGHT - radius);
            yVelocity *= REVERSAL_VELOCITY;
            xVelocity *= BOUNCE_SLOW;
            gravity = 0;
        }
    }

    public void addVelocity() {
        moveXwards(xVelocity);
        moveYwards(yVelocity + gravity++);
    }

    public void calculateVelocity(Point currentPoint, Point newPoint) {
        xVelocity = newPoint.getX() - currentPoint.getX();
        yVelocity = newPoint.getY() - currentPoint.getY();
    }

    public boolean offScreen() {
        return getCenterX() >= WIDTH - radius;
    }

    @Override
    public String toString(){
        return ""+getCenterY()+" "+paint.getRed()+" "+paint.getGreen()+" "+paint.getBlue()+" "+ radius +" "+xVelocity+" "+yVelocity+" "+gravity+" "+text;
    }
}
