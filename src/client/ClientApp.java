package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {
    FXMLLoader loader;
    ClientLoginController client_controller;

    public static void main(String[] args) {
        Application.launch(ClientApp.class, args);
    }

    public void start(Stage stage) throws Exception {
        loader = new FXMLLoader(getClass().
                getResource("LoginView.fxml"));
        Parent root = loader.load();
        client_controller = loader.getController();
        stage.setScene(new Scene(root, 600, 400));
        stage.setOnCloseRequest(e -> client_controller.shutdown());
        stage.setTitle("Login");
        stage.show();
    }
}
