package com.example.bsuir.controller.admin;

import com.example.bsuir.model.TCP.Request;
import com.example.bsuir.model.TCP.Response;
import com.example.bsuir.model.entities.Service;
import com.example.bsuir.model.enums.Requests;
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

public class AddUpdateServiceController implements Initializable {
    public TextField nameField;
    public TextField priceField;
    public TextField countryField;
    public Button btnBack;
    public Button btnSave;
    public Button btnLogOut;
    Service service;
    private int serviceId;

    public void onLogOut() throws IOException {
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        ClientSocket.getInstance().setUser(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void onBack() throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/manage_services.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void onSave() {
        try {
            Float.parseFloat(priceField.getText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Введите корректную стоимость");
            alert.showAndWait();
            return;
        }
        try {
            if (service == null) {
                service = new Service();
            }
            service.setName(nameField.getText());
            service.setCountry(countryField.getText());
            service.setPrice(Float.parseFloat(priceField.getText()));
            Request request;
            if (ClientSocket.getInstance().getServiceId() != -1) {
                service.setId(serviceId);
                request = new Request(Requests.UPDATE_SERVICE, new Gson().toJson(service));
            } else {
                request = new Request(Requests.ADD_SERVICE, new Gson().toJson(service));
            }
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
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/manage_services.fxml"));
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
            if (ClientSocket.getInstance().getServiceId() != -1) {
                GetService<Service> flightGetService = new GetService<>(Service.class);
                service = flightGetService.GetEntity(Requests.GET_SERVICE,
                                                     new Service(ClientSocket.getInstance().getServiceId()));
                serviceId = service.getId();
                nameField.setText(service.getName());
                countryField.setText(String.valueOf(service.getCountry()));
                priceField.setText(String.valueOf(service.getPrice()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
