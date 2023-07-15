package lk.ijse.chatApplication.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @since : 6/21/2023
 **/
public class ClientHandler extends Thread {
    private Socket socket;
    private ArrayList<ClientHandler> clientHandlerArrayList;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clientHandlerArrayList) {
        try {
            this.socket = socket;
            this.clientHandlerArrayList = clientHandlerArrayList;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = bufferedReader.readLine()) != null) {
                if (msg.equalsIgnoreCase("bye")) {
                    break;
                }
                for (ClientHandler clientHandler : clientHandlerArrayList) {
                    clientHandler.printWriter.println(msg);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bufferedReader.close();
                printWriter.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
