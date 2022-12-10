package com.example.bsuir.controller.user;

import com.example.bsuir.model.enums.Requests;
import com.example.bsuir.model.ServiceModel;
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

public class ServicesController implements Initializable {
    private final String blackbutton = "-fx-text-fill: black;\n" +
                                       "    -fx-font-family: \"Arial Narrow\";\n" +
                                       "    -fx-font-weight: bold;\n" +
                                       "   -fx-border-color:black;\n" +
                                       "   -fx-background-color:null;";

    public TableView<ServiceModel> serviceTable;
    public TableColumn<ServiceModel, String> name;
    public TableColumn<ServiceModel, String> country;
    public TableColumn<ServiceModel, String> price;
    public Button btnLogOut;
    public Button btnBack;
    public AnchorPane anchorPane;

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
            anchorPane.setStyle("-fx-background-image: url(images/lighttipa.jpg)");
            btnLogOut.setStyle(blackbutton);
            btnBack.setStyle(blackbutton);
        }
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        GetService<Service> batchGetService = new GetService<>(Service.class);
        Type listType = new TypeToken<ArrayList<Service>>() {
        }.getType();
        List<Service> services = new Gson().fromJson(batchGetService.GetEntities(Requests.GET_ALL_SERVICES), listType);
        ObservableList<ServiceModel> list = FXCollections.observableArrayList();
        if (services.size() != 0) {
            for (Service service : services) {
                ServiceModel tableService = new ServiceModel(service.getId(), service.getName(),
                                                             service.getCountry(), service.getPrice());
                list.add(tableService);
            }
        }

        serviceTable.setItems(list);
    }
}
