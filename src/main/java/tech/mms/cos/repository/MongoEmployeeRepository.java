package tech.mms.cos.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.json.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
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

@Primary
@Repository
public interface MongoEmployeeRepository extends MongoRepository<Employee, String>, EmployeeRepository {

    @Override
    default void saveEmployee(Employee employee) {
        this.save(employee);
    }

    @Override
    default List<Employee> readEmployees() {
        return this.findAll();
    }

}

/*
@Primary
@Repository
public class MongoEmployeeRepository implements EmployeeRepository {

    private static MongoEmployeeRepository mongoEmployeeRepository;
    private final MappingMongoConverter mappingMongoConverter;

    private MongoCollection<Employee> coll;

    public MongoEmployeeRepository(MongoClient mongoClient) {
        String connection = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(connection);
        MongoDatabase database = mongoClient.getDatabase("management-app");
    }

    @Override
    public void saveEmployee(Employee employee) {
        coll.insertOne(employee);
    }

    @Override
    public List<Employee> readEmployees() {
        return coll.find().into(new ArrayList<>());
    }

}
*/