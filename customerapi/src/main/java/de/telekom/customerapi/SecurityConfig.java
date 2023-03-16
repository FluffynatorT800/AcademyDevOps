package de.telekom.customerapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    private Environment env;
    String passAdmin = env.getProperty("spring.security.user.password");
    @Bean
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin")
                                .password(passAdmin).roles("USER");
    }

    @Bean
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").authorizeRequests().anyRequest().hasRole("User")
                .and().formLogin().loginPage("login,jsp")
                .failureUrl("/login.jsp?error=1").loginProcessingUrl("/login")
				.permitAll().and().logout()
				.logoutSuccessUrl("/index.html");
    }
}
