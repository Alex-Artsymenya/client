package com.example.bsuir.controller;

import com.example.bsuir.model.enums.Requests;
import com.example.bsuir.model.enums.Responses;
import com.example.bsuir.model.TCP.Request;
import com.example.bsuir.model.TCP.Response;
import com.example.bsuir.model.entities.User;
import com.example.bsuir.util.ClientSocket;
import com.google.gson.Gson;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public PasswordField passwordfieldPassword;
    public TextField textfieldLogin;
    public Button buttonRegister;
    public Button buttonLogin;
    public Label labelMessage;

    public void Login_Pressed() throws IOException {
        User user = new User();
        user.setLogin(textfieldLogin.getText());
        user.setPassword(passwordfieldPassword.getText());
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(user));
        requestModel.setRequestType(Requests.LOGIN);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();
        String answer = ClientSocket.getInstance().getInStream().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == Responses.OK) {
            labelMessage.setVisible(false);
            ClientSocket.getInstance().setUser(new Gson().fromJson(responseModel.getResponseData(), User.class));
            Stage stage = (Stage) buttonLogin.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/fxml/user/account.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        }
        if (responseModel.getResponseStatus() == Responses.ADMIN_OK) {
            labelMessage.setVisible(false);
            ClientSocket.getInstance().setUser(new Gson().fromJson(responseModel.getResponseData(), User.class));
            Stage stage = (Stage) buttonLogin.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/fxml/admin/account.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);

        } else {
            labelMessage.setVisible(true);
        }
    }

    public void Register_Pressed() throws IOException {
        Stage stage = (Stage) buttonRegister.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sign_up.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);

    }
}
