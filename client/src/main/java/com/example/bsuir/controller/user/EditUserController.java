package com.example.bsuir.controller.user;

import com.example.bsuir.model.enums.Requests;
import com.example.bsuir.model.TCP.Request;
import com.example.bsuir.model.TCP.Response;
import com.example.bsuir.model.entities.User;
import com.example.bsuir.util.ClientSocket;
import com.example.bsuir.util.GetService;
import com.google.gson.Gson;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditUserController implements Initializable {
    private final String blackbutton = "-fx-text-fill: black;\n" +
                                       "    -fx-font-family: \"Arial Narrow\";\n" +
                                       "    -fx-font-weight: bold;\n" +
                                       "   -fx-border-color:black;\n" +
                                       "   -fx-background-color:null;";
    private final String blackfont = "-fx-text-fill:black;";

    public TextField loginField;
    public TextField passwordField;
    public Button btnLogOut;
    public Button btnBack;
    public Button btnSave;
    public AnchorPane anchorPane;
    public Label text1;
    public Label text2;
    User user;
    int userId;

    public void onLogOut() throws IOException {
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        ClientSocket.getInstance().setUser(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void onBack() throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/user/manage_account.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void onSave() throws IOException {
        try {
            if (passwordField == null || loginField == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Заполните все поля.");
                alert.showAndWait();
            } else {
                user.setLogin(loginField.getText());
                user.setPassword(passwordField.getText());
                Request request;
                user.setId(userId);
                request = new Request(Requests.UPDATE_USER, new Gson().toJson(user));
                ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
                ClientSocket.getInstance().getOut().flush();
                Response response = new Gson().fromJson(ClientSocket.getInstance().getInStream().readLine(),
                                                        Response.class);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(response.getResponseMessage());
                alert.showAndWait();
                ClientSocket.getInstance().setEmployeeId(-1);
                Thread.sleep(1500);
                Stage stage = (Stage) btnSave.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/user/manage_account.fxml"));
                Scene newScene = new Scene(root);
                stage.setScene(newScene);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (UserAccountController.theme == 1) {
            anchorPane.setStyle("-fx-background-image: url(images/third.jpg)");
            btnLogOut.setStyle(blackbutton);
            btnBack.setStyle(blackbutton);
            btnSave.setStyle(blackbutton);
            text1.setStyle(blackfont);
            text2.setStyle(blackfont);
        }
        try {
            if (ClientSocket.getInstance().getUser().getId() != -1) {
                GetService<User> flightGetService = new GetService<>(User.class);
                user = flightGetService.GetEntity(Requests.GET_USER,
                                                  new User(ClientSocket.getInstance().getUser().getId()));
                userId = user.getId();
                loginField.setText(user.getLogin());
                passwordField.setText(user.getPassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

