package ibf2022.batch3.assessment.csf.orderbackend.respositories;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Repository;

import com.mongodb.client.model.Updates;

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
	// db.orders.aggregate([
	// { $match: { delivered: {$in: [null, false]} } },
	// { $sort: { date: -1 } },
	// { $project: {date:1, total:1, _id:1}}
	// ]);

	public List<PizzaOrder> getPendingOrdersByEmail(String email) {
		MatchOperation mOp = Aggregation.match(Criteria.where("delivered").in(null, false));
		SortOperation sOp = Aggregation.sort(Direction.DESC, "date");
		ProjectionOperation pOp = Aggregation.project("date", "total", "_id");

		List<Document> docResults = template
				.aggregate(Aggregation.newAggregation(mOp, sOp, pOp), COLLECTION_ORDERS, Document.class)
				.getMappedResults();

		List<PizzaOrder> results = new LinkedList<>();
		docResults.forEach(d -> {
			PizzaOrder o = new PizzaOrder();
			o.setOrderId(d.getString("_id"));
			o.setDate(d.getDate("date"));
			o.setTotal(d.getDouble("total").floatValue());
			results.add(o);
		});

		return results;
	}

	// TODO: Task 7
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	// Native MongoDB query here for markOrderDelivered()
	//
	// db.orders.updateOne({_id:'7068d15fd2'},{$set: {"delivered": true}})

	public boolean markOrderDelivered(String orderId) {
		Query q = new Query().addCriteria(Criteria.where("_id").is(orderId));
		return template.updateFirst(q, new Update().set("delivered", true), Document.class, COLLECTION_ORDERS) != null;
	}

}
