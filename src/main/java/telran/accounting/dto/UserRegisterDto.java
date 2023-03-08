package telran.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserRegisterDto
{
	private String login;
	private String password;
	private String firstName;
	private String lastName;
}
