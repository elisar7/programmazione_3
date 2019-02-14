package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerApp extends Application {

    FXMLLoader server_view = new FXMLLoader();

    public static void main(String[] args) {
        Application.launch(ServerApp.class, args);
    }

    public void start(Stage stage) throws Exception {
        server_view = new FXMLLoader(getClass().
                getResource("ServerView.fxml"));
        Parent root = server_view.load();
        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }

}
