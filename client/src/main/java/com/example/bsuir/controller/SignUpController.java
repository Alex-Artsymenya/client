package com.example.bsuir.controller;

import com.example.bsuir.model.enums.Requests;
import com.example.bsuir.model.enums.Responses;
import com.example.bsuir.model.enums.Roles;
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

public class SignUpController {
    public PasswordField passwordfieldPassword;
    public PasswordField passwordfieldConfirmPassword;
    public Button buttonSignUp;
    public Button buttonBack;
    public TextField textfieldLogin;
    public Label labelMessage;

    public void Signup_Pressed() throws IOException {
        labelMessage.setVisible(false);
        User user = new User();
        user.setLogin(textfieldLogin.getText());
        user.setPassword(passwordfieldPassword.getText());
        user.setRole(Roles.USER);
        if (!passwordfieldPassword.getText().equals(passwordfieldConfirmPassword.getText())) {
            labelMessage.setText("Пароли не совпадают");
            labelMessage.setVisible(true);
            return;
        }
        if (textfieldLogin.equals("") || passwordfieldPassword.equals("") || passwordfieldConfirmPassword.equals("")) {
            labelMessage.setText("Не все поля заполнены.");
            labelMessage.setVisible(true);
            return;
        }
        Request request = new Request();
        request.setRequestMessage(new Gson().toJson(user));
        request.setRequestType(Requests.SIGNUP);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();
        String answer = ClientSocket.getInstance().getInStream().readLine();
        Response response = new Gson().fromJson(answer, Response.class);
        if (response.getResponseStatus() == Responses.OK) {
            labelMessage.setVisible(false);
            ClientSocket.getInstance().setUser(new Gson().fromJson(response.getResponseData(), User.class));
            Stage stage = (Stage) buttonBack.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/fxml/user/account.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        } else {
            labelMessage.setText("Пользователь с таким логином уже существует.");
            labelMessage.setVisible(true);
        }
    }

    public void Back_Pressed() throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
