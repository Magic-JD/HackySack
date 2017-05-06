package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by User on 04/03/2017.
 */
public class Server {

    static ServerSocket serverSocket;
    static Socket socket;
    static ArrayList<ServerHelper> serverHelperArrayList;

    static JDialog serverRunning;

    public static boolean RUNNING = true;

    public static void main(String[] args){
        Integer port = 6066;
        System.out.println("Starting the server");
        serverHelperArrayList = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            RUNNING = false;
        }

        Thread thread = new Thread(() -> {
            JFrame frame = new JFrame();
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    RUNNING = false;
                    try {
                        serverSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            frame.setVisible(true);
            frame.setSize(new Dimension(400, 10));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setTitle("SERVER RUNNING");
        });
        thread.start();

        while (RUNNING) {
            try {
                socket = serverSocket.accept();
                ServerHelper sh = new ServerHelper(socket);
                serverHelperArrayList.add(sh);
                sh.start();
            } catch (IOException e) {
            }
        }

        for(ServerHelper sh : serverHelperArrayList){
            sh.shutDown();
        }

        System.out.println("Server successfully closed");
    }

    /**
     * This removes a helper from the list of helpers.
     * @param serverHelper the server helper to remove
     */
    public synchronized static void removeHelper(ServerHelper serverHelper) {
        synchronized (serverHelperArrayList){
            Iterator<ServerHelper> iterator = serverHelperArrayList.iterator();
            while (iterator.hasNext()){
                if(iterator.next().equals(serverHelper)){
                    iterator.remove();
                }
            }
        }
    }
}
