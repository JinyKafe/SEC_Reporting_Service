package de.scope.scopeone.reporting.sec.app.server.conf;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${server.internal.http.auth.username}")
  private String username;

  @Value("${server.internal.http.auth.password}")
  private String password;

  private static String prefixPasswordWithNoop(String value) {
    if (!value.startsWith("{")) {
      return "{noop}" + value;
    }

    return value;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.antMatcher("/**").authorizeRequests().anyRequest().authenticated()
        .and()
        .httpBasic();
  }

  // The password should have "{noop}" prefixed to stop special encoding from happening.

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser(username)
        .password(prefixPasswordWithNoop(password))
        .roles("ADMIN");
  }
}

