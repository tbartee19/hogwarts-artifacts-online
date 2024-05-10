package edu.tcu.cs.hogwartsartifactsonline.hogwartsuser;

import java.util.List;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public Result findAllUsers() {
        List<HogwartsUser> foundHogwartsUsers = this.userService.findAll();

        List<UserDto> userDtos = foundHogwartsUsers.stream()
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.isEnabled(), user.getRoles()))
                .toList();
        
        return new Result(true, StatusCode.SUCCESS, "Find all success", userDtos);
    }

    @GetMapping
    public Result findUserById(@PathVariable Integer userId) {
        HogwartsUser foundHogwartsUser = this.userService.findById(userId);

        UserDto userDto = new UserDto(
            foundHogwartsUser.getId(),
            foundHogwartsUser.getUsername(),
            foundHogwartsUser.isEnabled(),
            foundHogwartsUser.getRoles()
    );

    return new Result(true, StatusCode.SUCCESS, "User found successfully", userDto);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody HogwartsUser newHogwartsUser) {
        HogwartsUser savedUser = this.userService.save(newHogwartsUser);
        UserDto userDto = new UserDto(savedUser.getId(), savedUser.getUsername(), savedUser.isEnabled(), savedUser.getRoles());

        return new Result(true,StatusCode.SUCCESS,"Add Success",savedUser);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto) {
       
        HogwartsUser update = new HogwartsUser();
        update.setId(userId);
        update.setUsername(userDto.getUsername());
        update.setEnabled(userDto.isEnabled());
        update.setRoles(userDto.getRoles());
        HogwartsUser updatedHogwartsUser = this.userService.update(userId, update);

        UserDto updatedUserDto = new UserDto(
            updatedHogwartsUser.getId(),
            updatedHogwartsUser.getUsername(),
            updatedHogwartsUser.isEnabled(),
            updatedHogwartsUser.getRoles()
        );
        return new Result(true,StatusCode.SUCCESS, "Update success",updatedUserDto);
    }

    @DeleteMapping
    public Result deleteUser(@PathVariable Integer userId) {
        this.userService.delete(userId);

        return new Result(true,StatusCode.SUCCESS,"Delete successful");
    }

}