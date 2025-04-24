package bt.edu.gcit.usermicroservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.Arrays;
import bt.edu.gcit.usermicroservice.security.oauth.CustomerOAuth2UserService;
import bt.edu.gcit.usermicroservice.security.oauth.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class ShopemeSecurityConfig {

    public ShopemeSecurityConfig() {
        System.out.println("ShopemeSecurityConfig created");
    }

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomerOAuth2UserService oAuth2UserService;
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        System.out.println("H2i ");
        return new ProviderManager(Arrays.asList(authProvider()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        System.out.println("Hi ");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        System.out.println("UserDetailsService: " + userDetailsService.getClass().getName());
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        System.out.println("AuthProvider: " + authProvider.getClass().getName());
        return authProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").hasAnyAuthority("Admin")
                .requestMatchers(HttpMethod.GET, "/api/users/checkDuplicateEmail").hasAuthority("Admin")
                .requestMatchers(HttpMethod.PUT, "api/users/{id}").hasAuthority("Admin")
                .requestMatchers(HttpMethod.DELETE, "api/user/{id}").hasAuthority("Admin")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}/enabled").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/countries/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/countries").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/countries").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/countries").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/states/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/states/{country_id}").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/states").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/states").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/customer/*").permitAll())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.oauth2Login().userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler);

        // disable crsrf
        http.csrf().disable();
        return http.build();
    }
}
