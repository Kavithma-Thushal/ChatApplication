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
    private static ArrayList<ClientHandler> clientHandlerArrayList = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            Socket socket;

            while (true) {
                System.out.println("Server is listening for clients .....");
                socket = serverSocket.accept();
                System.out.println("Client is connected!");

                ClientHandler clientHandler = new ClientHandler(socket, clientHandlerArrayList);
                clientHandlerArrayList.add(clientHandler);
                clientHandler.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
