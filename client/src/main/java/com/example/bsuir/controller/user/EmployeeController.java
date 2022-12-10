package com.example.bsuir.controller.user;

import com.example.bsuir.model.enums.Requests;
import com.example.bsuir.model.EmployeeModel;
import com.example.bsuir.util.ClientSocket;
import com.example.bsuir.util.GetService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    private final String blackbutton = "-fx-text-fill: #000000;\n" +
                                       "    -fx-font-family: \"Arial Narrow\";\n" +
                                       "    -fx-font-weight: bold;\n" +
                                       "   -fx-border-color:black;\n" +
                                       "   -fx-background-color:null;";

    public TableView<EmployeeModel> employeeTable;
    public TableColumn<EmployeeModel, String> name;
    public TableColumn<EmployeeModel, String> surname;
    public TableColumn<EmployeeModel, String> services;
    public AnchorPane employee;
    public Button btnLogOut;
    public Button btnBack;

    public void onLogOut() throws IOException {
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        ClientSocket.getInstance().setUser(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void OnBack() throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/user/account.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (UserAccountController.theme == 1) {
            employee.setStyle("-fx-background-image: url(images/third.jpg)");
            btnLogOut.setStyle(blackbutton);
            btnBack.setStyle(blackbutton);
        }
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        services.setCellValueFactory(new PropertyValueFactory<>("services"));
        GetService<com.example.bsuir.model.entities.Employee> batchGetService = new GetService<>(
                com.example.bsuir.model.entities.Employee.class);
        Type listType = new TypeToken<ArrayList<com.example.bsuir.model.entities.Employee>>() {
        }.getType();
        List<com.example.bsuir.model.entities.Employee> employees = new Gson().fromJson(
                batchGetService.GetEntities(Requests.GET_ALL_EMPLOYEES), listType);
        ObservableList<EmployeeModel> list = FXCollections.observableArrayList();
        if (employees.size() != 0) {
            for (com.example.bsuir.model.entities.Employee employee : employees) {
                EmployeeModel tableEmployee = new EmployeeModel(employee.getId(), employee.getName(),
                                                                employee.getSurname(),
                                                                employee.getServices().toString());
                list.add(tableEmployee);
            }
        }

        employeeTable.setItems(list);
    }
}
