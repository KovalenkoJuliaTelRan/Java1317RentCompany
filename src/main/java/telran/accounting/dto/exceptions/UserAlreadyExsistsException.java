package telran.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExsistsException extends RuntimeException
{
	public UserAlreadyExsistsException(String login)
	{
		super("User " + login + " already exists");
	}
}
