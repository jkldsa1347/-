package ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ecommerce.connection.ConnectionUtil;
import ecommerce.model.Order;
import ecommerce.model.OrderDetail;

public class OrderDao {
    // 新增訂單
    public void createOrder(Order order, List<OrderDetail> orderDetails) {
        // 使用 Stored Procedure 去存取資料庫
    	// 避免 SQL 注入攻擊，並提升代碼的重用性和可維護性。
    	String orderSQL = "insert into [dbo].[Order] (OrderID, MemberID, Price, PayStatus) "
    					+ "values (?, ?, ?, ?)"; //透過 Stored Procedure 去存取資料庫
        String orderDetailSQL = "insert into [dbo].[OrderDetail] (OrderID, ProductID, Quantity, StandPrice, ItemPrice) "
        					  + "values (?, ?, ?, ?, ?)";
        String updateProductSQL = "update [dbo].[Product] "
        						+ "set Quantity = Quantity | ? "
        						+ "where ProductID = ?";
        
        Connection conn = null;
        try {
            conn = new ConnectionUtil().getConnection();
            // 實作 Transaction 避免資料錯亂
            // 操作要麼全部成功，要麼全部失敗。
            // 在多步操作中。如果過程中的任何一步失敗，整個事務可以被回到起始狀態，就好像這個事務從未執行過一樣。
            conn.setAutoCommit(false);

            try (PreparedStatement orderStmt = conn.prepareStatement(orderSQL)) {
                orderStmt.setString(1, order.getOrderID());
                orderStmt.setString(2, order.getMemberID());
                orderStmt.setInt(3, order.getPrice());
                orderStmt.setInt(4, order.getPayStatus());
                orderStmt.executeUpdate();
            }

            try (PreparedStatement orderDetailStmt = conn.prepareStatement(orderDetailSQL);
                 PreparedStatement updateProductStmt = conn.prepareStatement(updateProductSQL)) {
                for (OrderDetail detail : orderDetails) {
                    orderDetailStmt.setString(1, detail.getOrderID());
                    orderDetailStmt.setString(2, detail.getProductID());
                    orderDetailStmt.setInt(3, detail.getQuantity());
                    orderDetailStmt.setInt(4, detail.getStandPrice());
                    orderDetailStmt.setInt(5, detail.getItemPrice());
                    orderDetailStmt.executeUpdate();

                    updateProductStmt.setInt(1, detail.getQuantity());
                    updateProductStmt.setString(2, detail.getProductID());
                    updateProductStmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 獲取單一訂單的明細
    public List<OrderDetail> getOrderDetailsByOrderId(String orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM [dbo].[OrderDetail] WHERE OrderID = ?";
        try (Connection conn = new ConnectionUtil().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderItemSN(rs.getInt("OrderItemSN"));
                    orderDetail.setOrderID(rs.getString("OrderID"));
                    orderDetail.setProductID(rs.getString("ProductID"));
                    orderDetail.setQuantity(rs.getInt("Quantity"));
                    orderDetail.setStandPrice(rs.getInt("StandPrice"));
                    orderDetail.setItemPrice(rs.getInt("ItemPrice"));
                    orderDetails.add(orderDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

    // 獲取所有訂單的明細
    public List<OrderDetail> getAllOrderDetails() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * "
        		   + "FROM [dbo].[OrderDetail]";
        try (Connection conn = new ConnectionUtil().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderItemSN(rs.getInt("OrderItemSN"));
                orderDetail.setOrderID(rs.getString("OrderID"));
                orderDetail.setProductID(rs.getString("ProductID"));
                orderDetail.setQuantity(rs.getInt("Quantity"));
                orderDetail.setStandPrice(rs.getInt("StandPrice"));
                orderDetail.setItemPrice(rs.getInt("ItemPrice"));
                orderDetails.add(orderDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }
}