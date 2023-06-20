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
    private String msg;
    private String rly = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(3001);
                txtServerSendArea.appendText("Server is started!");

                socket = serverSocket.accept();
                txtServerSendArea.appendText("\nClient is accepted!");

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (!rly.equals("finish")) {
                    rly = dataInputStream.readUTF();
                    txtServerSendArea.appendText("\nClient : " + rly);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    private void serverSendOnAction(ActionEvent actionEvent) {
        try {
            msg = txtServerSendField.getText().trim();
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
            txtServerSendField.clear();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
