package telran.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class AuthorizationConfigurations
{
	@Bean
	public SecurityFilterChain confugure(HttpSecurity http) throws Exception
	{
		http.httpBasic();
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(new ExpiredPasswordFilter(), BasicAuthenticationFilter.class);
		http.authorizeHttpRequests(autorize -> autorize	
				.requestMatchers("/account/register", "/model", "/models").permitAll()
				.requestMatchers("/account/user/*/role/*", "/account/revoke/*", 
						"account/activate/*").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/account/user/{login}").access(new 
						WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMIN')"))
				.requestMatchers(HttpMethod.GET, "/account/*/{login}").access(new 
						WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMIN')"))
				.requestMatchers("/driver/add", "/car/return", "/model/cars", "/car/rent")
					.hasAnyRole("CLERK")
				.requestMatchers("/driver/cars", "/drivers/car", "/driver")
					.hasAnyRole("CLERK", "DRIVER")
				.requestMatchers("/drivers/active", "/models/*").hasRole("STATIST")
				.requestMatchers("/*/remove", "/car/add", "/model/add").hasRole("MANAGER")
				.requestMatchers("/records").hasRole("TECHNICIAN")
				.requestMatchers("/car").authenticated()
				.requestMatchers(HttpMethod.PUT, "/account/*").authenticated()
				.anyRequest().denyAll());
		return http.build();
	}
}
