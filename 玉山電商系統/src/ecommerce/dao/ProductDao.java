package ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ecommerce.connection.ConnectionUtil;
import ecommerce.model.Product;

public class ProductDao {
	public void addProduct(Product product) {
		String sql = "{call sp_add_product(?, ?, ?, ?)}";
		try (Connection conn = new ConnectionUtil().getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, product.getProductID());
			pstmt.setString(2, product.getProductName());
			pstmt.setInt(3, product.getPrice());
			pstmt.setInt(4, product.getQuantity());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Product> getProductsInStock() {
		List<Product> products = new ArrayList<>();
		String sql = "{call sp_GetProductsInStock}";
		try (Connection conn = new ConnectionUtil().getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				Product product = new Product();
				product.setProductID(rs.getString("ProductID"));
				product.setProductName(rs.getString("ProductName"));
				product.setPrice(rs.getInt("Price"));
				product.setQuantity(rs.getInt("Quantity"));
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}
}
