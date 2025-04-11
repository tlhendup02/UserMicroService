package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/users/{id}")
    public User findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @GetMapping("/users/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteById(id);
    }

    @GetMapping("/users/checkDuplicateEmail")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);
        return ResponseEntity.ok(isDuplicate);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @PutMapping("/users/{id}/enabled")
    public ResponseEntity<?> updateUserEnabledStatus(
        @PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
            Boolean enabled = requestBody.get("enabled");
            userService.updateUserEnabledStatus(id, enabled);
            System.out.println("User enabled status updated successfully");
            return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/photo")
    public ResponseEntity<?> uploadPhoto(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file) {

        User user = userService.findById(id);
        if (user != null) {
            try {
                user.setPhoto(file.getOriginalFilename()); // or save file to disk and store path
                userService.save(user);
                return ResponseEntity.ok(user);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Failed to upload photo");
            }
        }
        return ResponseEntity.badRequest().body("User not found");
    }
}