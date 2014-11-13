package com.simplesoft.oe.service;

import com.simplesoft.oe.dao.OrderDao;
import com.simplesoft.oe.domain.Order;

public class OrderServiceImpl implements OrderService{
	private OrderDao orderDao;
	
	@Override
	public void saveOrder(Order order) {
		if(order.getId() <= 0) {
			orderDao.addOrder(order);
		}
	}//saveOrder
	
	public void setOrderDap(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

}
