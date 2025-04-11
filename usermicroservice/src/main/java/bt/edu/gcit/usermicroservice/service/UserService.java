package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {
    User save(User user);
    User findById(int id);
    List<User> getAllUsers();
    void deleteById(int theId);
    // ResponseEntity<Boolean> checkDuplicateEmail(String email);
    User updateUser(int id, User updatedUser);
    void updateUserEnabledStatus(int id, boolean enabled);
    Boolean isEmailDuplicate(String email);
    void uploadUserPhoto(int id, MultipartFile photo) throws IOException;
}