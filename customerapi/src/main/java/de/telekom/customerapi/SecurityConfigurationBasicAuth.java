package de.telekom.customerapi;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfigurationBasicAuth {

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user1 = User.builder()
                .username("admin")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user1);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                    .authorizeHttpRequests((authz) -> authz
                            .antMatchers("/**").hasRole("USER")
                            .anyRequest().authenticated()
                    )
                    .httpBasic(Customizer.withDefaults());

        return http.build();
}
    
}
