package ibf2022.batch3.assessment.csf.orderbackend.respositories;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import jakarta.json.Json;

@Repository
public class PendingOrdersRepository {

	@Autowired
	@Qualifier("pending-orders")
	private RedisTemplate<String, String> template;

	// TODO: Task 3
	// WARNING: Do not change the method's signature.
	public void add(PizzaOrder order) {
		String jsonOrder = Json.createObjectBuilder()
				.add("orderId", order.getOrderId())
				.add("date", order.getDate().toString())
				.add("total", order.getTotal())
				.add("name", order.getName())
				.add("email", order.getEmail())
				.build().toString();

		template.opsForValue().append(order.getOrderId(), jsonOrder);
	}

	// TODO: Task 7
	// WARNING: Do not change the method's signature.
	public boolean delete(String orderId) {
		return template.delete(orderId);
	}

}
