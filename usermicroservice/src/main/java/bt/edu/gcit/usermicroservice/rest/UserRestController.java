package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import bt.edu.gcit.usermicroservice.entity.Role;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", consumes = "multipart/form-data")
    public User save(@RequestPart("firstName") @Valid @NotNull String firstName,
                     @RequestPart("lastName") @Valid @NotNull String lastName,
                     @RequestPart("email") @Valid @NotNull String email,
                     @RequestPart("password") @Valid @NotNull String password,
                     @RequestPart("photo") @Valid @NotNull MultipartFile photo,
                     @RequestPart("roles") @Valid @NotNull String rolesJson)
                     {
                        try {
                            User user = new User();
                            user.setFirstName(firstName);
                            user.setLastName(lastName);
                            user.setEmail(email);
                            user.setPassword(password);

                            ObjectMapper objectMapper = new ObjectMapper();
                            Set<Role> roles = objectMapper.readValue(rolesJson, new TypeReference<Set<Role>>(){});
                            user.setRoles(roles);
                            System.out.println("Uploading photo");

                            User savedUser = userService.save(user);

                            System.out.println("Uploading photo"+savedUser.getId().intValue());
                            userService.uploadUserPhoto(savedUser.getId().intValue(), photo);

                            return savedUser;
                        } catch (IOException e) {
                            throw new RuntimeException("Error while uploading photo", e);
                        }
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
    
}