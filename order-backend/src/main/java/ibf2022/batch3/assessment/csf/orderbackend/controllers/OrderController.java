package ibf2022.batch3.assessment.csf.orderbackend.controllers;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import ibf2022.batch3.assessment.csf.orderbackend.services.OrderException;
import ibf2022.batch3.assessment.csf.orderbackend.services.OrderingService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path = "/api")
public class OrderController {

	@Autowired
	private OrderingService orderSvc;

	// TODO: Task 3 - POST /api/order
	@PostMapping(path = "/order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> placeOrder(@RequestBody String payload) {
		try {
			PizzaOrder o = orderSvc.placeOrder(stringToPizzaOrder(payload));

			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.contentType(MediaType.APPLICATION_JSON)
					.body(
							Json.createObjectBuilder()
									.add("orderId", o.getOrderId())
									.add("date", o.getDate().toString())
									.add("name", o.getName())
									.add("email", o.getEmail())
									.add("total", o.getTotal())
									.build().toString());

		} catch (OrderException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.contentType(MediaType.APPLICATION_JSON)
					.body(
							Json.createObjectBuilder()
									.add("error", e.getMessage())
									.build().toString());
		}
	}

	// TODO: Task 6 - GET /api/orders/<email>
	@GetMapping(path = "/orders/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getOrdersByEmail(@PathVariable String email) {
		List<PizzaOrder> orders = orderSvc.getPendingOrdersByEmail(email);
		
		if (orders.size() > 0) {
			JsonArrayBuilder ab = Json.createArrayBuilder();
			orders.forEach(o -> {
				ab.add(Json.createObjectBuilder()
						.add("orderId", o.getOrderId())
						.add("total", o.getTotal())
						.add("date", o.getDate().toString())
						.build());

			});

			return ResponseEntity.status(HttpStatus.OK)
					.contentType(MediaType.APPLICATION_JSON)
					.body(ab.build().toString());

		}
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body("[]");

	}

	// TODO: Task 7 - DELETE /api/order/<orderId>

	private PizzaOrder stringToPizzaOrder(String json) {
		JsonObject o = Json.createReader(new StringReader(json)).readObject();
		PizzaOrder order = new PizzaOrder();
		order.setName(o.getString("name"));
		order.setEmail(o.getString("email"));
		order.setSauce(o.getString("sauce"));
		order.setSize(o.getJsonNumber("size").intValue());
		order.setThickCrust(o.getString("base").equalsIgnoreCase("thick"));

		JsonArray jArr = o.getJsonArray("toppings");
		List<String> toppings = new LinkedList<>();
		for (int i = 0; i < jArr.size(); i++) {
			toppings.add(jArr.getString(i));
		}
		order.setTopplings(toppings);
		order.setComments(o.getString("comments"));

		return order;
	}
}
