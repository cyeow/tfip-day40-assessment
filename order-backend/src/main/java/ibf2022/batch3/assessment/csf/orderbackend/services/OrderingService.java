package ibf2022.batch3.assessment.csf.orderbackend.services;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.OrdersRepository;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.PendingOrdersRepository;

@Service
public class OrderingService {

	@Autowired
	private OrdersRepository ordersRepo;

	@Autowired
	private PendingOrdersRepository pendingOrdersRepo;

	// TODO: Task 5
	// WARNING: DO NOT CHANGE THE METHOD'S SIGNATURE
	public PizzaOrder placeOrder(PizzaOrder order) throws OrderException {
		// retrieve price, orderId and date
		String pricingUrl = "https://pizza-pricing-production.up.railway.app/order";

		MultiValueMap<String, String> reqBody = new LinkedMultiValueMap<>();
		reqBody.add("name", order.getName());
		reqBody.add("email", order.getEmail());
		reqBody.add("sauce", order.getSauce());
		reqBody.add("size", order.getSize().toString());
		reqBody.add("thickCrust", order.getThickCrust().toString());
		reqBody.add("toppings", order.getTopplings().toString().replaceAll("\\[", "").replaceAll("\\]",""));
		reqBody.add("comments", order.getComments());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(List.of(MediaType.TEXT_PLAIN));

		HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(reqBody, headers);
		
		RestTemplate template = new RestTemplate();

		ResponseEntity<String> resp = null;
		try {
			resp = template.exchange(URI.create(pricingUrl), HttpMethod.POST, req, String.class);
		} catch (RestClientException e) {
			throw new OrderException("Error placing order");
		}

		String[] respBody = resp.getBody().split(",");

		// update order with details
		order.setOrderId(respBody[0]);
		order.setDate(new Date(Long.parseLong(respBody[1])));
		order.setTotal(Float.parseFloat(respBody[2]));

		// save order
		ordersRepo.add(order);
		pendingOrdersRepo.add(order);
		return order;
	}

	// For Task 6
	// WARNING: Do not change the method's signature or its implemenation
	public List<PizzaOrder> getPendingOrdersByEmail(String email) {
		return ordersRepo.getPendingOrdersByEmail(email);
	}

	// For Task 7
	// WARNING: Do not change the method's signature or its implemenation
	public boolean markOrderDelivered(String orderId) {
		return ordersRepo.markOrderDelivered(orderId) && pendingOrdersRepo.delete(orderId);
	}

}
