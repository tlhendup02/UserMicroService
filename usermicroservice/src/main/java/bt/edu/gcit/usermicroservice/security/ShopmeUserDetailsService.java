package bt.edu.gcit.usermicroservice.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;

@Service
public class ShopmeUserDetailsService implements UserDetailsService {
    @Autowired
    private final UserDAO userDAO;

    @Autowired
    public ShopmeUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User nto found with email: " +email);
        }

        List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> {
            System.out.println("Role: "+ role.getName());

            return new SimpleGrantedAuthority(role.getName());
        })
        .collect(Collectors.toList());
        System.out.println("Authorities: "+ authorities);
        
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
