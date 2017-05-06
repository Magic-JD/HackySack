package client;

import ballthrower.PanelLauncher;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by User on 01/03/2017.
 */
public class ClientLauncher extends Application {

    public static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static Pane pane;
    private static String ip;

    public static void main(String[] args){
        ip = JOptionPane.showInputDialog("IP to use");
        if(ip.equals("")){
            ip = "localhost";
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);
        pane = new Pane();
        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        scene.setFill(null);
        primaryStage.setScene(scene);
        primaryStage.show();
        Client client = new Client(pane);
        Thread clientThread = new Thread(() -> client.start(ip));
        clientThread.start();

        PanelLauncher panelLauncher = new PanelLauncher(client, pane);

        Thread guiThread = new Thread(()-> panelLauncher.run());

        guiThread.start();
        primaryStage.show();

        Thread thread = new Thread(() -> {
            JFrame frame = new JFrame();
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    client.RUNNING = false;
                    panelLauncher.stop();
                    client.stop();
                }
            });
            frame.setVisible(true);
            frame.setSize(new Dimension(400, 10));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setTitle("CLIENT RUNNING");
        });
        thread.start();
    }
}
