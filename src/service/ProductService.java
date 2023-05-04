package service;

import java.util.List;

import dao.ProductDao;
import vo.Product;

public class ProductService {

	ProductDao productDao = new ProductDao();
	
	/*
	 * 전체 상품 목록을 제공하는 서비스
	 * 
	 * 반환타입: List<Product>
	 * 메소드명: getAllproducts
	 * 매개변수: 없음
	 * 업무로직
	 * 	- ProductDao객체의 getProducts() 메소드를 호출해서 모든 상품을 조회하고 반환한다.
	 * 	- 1. 모든 상품정보를 조회하고 반환하기
	 * 		- ProductDao객체의 getProducts()를 호출해서 모든 상품정보를 조회하고 반환한다. 
	 */
	public List<Product> getAllproducts() {
		return productDao.getProducts();
	}
	
}
