package model.impl;

import db.DBConnection;
import dto.ItemDto;
import model.ItemModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemModelImpl implements ItemModel {

    @Override
    public boolean updateItm(ItemDto dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE item SET description=?, unitprice=?, qtyOnHand=? WHERE id=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, dto.getDesc());
        pstm.setDouble(2,dto.getUnitPrice());
        pstm.setInt(3, dto.getQty());
        pstm.setString(4, dto.getId());
        return pstm.executeUpdate()>0;
    }

    @Override
    public boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO item VALUES(?,?,?,?)";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, dto.getId());
        preparedStatement.setString(2, dto.getDesc());
        preparedStatement.setDouble(3, dto.getUnitPrice());
        preparedStatement.setInt(4, dto.getQty());

        return preparedStatement.executeUpdate()>0;

    }

    @Override
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM item WHERE code=?";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        int result = preparedStatement.executeUpdate();
        return result>0;
    }

    @Override
    public ItemDto searchItem() {
        return null;
    }

    @Override
    public List<ItemDto> allItems() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM item";
        Statement stm = DBConnection.getInstance().getConnection().createStatement();
        ResultSet result = stm.executeQuery(sql);
        List<ItemDto> itemList = new ArrayList<>();

        while(result.next()){
            ItemDto dto = new ItemDto(
                    result.getString(1),
                    result.getString(2),
                    result.getDouble(3),
                    result.getInt(4));

            itemList.add(dto);
        }
        return itemList;

    }
}
