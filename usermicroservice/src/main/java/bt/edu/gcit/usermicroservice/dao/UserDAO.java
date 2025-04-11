package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.User;
import java.util.List;

public interface UserDAO {
    User save(User user);
    User findByEmail(String email);
    User findById(int theId);
    List<User> getAllUsers();
    void deleteById(int theId);
    void updateUserEnabledStatus(int id, boolean enabled);
}