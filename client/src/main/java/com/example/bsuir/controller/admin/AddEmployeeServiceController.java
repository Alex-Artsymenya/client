package com.example.bsuir.controller.admin;

import com.example.bsuir.model.enums.Requests;
import com.example.bsuir.model.EmployeeModel;
import com.example.bsuir.model.ServiceModel;
import com.example.bsuir.model.TCP.Request;
import com.example.bsuir.model.TCP.Response;
import com.example.bsuir.model.entities.Employee;
import com.example.bsuir.model.entities.EmployeeService;
import com.example.bsuir.model.entities.Service;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddEmployeeServiceController implements Initializable {
    public TableView<EmployeeModel> employeeTable;
    public TableColumn<EmployeeModel, String> emplName;
    public TableColumn<EmployeeModel, String> surname;

    public TableView<ServiceModel> serviceTable;
    public TableColumn<ServiceModel, String> name;
    public TableColumn<ServiceModel, String> country;
    public TableColumn<ServiceModel, String> price;

    public Button btnLogOut;
    public Button btnBack;
    public Button btnSave;

    Employee employee = new Employee();
    Service service = new Service();

    public void OnMouseClickedEmployee() {
        EmployeeModel employeeModel = employeeTable.getSelectionModel().getSelectedItem();
        employee.setId(employeeModel.getId());
    }

    public void OnMouseClickedServices() {
        int id = serviceTable.getSelectionModel().getSelectedItem().getId();
        service.setId(id);
    }

    public void onSave() throws IOException, InterruptedException {
        EmployeeService employeeService = new EmployeeService();
        employeeService.setEmployee(employee);
        employeeService.setService(service);
        Request request;
        request = new Request(Requests.ADD_EMPLSERV, new Gson().toJson(employeeService));
        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();
        Response response = new Gson().fromJson(ClientSocket.getInstance().getInStream().readLine(), Response.class);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(response.getResponseMessage());
        alert.showAndWait();
        Thread.sleep(1500);
        Stage stage = (Stage) btnSave.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/account.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emplName.setCellValueFactory(new PropertyValueFactory<>("name"));
        GetService<Employee> batchGetService = new GetService<>(Employee.class);
        Type listType = new TypeToken<ArrayList<Employee>>() {
        }.getType();
        List<Employee> employees = new Gson().fromJson(batchGetService.GetEntities(Requests.GET_ALL_EMPLOYEES),
                                                       listType);
        ObservableList<EmployeeModel> list = FXCollections.observableArrayList();
        if (employees.size() != 0) {
            for (Employee employee : employees) {
                EmployeeModel tableEmployee = new EmployeeModel(employee.getId(), employee.getName(),
                                                                employee.getSurname(),
                                                                employee.getServices().toString());
                list.add(tableEmployee);
            }
        }

        employeeTable.setItems(list);

        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        GetService<Service> batchGetService2 = new GetService<>(Service.class);
        Type listType2 = new TypeToken<ArrayList<Service>>() {
        }.getType();
        List<Service> services = new Gson().fromJson(batchGetService2.GetEntities(Requests.GET_ALL_SERVICES),
                                                     listType2);
        ObservableList<ServiceModel> list2 = FXCollections.observableArrayList();
        if (services.size() != 0) {
            for (Service service : services) {
                ServiceModel tableService = new ServiceModel(service.getId(), service.getName(),
                                                             service.getCountry(), service.getPrice());
                list2.add(tableService);
            }
        }

        serviceTable.setItems(list2);
    }

    public void onBack() throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/account.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void onLogOut() throws IOException {
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        ClientSocket.getInstance().setUser(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
