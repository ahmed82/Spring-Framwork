package com.simplesoft.oe.web; 

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.simplesoft.oe.domain.Order;
import com.simplesoft.oe.service.OrderService;

/**
 * Custom controller (dispatcher) servlet
 */
public class ControllerServlet extends HttpServlet {
	private BeanFactory beanFactory;

	public void init() {
		beanFactory = new ClassPathXmlApplicationContext("spring-beans.xml");
	}

	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		if (req.getServletPath().equals("/index.request")) {
			handleIndex(req, resp);
		} else if (req.getServletPath().equals("/addOrder.request")) {
			handleAddOrder(req, resp);
		} else if (req.getServletPath().equals("/saveOrder.request")) {
			handleSaveOrder(req, resp);
		} else {
			throw new ServletException("unknown request:" + req.getServletPath());
		}
	}

	private void forwardToPage(String page, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/jsp/" + page).forward(req, resp);
	}

	private void handleIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		forwardToPage("index.jsp", req, resp);
	}

	private void handleAddOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		forwardToPage("addEditOrder.jsp", req, resp);
	}

	private Date convertToDate(String val) throws ServletException {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.parse(val);
		}
		catch (ParseException e) {
			throw new ServletException(e);
		}
	}
	
	private void handleSaveOrder(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		Order order = new Order();
		order.setCustomerName(req.getParameter("customerName"));
		order.setId(Long.parseLong(req.getParameter("id")));
		order.setOrderDate(convertToDate(req.getParameter("orderDate")));
		order.setProduct(req.getParameter("product"));

		OrderService orderService = (OrderService) beanFactory
				.getBean("orderService");
		orderService.saveOrder(order);

		forwardToPage("index.jsp", req, resp);
	}

}