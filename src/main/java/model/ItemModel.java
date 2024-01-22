package model;

import dto.ItemDto;

import java.sql.SQLException;
import java.util.List;

public interface ItemModel {
    boolean updateItm(ItemDto dto) throws SQLException, ClassNotFoundException;
    boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    boolean deleteItem(String id) throws SQLException, ClassNotFoundException;
    ItemDto searchItem();
    List<ItemDto> allItems() throws SQLException, ClassNotFoundException;
}
