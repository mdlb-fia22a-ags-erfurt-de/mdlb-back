package mdlb.fia22a.ags.de.mdlb_back;

import mdlb.fia22a.ags.de.mdlb_back.models.User;
import mdlb.fia22a.ags.de.mdlb_back.repositories.UserRepository;
import mdlb.fia22a.ags.de.mdlb_back.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(this.userRepository, this.passwordEncoder);
    }

    @Test
    void authenticateAdmin_validCredentials_returnsTrue() {
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("hashedPassword");
        adminUser.setAdmin(true);

        when(this.userRepository.findByUsername("admin")).thenReturn(adminUser);
        when(this.passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);

        assertTrue(this.userService.authenticateAdmin("admin", "password"));
    }

    @Test
    void authenticateAdmin_invalidCredentials_returnsFalse() {
        when(this.userRepository.findByUsername("admin")).thenReturn(null);

        assertFalse(this.userService.authenticateAdmin("admin", "password"));
    }

    @Test
    void createUser_validData_returnsCreatedUser() {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("hashedPassword");
        newUser.setAdmin(false);

        when(this.passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(this.userRepository.save(any(User.class))).thenReturn(newUser);

        User createdUser = this.userService.createUser("newuser", "password", false);

        assertNotNull(createdUser);
        assertEquals("newuser", createdUser.getUsername());
        assertEquals("hashedPassword", createdUser.getPassword());
        assertFalse(createdUser.isAdmin());

        verify(this.passwordEncoder).encode("password");
        verify(this.userRepository).save(any(User.class));
    }

    @Test
    void updateUser_existingUser_returnsUpdatedUser() {
        User existingUser = new User();
        existingUser.setUsername("existinguser");
        existingUser.setPassword("oldHashedPassword");
        existingUser.setAdmin(false);

        when(this.userRepository.findByUsername("existinguser")).thenReturn(existingUser);
        when(this.passwordEncoder.encode("newpassword")).thenReturn("newHashedPassword");
        when(this.userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = this.userService.updateUser("existinguser", "newpassword", true);

        assertNotNull(updatedUser);
        assertEquals("existinguser", updatedUser.getUsername());
        assertEquals("newHashedPassword", updatedUser.getPassword());
        assertTrue(updatedUser.isAdmin());

        verify(this.passwordEncoder).encode("newpassword");
        verify(this.userRepository).save(any(User.class));
    }

    @Test
    void updateUser_nonExistingUser_returnsNull() {
        when(this.userRepository.findByUsername("nonexistinguser")).thenReturn(null);

        User updatedUser = this.userService.updateUser("nonexistinguser", "newpassword", true);

        assertNull(updatedUser);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_existingUser_deletesUser() {
        User existingUser = new User();
        existingUser.setUsername("existinguser");

        when(this.userRepository.findByUsername("existinguser")).thenReturn(existingUser);

        this.userService.deleteUser("existinguser");

        verify(this.userRepository).delete(existingUser);
    }

    @Test
    void deleteUser_nonExistingUser_doesNothing() {
        when(this.userRepository.findByUsername("nonexistinguser")).thenReturn(null);

        this.userService.deleteUser("nonexistinguser");

        verify(this.userRepository, never()).delete(any(User.class));
    }
}
