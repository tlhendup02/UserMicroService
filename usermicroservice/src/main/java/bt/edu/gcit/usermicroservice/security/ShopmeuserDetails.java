package bt.edu.gcit.usermicroservice.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.entity.Role;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// If you just want to use the user detials containing the user email, password and roles, you can use the UserDetails interface directly
// and implement the methods. This is a simple implementation of UserDetails interface
// This class is used by Spring Security to get the user details from the database
// and use it for authentication and authorization
// We are creating a custom UserDetails class to hold the user details
public class ShopmeuserDetails implements UserDetails {

    private User user;

    public ShopmeuserDetails(User user) {
        this.user = user;
    }

    // The method below is used to get the user details from the database
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles(); // Here we are getting the roles of the user
        // We are using a set to store the roles of the user
        // We are using a list to store the authorities of the user

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (Role role : roles) { // Here we are iterating through the roles of the user
            // We are using a for loop to iterate through the roles of the user
            // then we are creating a new SimpleGrantedAuthority object
            // and adding it to the list of authorities
            // SimpleGrantedAuthority is a class that implements GrantedAuthority interface
            // It is used to represent the authority granted to the user
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return simpleGrantedAuthorities;
    }

    @Override
    public String getPassword() {
        System.out.println("Password: " + user.getPassword());
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        System.out.println("Username: " + user.getEmail());
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
} 
