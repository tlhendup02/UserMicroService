package bt.edu.gcit.usermicroservice.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import bt.edu.gcit.usermicroservice.service.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    
    @Autowired
    public AuthServiceImpl(@Lazy AuthenticationManager authenticatoinManager, @Lazy UserDetailsService userDetailsService) {
        this.authenticationManager = authenticatoinManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public UserDetails login(String email, String password) {
        authenticationManager.authenticate(new
        UsernamePasswordAuthenticationToken(email, password));
        return userDetailsService.loadUserByUsername(email);
    }
}
