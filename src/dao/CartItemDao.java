package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.CartItemDto;
import util.ConnUtils;
import vo.CartItem;

public class CartItemDao {
	
	/*
	 * 사용자의 장바구니 아이템정보를 조회하는 SELECT 기능이다.
	 * 
	 * 반환타입: List<CartItemDta>
	 * 메소드명: getCartItemDtosByUserNo
	 * 매개변수: int
	 */
	public List<CartItemDto> getCartItemDtosByUserNo(int userNo) {
		String sql = "select c.product_no, p.product_price,"
				+ "			 c.item_amount, p.product_name "
				+ "   from sample_cart_items c, sample_products p "
				+ "	  where c.product_no = p.product_no "
				+ "	  and c.user_no = ?";
		
		try {
			List<CartItemDto> dtos = new ArrayList<>();
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, userNo);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CartItemDto dto = new CartItemDto();
				dto.setProductNo(rs.getInt("product_no"));
				dto.setProductPrice(rs.getInt("product_price"));
				dto.setItemAmount(rs.getInt("item_amount"));
				dto.setProductName(rs.getString("product_name"));
				
				dtos.add(dto);
			}
			
			rs.close();
			pstmt.close();
			con.close();
			
			return dtos;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	
	/*
	 * 장바구니에 아이템정보가 존재하는지 조회하는 SELECT 기능이다.
	 * 
	 *  반환타입: CartItem
	 *  메소드명: getCartItem
	 *  매개변수: int, int
	 */
	public CartItem getCartItem(int userNo, int productNo) {
		String sql = "select * "
				   + "from sample_cart_items "
				   + "where user_no = ? "
				   + "and product_no = ? ";
		
		try {
			CartItem cartItem = null;
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, userNo);
			pstmt.setInt(2, productNo);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				cartItem = new CartItem();
				cartItem.setUserNo(rs.getInt("user_no"));
				cartItem.setProductNo(rs.getInt("product_no"));
				cartItem.setAmount(rs.getInt("item_amount"));
			}
			
			rs.close();
			pstmt.close();
			
			return cartItem;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	/*
	 * 장바구니 아이템을 추가하는 INSERT 기능이다.
	 * 
	 * 반환타입: void
	 * 메소드명: insertCartItem
	 * 매개변수: CartItem
	 */
	public void insertCartItem(CartItem cartItem) {
		String sql = "insert into sample_cart_items "
				   + "(user_no, product_no, item_amount) "
				   + "values(?, ?, ?) ";
				
		
		try {
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, cartItem.getUserNo());
			pstmt.setInt(2, cartItem.getProductNo());
			pstmt.setInt(3, cartItem.getAmount());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	
	/*
	 * 장바구니 아이템정보를 삭제하는 DELETE 기능이다.
	 * 
	 * 반환타입: void
	 * 메소드명: deleteCartItemsByUserNo
	 * 매개변수: int
	 */
	public void deleteCartItemsByUserNo(int userNo) {
		String sql = "delete from sample_cart_items "
				   + " where user_no = ? ";
		
		try {
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, userNo);
			
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
}





















