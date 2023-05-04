package service;

import java.util.List;

import dao.OrderDao;
import dao.OrderItemDao;
import dao.PointHistoryDao;
import dao.ProductDao;
import dao.UserDao;
import dto.OrderDetailDto;
import dto.OrderItemDto;
import vo.Order;
import vo.OrderItem;
import vo.PointHistory;
import vo.Product;
import vo.User;

public class OrderService {

	private PointHistoryDao pointHistoryDao = new PointHistoryDao();
	private ProductDao productDao = new ProductDao();
	private OrderDao orderDao = new OrderDao();
	private OrderItemDao orderItemDao = new OrderItemDao();
	private UserDao userDao = new UserDao();
	
	/*
	 * 나의 주문내역 조회 서비싀를 제공한다.
	 * 
	 * 반환타입:List<Order>
	 * 메소드명: getMyOrders
	 * 매개변수: int
	 * 업무로직
	 * 	- 전달받은 사용자번호에 해당하는 사용자의 주문내역정보를 제공한다.
	 * 	- 1. 주문내역정보를 조회하고 반환하기
	 * 		- OrderDao객체의 getOrdersByUserNo() 메소드를 호출해서
	 * 		  사용자번호에 해당하는 주문내역정보를 조회하고, 반환한다. 
	 */
	public List<Order> getMyOrders(int userNo) {
		return orderDao.getOrdersByUserNo(userNo);
	}
	
	
	/*
	 * 바로구매 서비스를 제공한다.
	 * 
	 * 반환타입: void
	 * 메소드이름: order
	 * 매개변수: int, int, int
	 * 업무로직
	 * 	- 주문정보, 주문상품정보를 저장한다.
	 * 	- 상품의 재고, 사용자의 포인트를 변경한다.
	 * 	- 포인트적립이력을 저장한다.
	 * 	- 1. 상품정보를 조회한다. 
	 * 		- 상품번호로 상품정보를 조회한다.
	 * 		- 상품정보가 존재하지 않으면 예외를 던진다.
	 * 	- 2. 주문하기
	 * 		- OrderDao객체의 getSequene()를 호출해서 주문번호를 조회한다.
	 * 		- 총주문금액을 계산한다.
	 * 		- 총결제금액을 계산한다.
	 * 		- 적립포인트를 계산한다.
	 *
	 * 		- Order객체를 생성한다.
	 * 		- Order객체에 주문번호, 총주문금액, 총결제금액, 적립포인트, 사용자번호를 저장한다. 
	 * 		- OrderDao의 insertOrder()를 호출해서 테이블에 저장시킨다.
	 * 	-3. 주문상품 저장하기 
	 * 		- OrderItem객체를 생성한다.
	 * 		- OrderItem객체에 주문번호, 상품번호, 구매수량, 상품가격을 저장한다.
	 * 		- OrderItemDao의 insertOrderItem()메소드를 호출해서 테이블에 저장시킨다. 
	 */
	public void order(int productNo, int amount, int userNo) {
		// 상품정보 조회하기
		Product product = productDao.getProductByNo(productNo);
		if(product == null) {
			throw new RuntimeException("상품정보가 존재하지 않습니다.");
		}
		
		// 신규 주문 일련번호 조회하기
		int orderNo = orderDao.getSequence();
		
		// 주문정보 저장하기
		int totalOrderPrice = product.getPrice()*amount;
		int usedPoint = 0;
		int totalCreditPrice = product.getPrice()*amount;
		int depositPoint = (int)(totalCreditPrice*0.01);
		
		Order order = new Order();
		order.setNo(orderNo);
		order.setTotalOrderPrice(totalOrderPrice);
		order.setUsedPoint(usedPoint);
		order.setTotalCreditPrice(totalCreditPrice);
		order.setDepositPoint(depositPoint);
		order.setUserNo(userNo);
		
		orderDao.insertOrder(order);
		
		// 주문상품정보 저장하기
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderNo(orderNo);
		orderItem.setProductNo(productNo);
		orderItem.setAmount(amount);
		orderItem.setPrice(product.getPrice());
		
		orderItemDao.insertOrderItem(orderItem); 
		
		// 포인트변경이력정보 저장하기
		User user = userDao.getUserByNo(userNo);
		int currentDepositPoint = user.getPoint();
		currentDepositPoint += depositPoint;
		
		PointHistory history = new PointHistory();
		history.setOrderNo(orderNo);
		history.setUserNo(userNo);
		history.setDepositPoint(depositPoint);
		history.setCurrentPoint(currentDepositPoint);
		
		pointHistoryDao.insertHistory(history);  
		
		// 상품의 재고수량 변경하기
		product.setStock(product.getStock() - amount);
		productDao.updateProduct(product);
		
		
		// 사용자의 포인트 변경하기
		user.setPoint(user.getPoint() + depositPoint);
		userDao.updateUser(user);
	}
	
	
	/*
	 * 주문상세정보를 제공하는 서비스
	 * 
	 * 반환타입: OrderDetailsDto
	 * 메소드이름: getOrderDetail
	 * 매개변수: int
	 * 업무로직
	 * 	- 주문번호에 해당하는 주문상세정보(주문정보, 주문상품정보)를 조회해서 반환한다.
	 * 	- 1. 주문정보를 조회한다.
	 * 		- OrderDao 객체의 getOrderByNo()를 호출해서 주문번호에 조회한다.
	 * 	- 2. 주문상품정보를 조회한다. 
	 * 		- OrderItemDao객체의 getOrderItemDtosByOrderNo()를 호출해서 주문번호에 해당하는 
	 * 		  주문상품정보를 전부 조회한다.
	 * 	- 3. OrderDetailDto객체를 반환한다.
	 * 		- OrderDetailDto객체를 생성한다.
	 * 		- OrderDetailDto객체의 멤버변수에 각각 주문정보와 주문상품정보를 저장하고 반환한다.
	 */
	public OrderDetailDto getOrderDetail(int orderNo, int userNo) {
		Order order = orderDao.getOrderByNo(orderNo);
		if (order == null) {
			throw new RuntimeException("주문번호가 존재하지 않습니다.");
		}
		
		if (order.getUserNo() != userNo) {
			throw new RuntimeException("다른 사용자의 주문정보는 조회할 수 없습니다.");
		}
		
		List<OrderItemDto> items = orderItemDao.getOrderItemDtosByOrderNo(orderNo);
		
		OrderDetailDto dto = new OrderDetailDto();
		dto.setOrder(order);
		dto.setItems(items);
		
		return dto;
	}
	
	
	/*
	 * 포이트변경 이력을 지공하는 서비스이다.
	 * 
	 * 반환타입: List<PointHistory>  
 	 * 메소드명: getMyPointHistories
	 * 매개변수: int
	 * 업무로직
	 * 	- 전달받은 사용자번호에 해당하는 조회된 포인트 변경이력정보를 반환한다.
	 * 	- 1. 포인트 변경이력정보 조회하기
	 * 		- PointHistoryDao의 getPointHistoriesByUserNo() 메소드를 호출해서
	 * 		  사용자의 포인트변경이력정보를 조회하고, 반환한다.
	 */
	public List<PointHistory> getMyPointHistories(int userNo) {
		return pointHistoryDao.getHistoryByUserNo(userNo);
	}
	
}














