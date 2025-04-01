package bt.edu.gcit.usermicroservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.FetchType;

import java.util.HashSet;
import java.util.Set;

import bt.edu.gcit.usermicroservice.entity.Role;

@Entity
//@Entity annotation marks the class as a persistent entity class - a class whose instances can be
//stored in the database.
@Table(name = "user")
// specifies the name of the database table that this entity is mapped to
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Used to mark id as the primary key and its value is auto-generated
    private Long id;

    //used to specify the details of the column
    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(length = 64)
    private String photo;

    // Used to establish a many-to-many relationship between user and role entities.
    @ManyToMany(fetch = FetchType.EAGER)
    // user_roles is a join table that holds the realtionship between user_id and role_id
    @JoinTable(name = "users_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles = new HashSet<>();

    // Constrcutors
    // The user class has 2 constrcutors: 
    // no-argument constructor 
    // constrcutor that initializes the email, password, firstname, lastName
    // the getRoles, setRoles & addRole methods are used to manage the roles associated with the user
    public User() {
        // Empty constructor
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    
}
