package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.ConnUtils;
import vo.Order;
import vo.OrderItem;

public class OrderDao {

	/*
	 * 주문일련번호를 조회하는 SELECT 기능
	 * 
	 * 반환타입: int
	 * 메소드명: getSequence
	 * 매개변수: 없음
	 */
	public int getSequence() {
		String sql = "select sample_orders_seq.nextVal as seq from dual";
		
		try {
			int sequence = 0;
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			sequence = rs.getInt("seq");
			
			rs.close();
			pstmt.close();
			con.close();
			
			return sequence;
		
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	/*
	 * 주문정보를 INSERT하는 기능이다.
	 * 
	 * 반환타입: void
	 * 메소드명: insertOrder
	 * 매개변수: Order 
	 */
	public void insertOrder(Order order) {
		String sql = "insert into sample_orders "
				   + "(order_no, total_order_price, used_point, "
				   + "total_credit_price, deposit_point, user_no) "
				   + "values "
				   + "(?, ?, ?, ?, ?, ?)";
		
		try {
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, order.getNo());
			pstmt.setInt(2, order.getTotalOrderPrice());
			pstmt.setInt(3, order.getUsedPoint());
			pstmt.setInt(4, order.getTotalCreditPrice());
			pstmt.setInt(5, order.getDepositPoint());
			pstmt.setInt(6, order.getUserNo());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	/*
	 * 사용자번호로 주문내역정보를 조회하는 SELECT 기능
	 * 
	 * 반환타입: List<Order>
	 * 메소드명: getOrdersByUserNo
	 * 매개변수: int
	 */
	public List<Order> getOrdersByUserNo(int userNo) {
		String sql = "select * "
				   + "from sample_orders "
				   + "where user_no = ? "
				   + "order by order_no desc ";
		
		try {
			List<Order> orders = new ArrayList<>();
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, userNo);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Order order = new Order();
				
				order.setNo(rs.getInt("order_no"));
				order.setCreateDate(rs.getDate("order_create_date"));
				order.setStatus(rs.getString("order_status"));
				order.setTotalOrderPrice(rs.getInt("total_order_price"));
				order.setUsedPoint(rs.getInt("used_point"));
				order.setTotalCreditPrice(rs.getInt("total_credit_price"));
				order.setDepositPoint(rs.getInt("deposit_point"));
				order.setUserNo(rs.getInt("user_no"));
				
				orders.add(order);
			}
			
			rs.close();
			pstmt.close();
			con.close();
			
			return orders;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	/*
	 * 주문번호로 주문정보를 조회하는 SELECT 기능
	 * 
	 * 반환타입: Order
	 * 메소드명: getOrderByNo
	 * 매개변수: int
	 */
	public Order getOrderByNo(int orderNo) {
		String sql = "select * "
				   + "from sample_orders "
				   + "where order_no = ? ";
		
		try {
			Order order = null;
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, orderNo);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				order = new Order();
				
				order.setNo(rs.getInt("order_no"));
				order.setCreateDate(rs.getDate("order_create_date"));
				order.setStatus(rs.getString("order_status"));
				order.setTotalOrderPrice(rs.getInt("total_order_price"));
				order.setUsedPoint(rs.getInt("used_point"));
				order.setTotalCreditPrice(rs.getInt("total_credit_price"));
				order.setDepositPoint(rs.getInt("deposit_point"));
				order.setUserNo(rs.getInt("user_no"));
			}
			rs.close();
			pstmt.close();
			con.close();
			
			return order;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
}



















