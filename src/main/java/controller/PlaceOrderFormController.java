package controller;

import dto.CustomerDto;
import dto.ItemDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CustomerModel;
import model.ItemModel;
import model.impl.CustomerModelImpl;
import model.impl.ItemModelImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PlaceOrderFormController {

    
    public Button backBtn;
    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private TextField txtName;

    @FXML
    private ComboBox<String> cmbItemCodes;

    @FXML
    private TextField txtDesc;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private TextField txtQty;

    @FXML
    private TableView<?> tblItems;

    @FXML
    private TableColumn colCode;

    @FXML
    private TableColumn colDesc;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableColumn colAmount;

    @FXML
    private TableColumn colOption;

    @FXML
    private Button btnAddToCart;

    @FXML
    private Button btnPlaceOrder;
    private List<CustomerDto> customers;
    private List<ItemDto> items;
    private CustomerModel customerModel = new CustomerModelImpl();
    private ItemModel itemModel = new ItemModelImpl();

    @FXML
    void addToCartBtnSetOnAction(ActionEvent event) {

    }

    @FXML
    void placeOrderBtnOnaction(ActionEvent event) {

    }

    public void backBtnOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) tblItems.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void initialize(){
        loadItemCodes();
        loadCustomerIds();

        cmbItemCodes.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, id) -> {
            for(ItemDto dto : items){
                if(dto.getId().equals(id)){
                    txtDesc.setText(dto.getDesc());
                    txtUnitPrice.setText(String.valueOf(dto.getUnitPrice()));
                }
            }
        });

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, id) ->{
            for(CustomerDto dto: customers){
                if(dto.getId().equals(id)){
                    txtName.setText(dto.getName());
                }
            }
        } );
    }

    private void loadCustomerIds() {
        try {
            customers = customerModel.allCustomer();
            ObservableList<String> customerIds = FXCollections.observableArrayList();
            for(CustomerDto dto: customers){
                customerIds.add(dto.getId());
            }
            cmbCustomerId.setItems(customerIds);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    private void loadItemCodes() {
        try {
            items = itemModel.allItems();
            ObservableList<String> itemCodes = FXCollections.observableArrayList();
            for(ItemDto dto:items){
                itemCodes.add(dto.getId());
            }
            cmbItemCodes.setItems(itemCodes);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
