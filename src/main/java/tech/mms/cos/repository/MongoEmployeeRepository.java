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
/*
ID = eindeutige Kennung, die verwendet wird, um ein Objekt oder einen Datensatz zu identifizieren. -> Entity vs Value Object
UUID = ist eine spezielle Art von ID, die so gestaltet ist, dass sie weltweit eindeutig ist.
 */


// Hat der Employee in der MongoDB eine ID. Falls nein, warum nicht. Falls ja, was ist die ID und wo kommt sie her
/*
Ja, zumindest sieht es so aus.
Beispiel  "_id": ObjectId("6707c2c9f9264f501885ac01") aus collection: management-app.employees
 */


// Employee erweitern um eine ID (UUID), Google "mongojack id entity"


// Anlegen eines neuen Employees, MongoDB hat eine ID fßr den neuen Employee, welcher eine UUID ist



// MongoDB: Collection (im Verlgeich zu Relationaler Datenbank), Was ist ein Index, Was ist ein Shard
/*
MongoDB ist eine NoSQL-Datenbank und eine Collection entspricht einer Tabelle in einer rationalen Datenbank
Ein Index (in einer Datenbank) ist wie ein Inhaltsverzeichnis. Es verhilft einem dabei, Daten schneller zu finden
mithilfe seiner Struktur.
Ein Shard ist eine Unterteilung der Datenbank um Daten auf mehrere Server zu verteilen.
 */


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
