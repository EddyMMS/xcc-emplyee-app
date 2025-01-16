package tech.mms.cos.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;
import tech.mms.cos.model.Employee;

import java.util.ArrayList;
import java.util.List;

// Anlegen eines neuen Eompoyees ßber das Programm in der MongoDB (Nutzung des ServiceLocators) (NoSQLBOoster)
// Was ist eine ID, Was ist eine UUID
// Hat der Employee in der MongoDB eine ID. Falls nein, warum nicht. Falls ja, was ist die ID und wo kommt sie her
// Employee erweitern um eine ID (UUID), Google "mongojack id entity"
// Anlegen eines neuen Employees, MongoDB hat eine ID fßr den neuen Employee, welcher eine UUID ist
// MongoDB: Collection (im Verlgeich zu Relationaler Datenbank), Was ist ein Index, Was ist ein Shard


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
