package lk.ijse.chatApplication.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @since : 6/21/2023
 **/
public class Server {
    private static ArrayList<Handler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket=new ServerSocket(1)){
            Socket socket;

            while (true){
                System.out.println("Waiting for client ...");
                socket = serverSocket.accept();
                System.out.println("Client is connected");

                Handler handler = new Handler(socket, clients);
                clients.add(handler);
                handler.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
