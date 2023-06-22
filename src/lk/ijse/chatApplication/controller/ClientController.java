package lk.ijse.chatApplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;

/**
 * @author : Kavithma Thushal
 * @since : 6/19/2023
 **/
public class ClientController extends Thread {

    @FXML
    private Label lblName;
    @FXML
    private ScrollPane msgShowArea;
    @FXML
    private VBox vBox;
    @FXML
    private TextField msgSendField;
    @FXML
    private AnchorPane emojiPane;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private FileChooser fileChooser;
    private File filePath;

    public void initialize() {
        lblName.setText(LoginController.userName);

        try {
            socket = new Socket("localhost", 1);
            System.out.println("Socket is connected with the server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        emojiPane.setVisible(false);
    }

    @Override
    public void run() {

    }

    @FXML
    private void btnMsgSendOnAction(MouseEvent event) {

    }

    @FXML
    private void msgSendOnAction(ActionEvent actionEvent) {

    }

    @FXML
    private void camOnAction(MouseEvent event) {

    }

    @FXML
    private void emojiPaneOnAction(MouseEvent event) {

    }

    @FXML
    private void emojiOnAction(MouseEvent event) {
        emojiPane.setVisible(true);
    }

    @FXML
    private void hideEmojiOnAction(MouseEvent event) {
        emojiPane.setVisible(false);
    }

    @FXML
    private void logoutOnAction(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void sad(MouseEvent event) {

    }

    @FXML
    private void lot_sad(MouseEvent event) {

    }

    @FXML
    private void money(MouseEvent event) {

    }

    @FXML
    private void love(MouseEvent event) {

    }

    @FXML
    private void green_sad(MouseEvent event) {

    }

    @FXML
    private void smile_eye_close(MouseEvent event) {

    }

    @FXML
    private void cry(MouseEvent event) {

    }

    @FXML
    private void sad_head(MouseEvent event) {

    }

    @FXML
    private void real_smile(MouseEvent event) {

    }

    @FXML
    private void tuin(MouseEvent event) {

    }

    @FXML
    private void woow(MouseEvent event) {

    }

    @FXML
    private void smile_normal(MouseEvent event) {

    }

    @FXML
    private void large_smile(MouseEvent event) {

    }

    @FXML
    private void small_smile(MouseEvent event) {

    }

    @FXML
    private void tong_smile(MouseEvent event) {

    }
}
