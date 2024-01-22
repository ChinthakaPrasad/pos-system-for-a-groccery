package model;

import dto.ItemDto;

public interface OrderModel {
    boolean addToCart(ItemDto dto);
    boolean placeOrder(ItemDto dto);

}
