package server;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static server.Server.RUNNING;

/**
 * Created by User on 04/03/2017.
 */
public class ServerHelper extends Thread {

    private final Socket clientSocket;
    private BufferedReader fromClient;
    private DataOutputStream toClient;


    public ServerHelper(Socket socket) {
        super("Server Thread");
        System.out.println("ServerHelper activated");
        clientSocket = socket;
        try {
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toClient = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        System.out.println("ServerHelper thread running");
        try {
            while (RUNNING) {
                if (fromClient.ready()) {
                    if (Server.serverHelperArrayList.size() == 1) {
                        sendBall(fromClient.readLine());
                    } else {
                        for (ServerHelper sh : Server.serverHelperArrayList) {
                            if (!sh.equals(this)) {
                                sh.sendBall(fromClient.readLine());
                            }
                        }
                    }
                } else {Thread.sleep(100);}
            }
        } catch (IOException | InterruptedException e) {
            Server.removeHelper(this);
        }
    }

    private void sendBall(String s) {
        try {
            toClient.writeBytes(s + "\n");
        } catch (IOException e) {
            for (ServerHelper sh : Server.serverHelperArrayList) {
                if (!sh.equals(this)) {
                    sh.sendBall(s);
                }
            }
            Server.removeHelper(this);
            shutDown();
        }
    }

    public void shutDown() {
        try {
            toClient.close();
            fromClient.close();
            clientSocket.close();
        } catch (IOException e) {
            new JDialog(new JFrame(), "Server did not shut down properly!");
        }
    }
}
