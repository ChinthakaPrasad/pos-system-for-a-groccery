package model.impl;

import dto.ItemDto;
import model.OrderModel;

public class OrderModelImpl implements OrderModel {
    @Override
    public boolean addToCart(ItemDto dto) {
        return false;
    }

    @Override
    public boolean placeOrder(ItemDto dto) {
        return false;
    }
}
