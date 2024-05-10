package edu.tcu.cs.hogwartsartifactsonline.hogwartsuser;

import java.util.List;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;

@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){this.userRepository = userRepository;}



    public List<HogwartsUser> findAll() {return this.userRepository.findAll();}


    public HogwartsUser findById(Integer userId) {
        return this.userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("User", userId));
    }

    public HogwartsUser save(HogwartsUser newHogwartsUser) {
        return this.userRepository.save(newHogwartsUser);
    }

    public HogwartsUser update(Integer userId, HogwartsUser update) {
        HogwartsUser oldHogwartsUser = this.userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("User", userId));

        oldHogwartsUser.setUsername(update.getUsername());
        oldHogwartsUser.setEnabled(update.isEnabled());
        oldHogwartsUser.setRoles(update.getRoles());
        return this.userRepository.save(oldHogwartsUser);


    }

    public void delete(Integer userId) {
        this.userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("User", userId));
        this.userRepository.deleteById(userId);
    }
}
