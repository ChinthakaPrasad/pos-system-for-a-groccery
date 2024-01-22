package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import dto.CustomerDto;
import dto.tm.CustomerTm;
import model.CustomerModel;
import model.impl.CustomerModelImpl;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    public Button backBtn;
    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtSalary;

    @FXML
    private TableView<CustomerTm> tblCustomer;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colSalary;

    @FXML
    private TableColumn colOption;
    private CustomerModel customerModel = new CustomerModelImpl();

    @FXML
    void reloadBtnOnaction(ActionEvent event) {
        loadCustomerTable();

    }

    @FXML
    void saveBtnOnaction(ActionEvent event) {
        CustomerDto c = new CustomerDto(txtId.getText(), txtName.getText(), txtAddress.getText(), Double.parseDouble(txtSalary.getText()));

        try {boolean isAdded = customerModel.saveCustomer(c);
            if (isAdded) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Saved").show();
            }


        }catch (SQLIntegrityConstraintViolationException e){
            new Alert(Alert.AlertType.ERROR, "Customer ID duplicate").show();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
    
    private void loadCustomerTable(){
        ObservableList<CustomerTm> tmList= FXCollections.observableArrayList();
        try {
            List<CustomerDto> customerList = customerModel.allCustomer();
            for (CustomerDto dto:customerList) {
                Button btn = new Button("Delete");
                CustomerTm c = new CustomerTm(
                        dto.getId(),
                        dto.getName(),
                        dto.getAddress(),
                        dto.getSalary(),
                        btn
                );
                btn.setOnAction(actionEvent -> {
                    try {
                        customerModel.deleteCustomer(c.getId());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });

                tmList.add(c);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        tblCustomer.setItems(tmList);

    }

    private void deleteCustomer(String id) {

        try {
            boolean isDeleted = customerModel.deleteCustomer(id);
            if(isDeleted){
                new Alert(Alert.AlertType.INFORMATION, "Delete Successful").show();
                loadCustomerTable();
            }
            else {
                new Alert(Alert.AlertType.ERROR,"Delete Unsuccessful").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void updateBtnOnaction(ActionEvent event) {
        CustomerDto c= new CustomerDto(txtId.getText(), txtName.getText(), txtAddress.getText(), Double.parseDouble(txtSalary.getText()));

        try {
            boolean isUpdated = customerModel.updateCustomer(c);
            if(isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Update Successful").show();
                loadCustomerTable();

            }
            else {
                new Alert(Alert.AlertType.ERROR,"Update UnSuccessful").show();

            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadCustomerTable();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
                    setData(newValue);

                });

    }

    private void setData(CustomerTm newValue) {
        if(newValue!=null){
            txtId.setEditable(false);
            txtId.setText(newValue.getId());
            txtName.setText(newValue.getName());
            txtAddress.setText(newValue.getAddress());
            txtSalary.setText(String.valueOf(newValue.getSalary()));

        }
    }

    public void backBtnOnaction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) tblCustomer.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
}
