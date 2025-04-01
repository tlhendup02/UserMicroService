package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        userService.deleteUser(id);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id,@RequestBody User user) {
        user.setId((long) id);
        return userService.save(user);
    }

    @GetMapping("/users/duplicateemail")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email) {
        return userService.checkDuplicateEmail(email);
    }

    @PutMapping("/users/{id}/enabled")
    public ResponseEntity<?> updateUserEnabledStatus(
            @PathVariable int id,
            @RequestBody Map<String, Boolean> requestBody) {

        User existingUser = userService.findById(id);
        if (existingUser != null && requestBody.containsKey("enabled")) {
            existingUser.setEnabled(requestBody.get("enabled"));
            userService.save(existingUser);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Invalid user or request body");
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