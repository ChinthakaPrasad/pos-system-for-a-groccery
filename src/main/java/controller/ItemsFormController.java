package controller;

import db.DBConnection;
import dto.ItemDto;
import dto.tm.ItemTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.ItemModel;
import model.impl.ItemModelImpl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ItemsFormController {
    public Button backBtn;
    public AnchorPane pane;
    public TextField txtCode;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQty;
    public TableView tblItem;
    public Button updateBtn;
    public Button saveBtn;
    public TableColumn colId;
    public TableColumn colDesc;
    public TableColumn colPrice;
    public TableColumn colQty;
    public TableColumn colOption;
    private ItemModel itemModel = new ItemModelImpl();

    public void backBtnOnaction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();

    }

    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("delete"));

        loadItemTable();

        tblItem.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            setData((ItemTm) newValue);

        });

    }

    private void setData(ItemTm newValue) {

        if(newValue!=null){
            txtCode.setEditable(false);
            txtCode.setText(newValue.getId());
            txtDescription.setText(newValue.getDesc());
            txtUnitPrice.setText(String.valueOf(newValue.getUnitPrice()));
            txtQty.setText(String.valueOf(newValue.getQty()));
            }

    }

    private void loadItemTable() throws SQLException, ClassNotFoundException {
        ObservableList<ItemTm> tmList = FXCollections.observableArrayList();

        List<ItemDto> itemList = itemModel.allItems();

        for(ItemDto dto : itemList){
            Button btn = new Button("Delete");
            ItemTm i = new ItemTm(dto.getId(), dto.getDesc(), dto.getUnitPrice(), dto.getQty(),btn);

            btn.setOnAction(actionEvent -> {
                try {
                    itemModel.deleteItem(i.getId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            tmList.add(i);
        }

        tblItem.setItems(tmList);
    }

    private void deleteItem(String id) {

        try {boolean isDeleted = itemModel.deleteItem(id);

            if(isDeleted){
                new Alert(Alert.AlertType.INFORMATION,"Delete Successful").show();
                loadItemTable();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBtnOnaction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ItemDto dto = new ItemDto(txtCode.getText(), txtDescription.getText(), Double.parseDouble(txtUnitPrice.getText()), Integer.parseInt(txtQty.getText()));

        boolean isUpdated = itemModel.updateItm(dto);
        if(isUpdated){
            new Alert(Alert.AlertType.INFORMATION, "Update Successfull").show();
            loadItemTable();
        }
    }

    public void saveBtnOnaction(ActionEvent actionEvent) {
        ItemDto dto = new ItemDto(txtCode.getText(), txtDescription.getText(), Double.parseDouble(txtUnitPrice.getText()), Integer.parseInt(txtQty.getText()));

        try {boolean isAdded = itemModel.saveItem(dto);

            if(isAdded){
                new Alert(Alert.AlertType.INFORMATION, "Save successfull").show();
                loadItemTable();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void mouseClickAction(MouseEvent mouseEvent) {
        pane.setOnMouseClicked(event->{
            txtCode.setEditable(true);
            txtCode.setText("");
            txtQty.setText("");
            txtDescription.setText("");
            txtUnitPrice.setText("");
        });
    }
}
