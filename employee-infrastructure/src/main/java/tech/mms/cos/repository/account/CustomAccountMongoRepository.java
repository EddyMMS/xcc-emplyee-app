package tech.mms.cos.repository.account;

import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import tech.mms.cos.core.auth.account.AccountRepository;
import tech.mms.cos.core.auth.account.model.Account;
import tech.mms.cos.core.auth.account.model.GithubAccount;

// TODO: Model eine Klasse mit dem Namen "Account" anelegst. Username und password, roles (list) und
// departments (list).
// TODO: Laden des Accounts aus der Datenbank beim überprüfen der Auth. (Neues repo)

// TODO: BCryptPasswordEncoder. Was ist ein Password Salt, was ist eine Password rainbow table, wie
// funktioniert BCryptPasswordEncoder?
/*
BCryptPasswordEncoder ist eine Implementierung der PasswordEncoder-Schnittstelle in Spring Security.
Es verwendet die BCrypt-Funktion, einen starken Hashing-Algorithmus, um Passwörter sicher zu hashen.

Ein Password Salt ist eine zufällige Zeichenkette, die vor dem Hashing zum Passwort hinzugefügt wird.

Eine Rainbow Table ist eine vorkonfigurierte Tabelle, die zum "Cracken" von Passwort-Hashes verwendet wird.
Sie enthält vorgefertigte Hash-Werte, die Angreifer nutzen können, um Passwörter aus einer Datenbank gestohlener Hashes zu entschlüsseln.
 */

// TODO: Password wird in DB verschlüsselt gespeichert und beim überprüfen ob das Password
// übereinstimmt auch BCryptPasswordEncoder verwenden.

@Repository
public class CustomAccountMongoRepository implements AccountRepository {

  private MongoTemplate mongoTemplate;
  private DefaultAccountMongoRepository defaultAccountMongoRepository;
  private PasswordEncoder passwordEncoder;

  public CustomAccountMongoRepository(
      MongoTemplate mongoTemplate,
      PasswordEncoder passwordEncoder,
      DefaultAccountMongoRepository defaultAccountMongoRepository) {
    this.mongoTemplate = mongoTemplate;
    this.passwordEncoder = passwordEncoder;
    this.defaultAccountMongoRepository = defaultAccountMongoRepository;
  }

  @Override
  public Account saveAccount(Account account) {
    return defaultAccountMongoRepository.insert(account);
  }

  @Override
  public boolean checkPassword(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  @Override
  public Optional<Account> findByUsername(String username) {
    return defaultAccountMongoRepository.findByUsername(username);
  }

  @Override
  public Optional<GithubAccount> findGithubAccountByGithubId(int githubId) {
    return Optional.ofNullable(
        mongoTemplate.findOne(
            Query.query(Criteria.where("githubId").is(githubId)), GithubAccount.class));
  }

  /*
  private boolean isPasswordAlreadyEncoded(String password) {
      return password.matches("^\\$2[aybx]\\$.{56}$");
  }*/

}
