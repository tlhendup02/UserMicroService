package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public UserServiceImpl(UserDAO userDAO, BCryptPasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDAO.save(user);
    }

    @Override
    public User findById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userDAO.deleteUser(id);
    }

    @Override
    public ResponseEntity<Boolean> checkDuplicateEmail(String email) {
        boolean exists = userDAO.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return userDAO.save(user);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateUserEnabledStatus(int id, Map<String, Boolean> requestBody) {
        User user = userDAO.findById(id);
        if (user != null && requestBody.containsKey("enabled")) {
            user.setEnabled(requestBody.get("enabled"));
            userDAO.save(user);
            return ResponseEntity.ok("User status updated.");
        }
        return ResponseEntity.badRequest().body("User not found or invalid request.");
    }

    @Override
    @Transactional
    public ResponseEntity<?> uploadPhoto(int id, String photoFilename) {
        User user = userDAO.findById(id);
        if (user != null) {
            user.setPhoto(photoFilename);
            userDAO.save(user);
            return ResponseEntity.ok("Photo updated.");
        }
        return ResponseEntity.badRequest().body("User not found.");
    }
}