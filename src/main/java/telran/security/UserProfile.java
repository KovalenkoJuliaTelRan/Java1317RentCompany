package telran.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserProfile extends User
{
	private boolean passwordIsNotExpired;
	
	public UserProfile(String username, String password, 
			Collection<? extends GrantedAuthority> authorities, boolean passwordIsNotExpired)
	{
		super(username, password, authorities);
		this.passwordIsNotExpired = passwordIsNotExpired;
	}

	public boolean isPasswordIsNotExpired()
	{
		return passwordIsNotExpired;
	}
}
