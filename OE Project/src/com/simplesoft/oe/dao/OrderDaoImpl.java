package com.simplesoft.oe.dao; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.simplesoft.oe.domain.Order;

/**
 * Implementation of OrderDao that uses JDBC
 */
public class OrderDaoImpl implements OrderDao {
	private Properties columnMappings;
	
	private synchronized long getNextId(){
		return System.currentTimeMillis();
	}
	
	public void addOrder(Order order) {
		order.setId(getNextId());

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			String sql = "insert into orders(id,customer_name,order_date,product) values(?,?,?,?)";
			//String sql = "insert into orders(" + columnMappings.get("id")
			//		+ ", " + columnMappings.get("customerName") + ", "
			//		+ columnMappings.get("orderDate") + ", "
			//		+ columnMappings.get("product") + ") values(?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, order.getId());
			stmt.setString(2, order.getCustomerName());
			stmt.setDate(3, new java.sql.Date(order.getOrderDate().getTime()));
			stmt.setString(4, order.getProduct());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		System.out.println("order dao saved " + order.getCustomerName());
	}//addOrder

	private Connection getConnection() {
		try {
			Context initCtx = new InitialContext();
			DataSource ds = (DataSource) initCtx
					.lookup("java:comp/env/jdbc/OEDB");

			Connection conn = ds.getConnection();
			return conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}