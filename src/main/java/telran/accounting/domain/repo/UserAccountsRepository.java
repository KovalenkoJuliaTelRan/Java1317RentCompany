package telran.accounting.domain.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.accounting.domain.entities.UserAccount;

public interface UserAccountsRepository extends MongoRepository<UserAccount, String>
{

}
