package com.example.bsuir.controller.admin;

import com.example.bsuir.util.ClientSocket;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminAccountController {
    public Button manageUsersButton;
    public Button manageEmployeesButton;
    public Button usersButton;
    public Button manageServicesButton;
    public Button manageAppointmentsButton;
    public Button loggoutButton;
    public Button addEmployeeServiceButton;

    public void manageUsersButtonPressed() throws IOException {
        Stage stage = (Stage) manageUsersButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/manage_users.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void usersButtonPressed() throws IOException {
        Stage stage = (Stage) usersButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/users.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void manageAppointmentsButtonPressed() throws IOException {
        Stage stage = (Stage) manageAppointmentsButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/manage_appointments.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void manageEmployeesButtonPressed() throws IOException {
        Stage stage = (Stage) manageEmployeesButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/manage_employee.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void manageServicesButtonPressed() throws IOException {
        Stage stage = (Stage) manageServicesButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/manage_services.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void addEmployeeServiceButtonPressed() throws IOException {
        Stage stage = (Stage) addEmployeeServiceButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/add_employee_service.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void logoutButtonPressed() throws IOException {
        Stage stage = (Stage) loggoutButton.getScene().getWindow();
        ClientSocket.getInstance().setUser(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
