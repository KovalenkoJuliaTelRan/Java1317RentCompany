package telran.accounting.domain.entities;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Document(collection = "user_accounts")
public class UserAccount
{
	@Id
	String login;
	@Setter
	String hashCode;
	@Setter
	String firstName;
	@Setter
	String lastName;
	@Setter
	HashSet<String> roles;
	@Setter
	LocalDateTime activationDate = LocalDateTime.now();
	@Setter
	boolean revoked;
	@Setter
	LinkedList<String> lastHashCodes = new LinkedList<>();
	
	public UserAccount(String login, String hashCode, String firstName, String lastName)
	{
		super();
		this.login = login;
		this.hashCode = hashCode;
		this.firstName = firstName;
		this.lastName = lastName;
		roles = new HashSet<>(Arrays.asList("USER"));
	}
	
	public UserAccount()
	{
		roles = new HashSet<>(Arrays.asList("USER"));
	}
}
