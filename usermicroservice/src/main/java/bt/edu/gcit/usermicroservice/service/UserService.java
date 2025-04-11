package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    User save(User user);
    User findById(int id);
    List<User> getAllUsers();
    void deleteUser(int id);
    // ResponseEntity<Boolean> checkDuplicateEmail(String email);
    User updateUser(User user);
    ResponseEntity<?> updateUserEnabledStatus(int id, Map<String, Boolean> requestBody);
    ResponseEntity<?> uploadPhoto(int id, String photoFilename); // or MultipartFile if handled here
    Boolean isEmailDuplicate(String email);
}