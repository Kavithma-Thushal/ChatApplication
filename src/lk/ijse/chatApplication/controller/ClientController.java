package lk.ijse.chatApplication.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private TextField txtMsgSendField;
    @FXML
    private AnchorPane emojiPane;
    @FXML
    private AnchorPane gifPane;

    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private FileChooser fileChooser;
    private File filePath;

    public void initialize() {
        lblName.setText(LoginController.userName);

        try {
            socket = new Socket("localhost", 8080);
            System.out.println("Server accepted " + LoginController.userName + "!");
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        emojiPane.setVisible(false);
        gifPane.setVisible(false);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = bufferedReader.readLine();
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

                //Handling Images
                if (firstChars.equalsIgnoreCase("img")) {

                    st = st.substring(3, st.length() - 1);

                    File file = new File(st);
                    Image image = new Image(file.toURI().toString());

                    ImageView imageView = new ImageView(image);

                    double scaleFactor = 0.07;
                    double newWidth = image.getWidth() * scaleFactor;
                    double newHeight = image.getHeight() * scaleFactor;

                    imageView.setFitWidth(newWidth);
                    imageView.setFitHeight(newHeight);

                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);

                    if (!cmd.equalsIgnoreCase(lblName.getText())) {

                        vBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        Label label1 = new Label("  " + cmd + " :-");
                        label1.setStyle("-fx-text-fill: white; -fx-background-color: rgb(43,82,71); -fx-background-radius: 5px;");


                        hBox.getChildren().add(label1);
                        hBox.getChildren().add(imageView);

                    } else {

                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(imageView);
                        Text text1 = new Text("");
                        hBox.getChildren().add(text1);
                    }

                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                    scrollPane.setVvalue(1.0);

                } else {    //Main Else

                    TextFlow tempFlow = new TextFlow();

                    if (!cmd.equalsIgnoreCase(lblName.getText() + ":")) {
                        Text txtName = new Text(cmd + "  ");
                        txtName.setFill(Color.WHITE);
                        txtName.getStyleClass().add("txtName");
                        tempFlow.getChildren().add(txtName);

                        tempFlow.setStyle("-fx-background-color: rgb(43,82,71);" + " -fx-background-radius: 5px");
                        tempFlow.setPadding(new Insets(3, 10, 3, 10));
                    }

                    tempFlow.getChildren().add(text);
                    tempFlow.setMaxWidth(200);

                    TextFlow flow = new TextFlow(tempFlow);
                    HBox hBox = new HBox(12);

                    if (!cmd.equalsIgnoreCase(lblName.getText() + ":")) {
                        vBox.setAlignment(Pos.TOP_LEFT);            //Handling Other Users
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(flow);
                        hBox.setPadding(new Insets(2, 5, 2, 5));

                    } else {
                        Text text2 = new Text(fullMsg + " ");       //Handling Current User
                        text2.setFill(Color.WHITE);
                        TextFlow flow2 = new TextFlow(text2);

                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(flow2);
                        hBox.setPadding(new Insets(2, 5, 2, 5));

                        flow2.setStyle("-fx-background-color: rgb(25,133,102);" + "-fx-background-radius: 5px");
                        flow2.setPadding(new Insets(3, 10, 3, 10));
                    }

                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                    scrollPane.setVvalue(1.0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void txtMsgSendOnAction(ActionEvent actionEvent) {
        String msg = txtMsgSendField.getText();
        printWriter.println(lblName.getText() + ": " + msg);
        txtMsgSendField.clear();

        if (msg.equalsIgnoreCase("bye")) {
            System.exit(0);
        }
    }

    @FXML
    private void btnMsgSendOnAction(MouseEvent event) {
        String msg = txtMsgSendField.getText();
        printWriter.println(lblName.getText() + ": " + msg);
        txtMsgSendField.clear();

        if (msg.equalsIgnoreCase("bye")) {
            System.exit(0);
        }
    }

    @FXML
    private void attachmentOnAction(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        this.filePath = fileChooser.showOpenDialog(stage);
        printWriter.println(lblName.getText() + " " + "img" + filePath.getPath());
    }

    @FXML
    private void voiceCallOnAction(MouseEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "Calling .....   ").show();
    }

    @FXML
    private void videoCallOnAction(MouseEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "Calling .....   ").show();
    }

    @FXML
    private void emojiOnAction(MouseEvent event) {
        emojiPane.setVisible(true);
    }

    @FXML
    private void gifOnAction(MouseEvent event) {
        gifPane.setVisible(true);
    }

    @FXML
    private void hideEmojiOnAction(MouseEvent event) {
        emojiPane.setVisible(false);
        gifPane.setVisible(false);
    }

    private void unicodeEmoji(int unicode) {
        String emoji = new String(Character.toChars(unicode));
        txtMsgSendField.setText(txtMsgSendField.getText() + emoji);
        txtMsgSendField.positionCaret(txtMsgSendField.getText().length());
        emojiPane.setVisible(false);
    }

    @FXML
    private void love(MouseEvent event) {
        unicodeEmoji(128525);
    }

    @FXML
    private void small_smile(MouseEvent event) {
        unicodeEmoji(128578);
    }

    @FXML
    private void normal_smile(MouseEvent event) {
        unicodeEmoji(128522);
    }

    @FXML
    private void large_smile(MouseEvent event) {
        unicodeEmoji(128513);
    }

    @FXML
    private void real_smile(MouseEvent event) {
        unicodeEmoji(128514);
    }

    @FXML
    private void eyeclose_smile(MouseEvent event) {
        unicodeEmoji(128540);
    }

    @FXML
    private void tong_smile(MouseEvent event) {
        unicodeEmoji(128539);
    }

    @FXML
    private void sad(MouseEvent event) {
        unicodeEmoji(128546);
    }

    @FXML
    private void lot_sad(MouseEvent event) {
        unicodeEmoji(128554);
    }

    @FXML
    private void green_sad(MouseEvent event) {
        unicodeEmoji(128560);
    }

    @FXML
    private void cry(MouseEvent event) {
        unicodeEmoji(128546);
    }

    @FXML
    private void cry_sad(MouseEvent event) {
        unicodeEmoji(128550);
    }

    @FXML
    private void woow(MouseEvent event) {
        unicodeEmoji(128559);
    }

    @FXML
    private void tuin(MouseEvent event) {
        unicodeEmoji(128519);
    }

    @FXML
    private void money(MouseEvent event) {
        unicodeEmoji(129297);
    }

    private void gifEmoji(String path) {
        this.filePath = new File(path);
        printWriter.println(lblName.getText() + " " + "img" + filePath.getPath());
        gifPane.setVisible(false);
    }

    @FXML
    private void loveGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\love.png");
    }

    @FXML
    private void small_smileGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\small_smile.png");
    }

    @FXML
    private void normal_smileGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\normal_smile.png");
    }

    @FXML
    private void large_smileGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\large_smile.png");
    }

    @FXML
    private void real_smileGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\real_smile.png");
    }

    @FXML
    private void eyeclose_smileGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\eyeclose_smile.png");
    }

    @FXML
    private void tong_smileGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\tong_smile.png");
    }

    @FXML
    private void sadGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\sad.png");
    }

    @FXML
    private void lot_sadGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\lot_sad.png");
    }

    @FXML
    private void green_sadGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\green_sad.png");
    }

    @FXML
    private void cryGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\cry.png");
    }

    @FXML
    private void cry_sadGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\cry_sad.png");
    }

    @FXML
    private void woowGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\woow.png");
    }

    @FXML
    private void tuinGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\tuin.png");
    }

    @FXML
    private void moneyGif(MouseEvent event) {
        gifEmoji("D:\\IJSE\\Workspace\\2nd Sem Repo\\ChatApplication\\src\\lk\\ijse\\chatApplication\\view\\images\\gif\\money.png");
    }
}
