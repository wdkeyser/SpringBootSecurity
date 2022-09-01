package willydekeyser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.cors().disable()
		.csrf().disable()
		.headers().frameOptions().disable()
		.and()
		.authorizeHttpRequests((authorize) -> authorize
				.antMatchers("/user").hasAuthority("ROLE_USER")
				.antMatchers("/admin").hasAuthority("ROLE_ADMIN")
				.antMatchers("/moderator").hasAuthority("ROLE_MODERATOR")
				.antMatchers(HttpMethod.GET, "/users", "/authorities").authenticated()
				.antMatchers("/").permitAll()
				.antMatchers("/login", "/logout").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.anyRequest().denyAll())
				.httpBasic()
				.and()
				.formLogin()
				.and()
				.logout(logout -> logout.deleteCookies("JSESSIONID"));
		return http.build();
	}

	@Bean
	MyUsersDetailsService myUsersDetailsService() {
		return new MyUsersDetailsService();
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
