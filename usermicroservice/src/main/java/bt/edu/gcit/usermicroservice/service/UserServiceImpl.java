package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import java.nio.file.Paths;


import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String uploadDir = "src/main/resources/static/images";

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
    public void deleteById(int theId) {
        userDAO.deleteById(theId);
    }

    @Override
    public Boolean isEmailDuplicate(String email) {
        User user = userDAO.findByEmail(email);
        return user != null;
    }

    @Transactional
    @Override
    public User updateUser(int id, User updatedUser) {
        User existingUser = userDAO.findById(id);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        
        if (!existingUser.getPassword().equals(updatedUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        existingUser.setRoles(updatedUser.getRoles());
        return userDAO.save(existingUser);
    }

    @Override
    @Transactional
    public void updateUserEnabledStatus(int id, boolean enabled) {
        userDAO.updateUserEnabledStatus(id, enabled);
    }

    @Override
    @Transactional
    public void uploadUserPhoto(int id, MultipartFile photo) throws IOException {
        User user = findById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        if (photo.getSize() > 1024 * 1024) {
            throw new FileSizeException("File Size must be < 1MB");
        }

        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());

        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;

        Path uploadPath = Paths.get(uploadDir, filename);
        photo.transferTo(uploadPath);

        user.setPhoto(filename);
        save(user);
    }
}