package lk.ijse.chatApplication.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author : Kavithma Thushal
 * @since : 6/21/2023
 **/
public class LoginController {

    @FXML
    private JFXTextField txtEnterName;
    String userName;

    @FXML
    private void txtEnterNameOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/chatApplication/view/client.fxml"))));
        stage.setResizable(false);
        stage.setTitle("Client");
        stage.show();
        userName = txtEnterName.getText();
        txtEnterName.clear();
    }
}
