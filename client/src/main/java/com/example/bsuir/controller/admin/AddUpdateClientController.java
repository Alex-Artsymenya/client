package com.example.bsuir.controller.admin;

import com.example.bsuir.model.enums.Requests;
import com.example.bsuir.model.TCP.Request;
import com.example.bsuir.model.TCP.Response;
import com.example.bsuir.model.entities.Client;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddUpdateClientController implements Initializable {
    public TextField nameField;
    public TextField surnameField;
    public TextField phoneField;
    public TextField emailField;
    public Button btnBack;
    public Button btnSave;
    public Button btnLogOut;
    Client client;
    private int clientId;
    private int userId;

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

    public void onSave() {
        try {
            User user = new User();
            if (client == null) {
                client = new Client();
            }
            user.setId(ClientSocket.getInstance().getUser().getId());
            client.setName(nameField.getText());
            client.setSurname(surnameField.getText());
            client.setTelephone(phoneField.getText());
            client.setEmail(emailField.getText());
            client.setUser(user);
            Request request;
            if (ClientSocket.getInstance().getClientId() != -1) {
                client.setId(clientId);
                request = new Request(Requests.UPDATE_CLIENT, new Gson().toJson(client));
            } else {
                request = new Request(Requests.ADD_CLIENT, new Gson().toJson(client));
            }
            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();
            Response response = new Gson().fromJson(ClientSocket.getInstance().getInStream().readLine(),
                                                    Response.class);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(response.getResponseMessage());
            alert.showAndWait();
            ClientSocket.getInstance().setClientId(-1);
            Thread.sleep(1500);
            Stage stage = (Stage) btnSave.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/user/manage_account.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if (ClientSocket.getInstance().getClientId() != -1) {
                GetService<Client> flightGetService = new GetService<>(Client.class);
                client = flightGetService.GetEntity(Requests.GET_CLIENT,
                                                    new Client(ClientSocket.getInstance().getClientId()));
                clientId = client.getId();
                userId = client.getUser().getId();
                nameField.setText(client.getName());
                surnameField.setText(client.getSurname());
                phoneField.setText(client.getTelephone());
                emailField.setText(client.getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
