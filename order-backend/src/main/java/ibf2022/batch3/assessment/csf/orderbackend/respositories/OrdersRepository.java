package ibf2022.batch3.assessment.csf.orderbackend.respositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;

@Repository
public class OrdersRepository {

	@Autowired
	private MongoTemplate template; 

	private static final String COLLECTION_ORDERS = "orders";
	// TODO: Task 3
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	// Native MongoDB query here for add()
	// db.orders.insertOne({
	// _id: '123456',
	// date: ISODate("1970-07-15T00:28:51.140Z"),
	// total: 23.1,
	// name: 'dory',
	// email: 'fish@dory.com',
	// sauce: 'classic',
	// size: 2,
	// crust: 'thick',
	// comments: 'more burnt pls',
	// toppings: ['arugula', 'beef']
	// });

	public void add(PizzaOrder order) {
		Document d = new Document()
				.append("_id", order.getOrderId())
				.append("date", order.getDate())
				.append("total", order.getTotal())
				.append("name", order.getName())
				.append("email", order.getEmail())
				.append("sauce", order.getSauce())
				.append("size", order.getSize())
				.append("crust", order.getThickCrust() ? "thick" : "thin")
				.append("comments", order.getComments())
				.append("toppings", order.getTopplings());

		template.insert(d, COLLECTION_ORDERS);
	}

	// TODO: Task 6
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	// Native MongoDB query here for getPendingOrdersByEmail()
	public List<PizzaOrder> getPendingOrdersByEmail(String email) {

		return null;
	}

	// TODO: Task 7
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	// Native MongoDB query here for markOrderDelivered()
	public boolean markOrderDelivered(String orderId) {

		return false;
	}

}
