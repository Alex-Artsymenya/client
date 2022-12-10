package com.example.bsuir.controller.admin;

import com.example.bsuir.model.enums.Requests;
import com.example.bsuir.model.enums.Roles;
import com.example.bsuir.model.ClientModel;
import com.example.bsuir.model.TCP.Request;
import com.example.bsuir.model.entities.Client;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageUsersController implements Initializable {
    public TableView<ClientModel> clientsTable;
    public TableColumn<ClientModel, String> id;
    public TableColumn<ClientModel, String> FIO;
    public TableColumn<ClientModel, String> email;
    public TableColumn<ClientModel, String> phone;
    public Button btnAdd;
    public Button btnEdit;
    public Button btnDelete;
    public Button btnBack;
    public Button btnLogOut;

    public void OnMouseClicked() {
        if (clientsTable.getSelectionModel().getSelectedItem() != null) {
            btnDelete.setDisable(false);
            btnEdit.setDisable(false);
        } else {
            btnDelete.setDisable(true);
            btnEdit.setDisable(true);
        }
    }

    public void OnBack() throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/account.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void OnAdd() throws IOException {
        Stage stage = (Stage) btnAdd.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/add_update_client.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void OnEdit() throws IOException {
        ClientModel clientModel = clientsTable.getSelectionModel().getSelectedItem();
        ClientSocket.getInstance().setClientId(clientModel.getId());
        Stage stage = (Stage) btnEdit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/add_update_client.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void OnDelete() throws IOException {
        Request requestModel = new Request();
        ClientModel clientModel = clientsTable.getSelectionModel().getSelectedItem();
        requestModel.setRequestMessage(new Gson().toJson(new Client(clientModel.getId())));
        requestModel.setRequestType(Requests.DELETE_CLIENT);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();
        clientsTable.getItems().remove(clientModel);
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        ClientSocket.getInstance().getInStream().readLine();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (ClientSocket.getInstance().getUser().getRole().equals(Roles.ADMIN.toString())) {
            btnDelete.setVisible(true); // отображение buttons;
            btnAdd.setVisible(true);
            btnEdit.setVisible(true);
        }
        FIO.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        GetService<Client> batchGetService = new GetService<>(Client.class);
        Type listType = new TypeToken<ArrayList<Client>>() {
        }.getType();
        List<Client> clients = new Gson().fromJson(batchGetService.GetEntities(Requests.GET_ALL_CLIENTS), listType);
        ObservableList<ClientModel> list = FXCollections.observableArrayList();
        if (clients.size() != 0) {
            for (Client client : clients) {
                ClientModel tableUser = new ClientModel(client.getId(), client.getName() + " " + client.getSurname(),
                                                        client.getEmail(), client.getTelephone());
                list.add(tableUser);
            }
        }

        clientsTable.setItems(list);
    }

    public void onLogOut() throws IOException {
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        ClientSocket.getInstance().setUser(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
