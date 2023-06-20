import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author : Kavithma Thushal
 * @since : 6/20/2023
 **/
public class ClientAppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent parent= FXMLLoader.load(getClass().getResource("lk/ijse/chatApplication/view/client.fxml"));
        Scene scene=new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
