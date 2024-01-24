package model.impl;

import db.DBConnection;
import dto.ItemDto;
import dto.OrderDto;
import model.OrderDetailsModel;
import model.OrderModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderModelImpl implements OrderModel {

    private OrderDetailsModel orderDetailsModel= new OrderDetailsModelImpl();
    @Override
    public boolean addToCart(ItemDto dto) {
        return false;
    }

    @Override
    public boolean placeOrder(OrderDto dto) throws SQLException, ClassNotFoundException {
        Connection connection = null;

        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO orders VALUES(?,?,?)";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, dto.getOrderId());
            pstm.setString(2, dto.getDate());
            pstm.setString(3, dto.getCustomerID());
            System.out.println(2);

            if(pstm.executeUpdate()>0){
                System.out.println(1);
                boolean idAdded = orderDetailsModel.saveOrderDetails(dto.getList());
                if(idAdded){
                    connection.commit();
                    return true;
                }
            }
        }catch (ClassNotFoundException ex  ){
            connection.rollback();
        }finally {
            connection.setAutoCommit(true);
        }

        return false;
    }

    @Override
    public OrderDto lastOrder() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM orders ORDER BY id DESC LIMIT 1";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        while(resultSet.next()){
            return new OrderDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    null
            );
        }
        return null;
    }
}
