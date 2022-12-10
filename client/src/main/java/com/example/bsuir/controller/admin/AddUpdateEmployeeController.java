package com.example.bsuir.controller.admin;

import com.example.bsuir.model.enums.Requests;
import com.example.bsuir.model.TCP.Request;
import com.example.bsuir.model.TCP.Response;
import com.example.bsuir.model.entities.Employee;
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

public class AddUpdateEmployeeController implements Initializable {
    public TextField nameField;
    public TextField surnameField;
    public Button btnBack;
    public Button btnSave;
    public Button btnLogOut;
    Employee employee;
    private int employeeId;

    public void onLogOut() throws IOException {
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        ClientSocket.getInstance().setUser(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void onBack() throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/manage_employee.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void onSave() {
        try {
            if (employee == null) {
                employee = new Employee();
            }
            employee.setName(nameField.getText());
            employee.setSurname(surnameField.getText());
            Request request;
            if (ClientSocket.getInstance().getEmployeeId() != -1) {
                employee.setId(employeeId);
                request = new Request(Requests.UPDATE_EMPLOYEE, new Gson().toJson(employee));
            } else {
                request = new Request(Requests.ADD_EMPLOYEE, new Gson().toJson(employee));
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
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/manage_employee.fxml"));
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
            if (ClientSocket.getInstance().getEmployeeId() != -1) {
                GetService<Employee> flightGetService = new GetService<>(Employee.class);
                employee = flightGetService.GetEntity(Requests.GET_EMPLOYEE,
                                                      new Employee(ClientSocket.getInstance().getEmployeeId()));
                employeeId = employee.getId();
                nameField.setText(employee.getName());
                surnameField.setText(employee.getSurname());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
