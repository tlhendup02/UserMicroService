package bt.edu.gcit.usermicroservice.dao;

import java.util.List;
import bt.edu.gcit.usermicroservice.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;

@Repository
public class UserDAOImpl implements UserDAO {

    private final EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User save(User user) {
        return entityManager.merge(user);
    }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        System.out.println(users.size());
        if (users.isEmpty()) {
            return null;
        } else {
            System.out.println(users.get(0)+" "+users.get(0).getEmail());
            return users.get(0);
        }
    }

    @Override
    public User findById(int theId) {
        User user = entityManager.find(User.class, theId);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("FROM User ", User.class).getResultList();
    }

    @Override
    public void deleteById(int theId) {
        User user = findById(theId);
        entityManager.remove(user);
    }

    @Override
    public void updateUserEnabledStatus(int id, boolean enabled) {
        User user = entityManager.find(User.class, id);
        if (user == null){
            throw new UserNotFoundException("User not found with id: " + id);  
        }
        user.setEnabled(enabled);
        entityManager.persist(user);
    }

}