package com.example.bsuir.controller.admin;

import com.example.bsuir.model.enums.Requests;
import com.example.bsuir.model.enums.Roles;
import com.example.bsuir.model.AppointmentModel;
import com.example.bsuir.model.TCP.Request;
import com.example.bsuir.model.entities.Appointment;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class ManageAppointmentsController implements Initializable {
    public TableView<AppointmentModel> appointmentTable;
    public TableColumn<AppointmentModel, String> id;
    public TableColumn<AppointmentModel, String> start;
    public TableColumn<AppointmentModel, String> end;
    public TableColumn<AppointmentModel, String> date;
    public TableColumn<AppointmentModel, String> service;
    public TableColumn<AppointmentModel, String> client;
    public Button btnStatistic;
    public Button btnEdit;
    public Button btnDelete;
    public Button btnBack;
    public Button btnLogOut;
    public Button btnReset;
    public Button btnFilter;
    public Button btnCheck;
    public TextField searchField;
    public DatePicker otField = null;
    public DatePicker doField = null;
    int i = 0;
    List<Appointment> appointments;

    public void OnMouseClicked() {
        btnDelete.setDisable(appointmentTable.getSelectionModel().getSelectedItem() == null);
    }

    public void OnBack() throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/account.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void OnEdit() throws IOException {
        AppointmentModel appointmentModel = appointmentTable.getSelectionModel().getSelectedItem();
        ClientSocket.getInstance().setUserId(appointmentModel.getId());
        Stage stage = (Stage) btnEdit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/user/add_appointment.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void OnDelete() throws IOException {
        Request requestModel = new Request();
        AppointmentModel appointmentModel = appointmentTable.getSelectionModel().getSelectedItem();
        requestModel.setRequestMessage(new Gson().toJson(new Appointment(appointmentModel.getId())));
        requestModel.setRequestType(Requests.DELETE_APPOINTMENT);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();
        appointmentTable.getItems().remove(appointmentModel);
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        ClientSocket.getInstance().getInStream().readLine();
    }

    public void onLogOut() throws IOException {
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        ClientSocket.getInstance().setUser(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (ClientSocket.getInstance().getUser().getRole().equals(Roles.ADMIN.toString())) {
            btnDelete.setVisible(true); // отображение buttons;
        }
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        service.setCellValueFactory(new PropertyValueFactory<>("service"));
        client.setCellValueFactory(new PropertyValueFactory<>("client"));
        GetService<Appointment> batchGetService = new GetService<>(Appointment.class);
        Type listType = new TypeToken<ArrayList<Appointment>>() {
        }.getType();
        appointments = new Gson().fromJson(batchGetService.GetEntities(Requests.GET_ALL_APPOINTMENTS), listType);
        ObservableList<AppointmentModel> list = FXCollections.observableArrayList();
        if (appointments.size() != 0) {
            for (Appointment appointment : appointments) {
                AppointmentModel tableAppointment = new AppointmentModel(appointment.getId(),
                                                                         appointment.getEndTime().toString(),
                                                                         appointment.getStarTime().toString(),
                                                                         appointment.getDate().toString(),
                                                                         appointment.getService().getService()
                                                                                    .getName(),
                                                                         appointment.getClient().getName());
                list.add(tableAppointment);
            }
        }
        appointmentTable.setItems(list);
    }

    public void onSort() {
        appointments.sort(new Comparator<Appointment>() {
            public int compare(Appointment o1, Appointment o2) {
                return o1.getClient().getName().compareTo(o2.getClient().getName());
            }
        });
        ObservableList<AppointmentModel> list = FXCollections.observableArrayList();
        if (appointments.size() != 0) {
            for (Appointment appointment : appointments) {
                AppointmentModel tableAppointment = new AppointmentModel(appointment.getId(),
                                                                         appointment.getEndTime().toString(),
                                                                         appointment.getStarTime().toString(),
                                                                         appointment.getDate().toString(),
                                                                         appointment.getService().getService()
                                                                                    .getName(),
                                                                         appointment.getClient().getName());
                list.add(tableAppointment);
            }
        }
        appointmentTable.setItems(list);
    }

    public void onFilter() throws InterruptedException {
        if (otField.getValue() == null || doField.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Заполните все поля.");
            alert.showAndWait();
            Thread.sleep(1500);
        } else {
            int status = java.sql.Date.valueOf(otField.getValue().toString())
                                      .compareTo(java.sql.Date.valueOf(doField.getValue().toString()));
            if (status > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Выберите корректный диапазон.");
                alert.showAndWait();
                Thread.sleep(1500);
                return;
            }
            GetService<Appointment> batchGetService = new GetService<>(Appointment.class);
            Type listType = new TypeToken<ArrayList<Appointment>>() {
            }.getType();
            appointments = new Gson().fromJson(batchGetService.GetEntities(Requests.GET_ALL_APPOINTMENTS), listType);
            ObservableList<AppointmentModel> list = FXCollections.observableArrayList();
            if (appointments.size() != 0) {
                for (Appointment appointment : appointments) {
                    int start = appointment.getDate().compareTo(java.sql.Date.valueOf(otField.getValue().toString()));
                    int end = appointment.getDate().compareTo(java.sql.Date.valueOf(doField.getValue().toString()));
                    if (start >= 0 && end <= 0) {
                        AppointmentModel tableAppointment = new AppointmentModel(appointment.getId(),
                                                                                 appointment.getEndTime().toString(),
                                                                                 appointment.getStarTime().toString(),
                                                                                 appointment.getDate().toString(),
                                                                                 appointment.getService().getService()
                                                                                            .getName(),
                                                                                 appointment.getClient().getName());
                        list.add(tableAppointment);
                    }
                }
            }
            if (list.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Записей нет.");
                alert.showAndWait();
                Thread.sleep(1500);
                return;
            }
            appointmentTable.setItems(list);
        }
    }

    public void onSearch() throws InterruptedException {
        GetService<Appointment> batchGetService = new GetService<>(Appointment.class);
        Type listType = new TypeToken<ArrayList<Appointment>>() {
        }.getType();
        appointments = new Gson().fromJson(batchGetService.GetEntities(Requests.GET_ALL_APPOINTMENTS), listType);
        ObservableList<AppointmentModel> list = FXCollections.observableArrayList();
        if (appointments.size() != 0) {
            for (Appointment appointment : appointments) {
                if (appointment.getService().getService().getName().equalsIgnoreCase(searchField.getText())) {
                    AppointmentModel tableAppointment = new AppointmentModel(appointment.getId(),
                                                                             appointment.getEndTime().toString(),
                                                                             appointment.getStarTime().toString(),
                                                                             appointment.getDate().toString(),
                                                                             appointment.getService().getService()
                                                                                        .getName(),
                                                                             appointment.getClient().getName());
                    list.add(tableAppointment);
                }
            }
        }
        if (list.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Записей нет или ничего не найдено.");
            alert.showAndWait();
            Thread.sleep(1500);
            return;
        }
        appointmentTable.setItems(list);
    }

    public void onReset() throws InterruptedException {
        GetService<Appointment> batchGetService = new GetService<>(Appointment.class);
        Type listType = new TypeToken<ArrayList<Appointment>>() {
        }.getType();
        appointments = new Gson().fromJson(batchGetService.GetEntities(Requests.GET_ALL_APPOINTMENTS), listType);
        ObservableList<AppointmentModel> list = FXCollections.observableArrayList();
        if (appointments.size() != 0) {
            for (Appointment appointment : appointments) {
                AppointmentModel tableAppointment = new AppointmentModel(appointment.getId(),
                                                                         appointment.getEndTime().toString(),
                                                                         appointment.getStarTime().toString(),
                                                                         appointment.getDate().toString(),
                                                                         appointment.getService().getService()
                                                                                    .getName(),
                                                                         appointment.getClient().getName());
                list.add(tableAppointment);
            }
        }
        appointmentTable.setItems(list);
    }

    public void onStatistic() throws IOException {
        Stage stage = (Stage) btnStatistic.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin/statistic.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void onCheck() {
        AppointmentModel appointmentModel = appointmentTable.getSelectionModel().getSelectedItem();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z", Locale.US);
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));
        try (FileWriter writer = new FileWriter("check.txt", true)) {
            i++;
            String number = "Счет номер: ";
            String client = "Клиент: ";
            String service = "Услуга: ";
            String dat = "Дата оказания: ";
            writer.append("***************************************************");
            writer.append('\n');
            writer.write(number + i);
            writer.append('\n');
            writer.write(client + appointmentModel.getClient());
            writer.append('\n');
            writer.write(service + appointmentModel.getService());
            writer.append('\n');
            writer.write(dat + appointmentModel.getClient());
            writer.append('\n');
            writer.append('\n');
            writer.append('\n');
            writer.write("Дата формирования счета: " + date);
            writer.append('\n');
            writer.append("***************************************************");
            writer.append('\n');
            writer.append('\n');
            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }
}
