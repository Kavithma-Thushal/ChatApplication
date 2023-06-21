package lk.ijse.chatApplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author : Kavithma Thushal
 * @since : 6/19/2023
 **/
public class ServerController implements Initializable {

    @FXML
    private TextField txtServerSendField;
    @FXML
    private TextArea txtServerSendArea;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String msg = "";
    private String rly;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(2);
                System.out.println("Server is started!");

                socket = serverSocket.accept();
                System.out.println("Client is accepted!");

                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                while (!msg.equals("finish")) {
                    msg = dataInputStream.readUTF();
                    txtServerSendArea.appendText("\nClient : " + msg);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    private void serverSendOnAction(ActionEvent actionEvent) {
        try {
            rly = txtServerSendField.getText().trim();
            dataOutputStream.writeUTF(rly);
            dataOutputStream.flush();
            txtServerSendField.clear();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
