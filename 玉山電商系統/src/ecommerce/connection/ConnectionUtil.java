package ecommerce.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 連線資料庫
public class ConnectionUtil {
	private static String url = "jdbc:sqlserver://localhost:1433;" + "databaseName=EcommerceDB;" + "encrypt=false";
	private static String user = "sa";
	private static String pwd = "Dask8610@";

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, pwd);
	}
}