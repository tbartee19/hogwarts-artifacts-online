package edu.tcu.cs.hogwartsartifactsonline.hogwartsuser;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private HogwartsUser u1, u2, u3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample user setup
        u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("harry.potter");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("STUDENT");

        u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("hermione.granger");
        u2.setPassword("123456");
        u2.setEnabled(true);
        u2.setRoles("STUDENT");

        u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("ron.weasley");
        u3.setPassword("123456");
        u3.setEnabled(true);
        u3.setRoles("STUDENT");
    }

    @Test
    void testFindAllSuccess() {
        List<HogwartsUser> expectedUsers = Arrays.asList(u1, u2, u3);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<HogwartsUser> result = userService.findAll();
        assertEquals(3, result.size());
        assertTrue(result.containsAll(expectedUsers));
    }

    @Test
    void testFindByIdSuccess() {
        when(userRepository.findById(1)).thenReturn(Optional.of(u1));

        HogwartsUser result = userService.findById(1);
        assertEquals("harry.potter", result.getUsername());
    }

    @Test
    void findByIdNotFound() {
        when(userRepository.findById(4)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.findById(4));
    }

    @Test
    void saveSuccess() {
        when(userRepository.save(any(HogwartsUser.class))).thenReturn(u1);

        HogwartsUser result = userService.save(u1);
        assertEquals("harry.potter", result.getUsername());
    }

    @Test
    void updateSuccess() {
        when(userRepository.findById(1)).thenReturn(Optional.of(u1));
        when(userRepository.save(any(HogwartsUser.class))).thenReturn(u1);

        HogwartsUser updatedUser = new HogwartsUser();
        updatedUser.setUsername("harry.potter.updated");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("STUDENT");

        HogwartsUser result = userService.update(1, updatedUser);
        assertEquals("harry.potter.updated", result.getUsername());
    }

    @Test
    void updateNotFound() {
        when(userRepository.findById(4)).thenReturn(Optional.empty());

        HogwartsUser nonExistentUpdate = new HogwartsUser();
        nonExistentUpdate.setUsername("unknown.user");
        nonExistentUpdate.setEnabled(true);
        nonExistentUpdate.setRoles("UNKNOWN");

        assertThrows(ObjectNotFoundException.class, () -> userService.update(4, nonExistentUpdate));
    }

    @Test
    void deleteSuccess() {
        when(userRepository.findById(1)).thenReturn(Optional.of(u1));
        doNothing().when(userRepository).deleteById(1);

        userService.delete(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteNotFound() {
        when(userRepository.findById(4)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.delete(4));
        verify(userRepository, never()).deleteById(4);
    }
}
