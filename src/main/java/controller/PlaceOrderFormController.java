package controller;

import dto.CustomerDto;
import dto.ItemDto;
import dto.OrderDetailsDto;
import dto.OrderDto;
import dto.tm.OrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.CustomerModel;
import model.ItemModel;
import model.OrderModel;
import model.impl.CustomerModelImpl;
import model.impl.ItemModelImpl;
import model.impl.OrderModelImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderFormController {

    
    public Button backBtn;
    public Label lblTotal;
    public Label lblOrderID;
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
    private TableView<OrderTm> tblItems;

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
    private ObservableList<OrderTm> orderTm = FXCollections.observableArrayList();
    private double tot=0;
    OrderModel orderModel = new OrderModelImpl();

    @FXML
    void addToCartBtnSetOnAction(ActionEvent event) {
        Button btn = new Button("Delete");
        Double amount = Double.parseDouble(txtUnitPrice.getText())*Integer.parseInt(txtQty.getText());

        OrderTm order = new OrderTm(
                cmbItemCodes.getValue().toString(),
                txtDesc.getText(),
                Integer.parseInt(txtQty.getText()),
                amount,
                btn);

        btn.setOnAction(actionEvent -> {
            tot-=order.getAmount();
            orderTm.remove(order);
            tblItems.refresh();
            lblTotal.setText(String.format("%.2f", tot));
        });
        boolean isExist= false;

        for(OrderTm tm:orderTm){
            if(order.getCode().equals(tm.getCode())){
                tm.setQty(tm.getQty()+Integer.parseInt(txtQty.getText()));
                tm.setAmount(tm.getAmount()+ order.getAmount());
                tot+=order.getAmount();
                isExist= true;
            }
        }
        if(!isExist){
            orderTm.add(order);
            tot+=order.getAmount();
        }
        tblItems.setItems(orderTm);
        tblItems.refresh();
        lblTotal.setText(String.format("%.2f", tot));

    }

    @FXML
    void placeOrderBtnOnaction(ActionEvent event) {
        List<OrderDetailsDto> list = new ArrayList<>();
        for(OrderTm tm:orderTm){
            list.add(new OrderDetailsDto(
                    lblOrderID.getText(),
                    tm.getCode(),
                    tm.getQty(),
                    tm.getAmount()/tm.getQty()
            ));
        }

        OrderDto dto = new OrderDto(
                lblOrderID.getText(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                cmbCustomerId.getValue(),
                list
        );

        boolean isAdded = false;
        try {
            isAdded = orderModel.placeOrder(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(isAdded){
            new Alert(Alert.AlertType.INFORMATION, "Order Succesfull").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Order Unsuccessfull").show();
        }


    }

    public String genarateId() throws SQLException, ClassNotFoundException {
        OrderDto lastOrder = orderModel.lastOrder();
        if(lastOrder!=null){
            String id = lastOrder.getOrderId();
            int num = Integer.parseInt(id.split("[D]")[1]);
            num++;
            return String.format("D%03d",num);
        }else{
            return "D001";
        }
    }

    public void backBtnOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) tblItems.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void initialize(){
        try {
            lblOrderID.setText(genarateId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

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
