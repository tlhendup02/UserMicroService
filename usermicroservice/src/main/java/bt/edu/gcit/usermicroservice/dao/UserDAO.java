package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.User;
import java.util.List;

public interface UserDAO {
    User save(User user);
    User findById(int id);
    List<User> getAllUsers();
    void deleteUser(int id);
    boolean emailExists(String email);
}