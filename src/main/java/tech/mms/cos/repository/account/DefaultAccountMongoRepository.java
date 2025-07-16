package tech.mms.cos.repository.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import tech.mms.cos.core.model.Account;
import tech.mms.cos.core.model.Employee;

import java.util.Optional;
import java.util.UUID;


public interface DefaultAccountMongoRepository extends MongoRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);
}
