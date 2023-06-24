package lk.ijse.chatApplication.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private VBox vBox;
    @FXML
    private TextField txtMsgSendField;
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
        try {
            while (true) {
                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];

                StringBuilder fullMsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMsg.append(tokens[i] + " ");
                }

                String[] msgToAr = msg.split(" ");
                String st = "";
                for (int i = 0; i < msgToAr.length - 1; i++) {
                    st += msgToAr[i + 1] + " ";
                }

                Text text = new Text(st);
                text.setFill(Color.WHITE);
                String firstChars = "";
                if (st.length() > 3) {
                    firstChars = st.substring(0, 3);
                }

                if (firstChars.equalsIgnoreCase("img")) {
                    //for the Images

                    st = st.substring(3, st.length() - 1);

                    File file = new File(st);
                    Image image = new Image(file.toURI().toString());

                    ImageView imageView = new ImageView(image);

                    imageView.setFitHeight(150);
                    imageView.setFitWidth(200);

                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);

                    if (!cmd.equalsIgnoreCase(lblName.getText())) {

                        vBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        Text text1 = new Text("  " + cmd + " :");
                        hBox.getChildren().add(text1);
                        hBox.getChildren().add(imageView);

                    } else {

                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(imageView);
                        Text text1 = new Text("");
                        hBox.getChildren().add(text1);
                    }

                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));

                } else {

                    TextFlow tempFlow = new TextFlow();

                    if (!cmd.equalsIgnoreCase(lblName.getText() + ":")) {
                        Text txtName = new Text(cmd + " ");
                        txtName.setFill(Color.WHITE);
                        txtName.getStyleClass().add("txtName");
                        tempFlow.getChildren().add(txtName);

                        tempFlow.setStyle("-fx-color: rgb(239,242,255);" + "-fx-background-color: rgb(43,82,71);" + " -fx-background-radius: 5px");
                        tempFlow.setPadding(new Insets(3, 10, 3, 10));
                    }

                    tempFlow.getChildren().add(text);
                    tempFlow.setMaxWidth(200); //200

                    TextFlow flow = new TextFlow(tempFlow);

                    HBox hBox = new HBox(12); //12

                    if (!cmd.equalsIgnoreCase(lblName.getText() + ":")) {

                        vBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(flow);

                    } else {

                        Text text2 = new Text(fullMsg + " ");
                        text2.setFill(Color.WHITE);
                        TextFlow flow2 = new TextFlow(text2);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(flow2);
                        hBox.setPadding(new Insets(2, 5, 2, 10));

                        flow2.setStyle("-fx-color: rgb(239,242,255);" + "-fx-background-color: rgb(25,133,102);" + "-fx-background-radius: 5px");
                        flow2.setPadding(new Insets(3, 10, 3, 10));
                    }

                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void txtMsgSendOnAction(ActionEvent actionEvent) {
        String msg = txtMsgSendField.getText();
        writer.println(lblName.getText() + ": " + msg);

        txtMsgSendField.clear();

        if (msg.equalsIgnoreCase("bye")) {
            System.exit(0);
        }
    }

    @FXML
    private void btnMsgSendOnAction(MouseEvent event) {
        String msg = txtMsgSendField.getText();
        writer.println(lblName.getText() + ": " + msg);

        txtMsgSendField.clear();

        if (msg.equalsIgnoreCase("bye")) {
            System.exit(0);
        }
    }

    @FXML
    private void camOnAction(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        writer.println(lblName.getText() + " " + "img" + filePath.getPath());
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
    private void sad(MouseEvent event) {
        String emoji = new String(Character.toChars(128546));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void lot_sad(MouseEvent event) {
        String emoji = new String(Character.toChars(128554));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void money(MouseEvent event) {
        String emoji = new String(Character.toChars(129297));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void love(MouseEvent event) {
        String emoji = new String(Character.toChars(128525));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void green_sad(MouseEvent event) {
        String emoji = new String(Character.toChars(128560));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void smile_eye_close(MouseEvent event) {
        String emoji = new String(Character.toChars(128540));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void cry(MouseEvent event) {
        String emoji = new String(Character.toChars(128546));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void sad_head(MouseEvent event) {
        String emoji = new String(Character.toChars(128550));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void real_smile(MouseEvent event) {
        String emoji = new String(Character.toChars(128514));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void tuin(MouseEvent event) {
        String emoji = new String(Character.toChars(128519));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void woow(MouseEvent event) {
        String emoji = new String(Character.toChars(128559));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void smile_normal(MouseEvent event) {
        String emoji = new String(Character.toChars(128513));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void large_smile(MouseEvent event) {
        String emoji = new String(Character.toChars(128522));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void small_smile(MouseEvent event) {
        String emoji = new String(Character.toChars(128578));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void tong_smile(MouseEvent event) {
        String emoji = new String(Character.toChars(128539));
        txtMsgSendField.setText(emoji);
        emojiPane.setVisible(false);
    }

    @FXML
    private void voiceCallOnAction(MouseEvent event) {

    }

    @FXML
    private void videoCallOnAction(MouseEvent event) {

    }
}
