package telran.accounting.servise;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import telran.accounting.domain.entities.UserAccount;
import telran.accounting.domain.repo.UserAccountsRepository;
import telran.accounting.dto.RolesResponseDto;
import telran.accounting.dto.UserAccountResponseDto;
import telran.accounting.dto.UserRegisterDto;
import telran.accounting.dto.UserUpdateDto;
import telran.accounting.dto.exceptions.AccountAlreadyActivatedException;
import telran.accounting.dto.exceptions.AccountAlreadyRevokedException;
import telran.accounting.dto.exceptions.RoleAlredyExistsException;
import telran.accounting.dto.exceptions.RoleNotExistsException;
import telran.accounting.dto.exceptions.UserAlreadyExsistsException;
import telran.accounting.dto.exceptions.UserNotFoundException;
import telran.accounting.dto.exceptions.WrongPasswordException;

@Service
public class AccountingManagement implements IAccounting, CommandLineRunner
{
	@Autowired
	UserAccountsRepository repository;
	
	@Autowired
	PasswordEncoder encoder;

	@Value("${password_length:5}")
	private int password_length;

	@Value("${n_last_hashcodes:3}")
	private int n_last_hashcodes;

	@Override
	public UserAccountResponseDto registration(UserRegisterDto user)
	{
		if (repository.existsById(user.getLogin()))
			throw new UserAlreadyExsistsException(user.getLogin());
		if (!isPasswordValid(user.getPassword()))
			throw new WrongPasswordException(user.getPassword());

		UserAccount account = new UserAccount(user.getLogin(), getHashCode(user.getPassword()), user.getFirstName(),
				user.getLastName());
		repository.save(account);
		return new UserAccountResponseDto(user.getLogin(), user.getFirstName(), user.getLastName(), account.getRoles());
	}

	private String getHashCode(String password)
	{
		return encoder.encode(password);
	}

	private boolean isPasswordValid(String password)
	{
		return password.length() >= password_length;
	}

	@Override
	public UserAccountResponseDto removeUser(String login)
	{
		UserAccount account = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		repository.delete(account);
		return new UserAccountResponseDto(login, account.getFirstName(), account.getLastName(), account.getRoles());
	}

	@Override
	public UserAccountResponseDto getUser(String login)
	{
		UserAccount account = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		return new UserAccountResponseDto(login, account.getFirstName(), account.getLastName(), account.getRoles());
	}

	@Override
	public UserAccountResponseDto updateUser(String login, UserUpdateDto account)
	{
		UserAccount accountMongo = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		if (account.getFirstName() != null)
			accountMongo.setFirstName(account.getFirstName());
		if (account.getLastName() != null)
			accountMongo.setLastName(account.getLastName());
		repository.save(accountMongo);
		return new UserAccountResponseDto(login, accountMongo.getFirstName(), accountMongo.getLastName(),
				accountMongo.getRoles());
	}

	@Override
	public boolean updatePassword(String login, String password)
	{
		if (!isPasswordValid(password))
			throw new WrongPasswordException(password);

		UserAccount account = repository.findById(login)
				.orElseThrow(() -> new UserNotFoundException());

		if (encoder.matches(password, account.getHashCode()))
			throw new WrongPasswordException(password);

		LinkedList<String> lastHashCodes = account.getLastHashCodes();
		if (isPasswordFromLast(lastHashCodes, password))
			throw new WrongPasswordException(password);

		if (lastHashCodes.size() == n_last_hashcodes)
			lastHashCodes.removeFirst();
		lastHashCodes.add(account.getHashCode());

		account.setHashCode(encoder.encode(password));
		account.setActivationDate(LocalDateTime.now());
		repository.save(account);
		return true;
	}

	private boolean isPasswordFromLast(LinkedList<String> lastHashCodes, String password)
	{
		return lastHashCodes.stream().anyMatch(c -> encoder.matches(password, c));
	}

	@Override
	public boolean revokeAccount(String login)
	{
		UserAccount account = repository.findById(login)
				.orElseThrow(() -> new UserNotFoundException());
		if (account.isRevoked())
			throw new AccountAlreadyRevokedException();

		account.setRevoked(true);
		repository.save(account);
		return true;
	}

	@Override
	public boolean activateAccount(String login)
	{
		UserAccount account = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		if (!account.isRevoked())
			throw new AccountAlreadyActivatedException();

		account.setRevoked(false);
		account.setActivationDate(LocalDateTime.now());
		repository.save(account);
		return true;
	}

	public String getPasswordHash(String login)
	{
		UserAccount account = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		return account.isRevoked() ? null : account.getHashCode();
	}

	@Override
	public LocalDateTime getActivationDate(String login)
	{
		UserAccount account = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		return account.isRevoked() ? null : account.getActivationDate();
	}

	@Override
	public RolesResponseDto getRoles(String login)
	{
		UserAccount account = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		return account.isRevoked() ? null : new RolesResponseDto(login, account.getRoles());
	}

	public RolesResponseDto addRole(String login, String role)
	{
		UserAccount account = repository.findById(login).orElseThrow(() -> new UserNotFoundException());

		HashSet<String> roles = account.getRoles();
		if (roles.contains(role))
			throw new RoleAlredyExistsException();

		roles.add(role);
		repository.save(account);
		return new RolesResponseDto(login, account.getRoles());
	}

	@Override
	public RolesResponseDto removeRole(String login, String role)
	{
		UserAccount account = repository.findById(login).orElseThrow(() -> new UserNotFoundException());

		HashSet<String> roles = account.getRoles();
		if (!roles.contains(role))
			throw new RoleNotExistsException();

		roles.remove(role);
		repository.save(account);
		return new RolesResponseDto(login, account.getRoles());
	}

	@Override
	public void run(String... args) throws Exception
	{
		if (!repository.existsById("admin"))
		{
			UserAccount account = new UserAccount("admin", 
					encoder.encode("admin"), "", "");
			account.setRoles(new HashSet<>(Arrays.asList("ADMIN")));
			repository.save(account);
		}

	}
}
