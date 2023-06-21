package lk.ijse.chatApplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author : Kavithma Thushal
 * @since : 6/19/2023
 **/
public class ClientController implements Initializable {

    @FXML
    private TextField txtClientSendField;
    @FXML
    private TextArea txtClientSendArea;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String msg = "";
    private String rly;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 2);

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (!msg.equals("finish")) {
                    msg = dataInputStream.readUTF();
                    txtClientSendArea.appendText("\nServer : " + msg);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    private void clientSendOnAction(ActionEvent actionEvent) {
        try {
            rly = txtClientSendField.getText().trim();
            dataOutputStream.writeUTF(rly);
            dataOutputStream.flush();
            txtClientSendField.clear();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
