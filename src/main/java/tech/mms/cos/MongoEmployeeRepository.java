package tech.mms.cos;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;

import java.util.ArrayList;
import java.util.List;

public class MongoEmployeeRepository implements EmployeeRepository {

    private static MongoEmployeeRepository mongoEmployeeRepository;

    private JacksonMongoCollection<Employee> coll;

    private MongoEmployeeRepository() {
        String connection = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(connection);
        MongoDatabase database = mongoClient.getDatabase("management-app");

        this.coll = JacksonMongoCollection.builder()
                .build(database, "employees", Employee.class, UuidRepresentation.STANDARD);
    }

    public static MongoEmployeeRepository get() {
        if (mongoEmployeeRepository == null) {
            mongoEmployeeRepository = new MongoEmployeeRepository();
        }

        return mongoEmployeeRepository;
    }

    @Override
    public void saveEmployee(Employee employee) {
        coll.save(employee);
    }

    @Override
    public List<Employee> readEmployees() {
        return coll.find().into(new ArrayList<>());
    }

}
