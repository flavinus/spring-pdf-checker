package fr.flavinus.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration
{
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // Disables auth
    http.csrf().disable().authorizeRequests()
        .antMatchers("/secure").authenticated()
        .anyRequest().anonymous();

    return http.build();
  }
}
