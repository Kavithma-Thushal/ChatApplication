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
public class Handler extends Thread {
    private ArrayList<Handler> clients;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Handler(Socket socket, ArrayList<Handler> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                if (msg.equalsIgnoreCase("exit")) {
                    break;
                }
                for (Handler cl : clients) {
                    cl.writer.println(msg);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
