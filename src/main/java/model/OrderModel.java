package model;

import dto.ItemDto;
import dto.OrderDto;

import java.sql.SQLException;

public interface OrderModel {
    boolean addToCart(ItemDto dto);
    boolean placeOrder(OrderDto dto) throws SQLException, ClassNotFoundException;
    OrderDto lastOrder() throws SQLException, ClassNotFoundException;

}
