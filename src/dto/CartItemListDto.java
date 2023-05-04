package dto;

import java.util.List;

public class CartItemListDto {

	private List<CartItemDto> dtos;
	
	public List<CartItemDto> getDtos() {
		return dtos;
	}
	
	public void setDtos(List<CartItemDto> dtos) {
		this.dtos = dtos;
	}
	
	public int getTotalAmount() {
		int totalAmount = 0;
		
		for (CartItemDto dto : dtos) {
			totalAmount += dto.getItemAmount();
		}
		
		return totalAmount;
	}
	
	public int getTotalOrderPrice() {
		int totalOrderPrice = 0;
		
		for (CartItemDto dto : dtos) {
			int amount = dto.getItemAmount();
			int price = dto.getProductPrice();
			int orderPrice = price*amount;
			
			totalOrderPrice +=orderPrice;
		}
		
		return totalOrderPrice; 
	}
}














