package client;

import ballthrower.interactive.Ball;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Joe on 05/03/2017.
 */
public class Client {

    private Socket clientSocket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;
    private String host;
    private Integer port = 6066;
    private Pane pane;
    // This is shared by all the components, allows the client to add balls to the graphics.
    private ArrayList<Ball> balls;

    public boolean RUNNING = true;

    public Client(Pane pane) {
        this.pane = pane;
    }

    /**
     * Takes the IP from the dialog, and uses it to connect the clent to the right server.
     * @param ip
     */
    public void start(String ip) {
        try {
            host = ip;
            clientSocket = new Socket(host, port);
            System.out.println("connected");
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Looks from messages from the server while running.
     */
    private void run() {
        while(RUNNING){
            try {
                if(inFromServer.ready()){
                    addBall(inFromServer.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addBall(String ballDescription) {
        balls.add(new Ball(ballDescription, pane));
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void sendBall(String sendable) {
        try {
            outToServer.writeBytes(sendable+"\n");
        } catch (IOException e) {
            stop();
            new Thread(()->addBall(sendable)).start();
        }
    }

    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    public void stop(){
        RUNNING = false;
        try {
            outToServer.close();
            clientSocket.close();
            inFromServer.close();
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
