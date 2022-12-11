package com.example.bsuir;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.bsuir.util.ClientSocket;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientSocket.getInstance().getSocket();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        primaryStage.setTitle("coursework");
        primaryStage.setScene(new Scene(root, 700, 470));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

