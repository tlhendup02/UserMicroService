package bt.edu.gcit.usermicroservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ShopemeSecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
        configurer.requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("Admin")
        .requestMatchers(HttpMethod.GET, "/api/users/all").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/users/checkDuplicateEmail").hasAuthority("Admin")
        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAuthority("Admin")
        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("Admin")
        .requestMatchers(HttpMethod.PUT, "/api/users/{id}/enabled").hasAuthority("Admin")
        );


        //disable crsrf
        http.csrf().disable();
        return http.build();
    }
}
