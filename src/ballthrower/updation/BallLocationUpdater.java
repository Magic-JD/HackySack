package ballthrower.updation;

import ballthrower.PanelLauncher;
import ballthrower.interactive.Ball;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by User on 06/05/2017.
 */
public class BallLocationUpdater  implements ActionListener {

    private PanelLauncher panelLauncher;
    private ArrayList<Ball> balls;

    public BallLocationUpdater(PanelLauncher panelLauncher){
        this.panelLauncher = panelLauncher;
        balls = panelLauncher.getBalls();
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        synchronized (balls) {
            Iterator<Ball> iter = balls.iterator();
            while (iter.hasNext()) {
                Ball ball = iter.next();
                if (!ball.isPressed()) {
                    ball.redraw();
                    if (ball.offScreen()) {
                        panelLauncher.removeBall(ball, true);
                        iter.remove();
                    }

                    ball.addVelocity();
                } else {
                    if (panelLauncher.shouldDelete(ball)) {
                        panelLauncher.removeBall(ball, false);
                        iter.remove();
                    }
                }
            }
        }
    }
}