package tech.mms.cos.repository.account;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import tech.mms.cos.core.auth.account.model.Account;

public interface DefaultAccountMongoRepository extends MongoRepository<Account, UUID> {
  Optional<Account> findByUsername(String username);
}
