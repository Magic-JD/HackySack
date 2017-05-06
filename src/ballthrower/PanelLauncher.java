package ballthrower;

import ballthrower.interactive.Ball;
import ballthrower.interactive.BallAdder;
import ballthrower.updation.BallLocationUpdater;
import client.Client;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by User on 01/03/2017.
 */
public class PanelLauncher {

    public static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static int WAIT = 100;

    private Client client;
    private Timer timer;
    private Pane pane;
    private ArrayList<Ball> balls;
    private BallAdder ballAdder;

    public PanelLauncher(Client client, Pane pane) {
        this.client = client;

        this.pane = pane;
    }

    public void run(){
        pane.setOnMouseClicked(e-> changeBallColors());
        balls = new ArrayList<>();
        ballAdder = new BallAdder(balls, pane);
        Platform.runLater(() -> pane.getChildren().add(ballAdder));
        timer = new Timer(WAIT, new BallLocationUpdater(this));
        timer.start();
        client.setBalls(balls);
    }

    private void changeBallColors() {
        for (Ball ball : balls) {
            ball.incrementColorSelection();
        }
    }

    public void stop() {
        timer.stop();
        Platform.exit();
    }

    public void removeBall(Ball ball, boolean send) {
        if (send) {
            client.sendBall(ball.toString());
        }
        Platform.runLater(() -> pane.getChildren().remove(ball));
    }

    public boolean shouldDelete(Ball ball) {
        return ball.intersects(ballAdder.getBoundsInLocal());
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }
}
