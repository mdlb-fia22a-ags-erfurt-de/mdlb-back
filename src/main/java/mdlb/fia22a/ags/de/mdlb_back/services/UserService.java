package mdlb.fia22a.ags.de.mdlb_back.services;

import mdlb.fia22a.ags.de.mdlb_back.models.User;
import mdlb.fia22a.ags.de.mdlb_back.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticateAdmin(String username, String password) {
        User user = this.userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password) && user.isAdmin();
    }

    public User getUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    public User createUser(String username, String password, boolean isAdmin) {
        User newUser = new User();
        newUser.setUsername(username);

        newUser.setPassword(this.passwordEncoder.encode(password));
        newUser.setAdmin(isAdmin);
        return this.userRepository.save(newUser);
    }

    public void deleteUser(String username) {
        User user = this.userRepository.findByUsername(username);
        if (user != null) {
            this.userRepository.delete(user);
        }
    }

    public User updateUser(String username, String newPassword, Boolean isAdmin) {
        User user = this.userRepository.findByUsername(username);
        if (user != null) {
            if (newPassword != null && !newPassword.isEmpty()) {
                user.setPassword(this.passwordEncoder.encode(newPassword));
            }
            if (isAdmin != null) {
                user.setAdmin(isAdmin);
            }
            return this.userRepository.save(user);
        }
        return null;
    }

}
