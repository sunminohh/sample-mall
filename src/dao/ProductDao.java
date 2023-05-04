package dao;

import java.nio.channels.NonWritableChannelException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.ConnUtils;
import vo.Product;

public class ProductDao {
	
	// 상품가격, 할인율, 재고수량을 변경하는 메소드
	// 변경가능한 모든것을 포함하는 update문 작성
	public void updateProduct(Product product) {
		String sql = "update sample_products "
				   + "set"
				   + "		product_price = ?, "
				   + "		product_discount_rate = ?, "
				   + "		product_stock = ? "
				   + "where product_no = ? ";	
		
		try {
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, product.getPrice());
			pstmt.setDouble(2, product.getDiscountRate());
			pstmt.setInt(3, product.getStock());
			pstmt.setInt(4, product.getNo());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	// 상품번호에 따라 상품 하나를 조회하는 메소드
	public Product getProductByNo(int productNo) {
		String sql = "select * "
				   + "from sample_products "
				   + "where product_no = ? ";
		
		try {
			Product product = null;
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, productNo);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				product = new Product();
				product.setNo(rs.getInt("product_no"));
				product.setName(rs.getString("product_name"));
				product.setMaker(rs.getString("product_maker"));
				product.setPrice(rs.getInt("product_price"));
				product.setDiscountRate(rs.getDouble("product_discount_rate"));
				product.setStock(rs.getInt("product_stock"));
				product.setCreateDate(rs.getDate("product_create_date"));
				 
			}
			rs.close();
			pstmt.close();
			con.close();
			
			return product;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	
	/*
	 * 모든 상품정보 조회하기 - SELECT 작업
	 * 
	 * 반환타입: List<Product>
	 * 메소드명: getProducts()
	 * 매개변수: 없음
	 */
	public List<Product> getProducts() {
		String sql = " select * "
				   + "from sample_products "
				   + "order by product_no asc";
		
		try {
			List<Product> productList = new ArrayList<>();
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setNo(rs.getInt("product_no"));
				product.setName(rs.getString("product_name"));
				product.setMaker(rs.getString("product_maker"));
				product.setPrice(rs.getInt("product_price"));
				product.setDiscountRate(rs.getDouble("product_discount_rate"));
				product.setStock(rs.getInt("product_stock"));
				product.setCreateDate(rs.getDate("product_create_date"));
				 
				productList.add(product);
			}
			
			rs.close();
			pstmt.close();
			con.close();
			
			return productList;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
}