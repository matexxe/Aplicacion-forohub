package api.forohub.forohub.controller;


import api.forohub.forohub.domain.user.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name="bearer-key")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Endpoint to retrieve a pageable list of users
    @GetMapping
    public ResponseEntity <Page<UserDataList>> listOfUsers(@PageableDefault(size=10)Pageable paged){
        // Retrieve and return a pageable list of active users
        return ResponseEntity.ok(userRepository.findByActiveTrue(paged).map(UserDataList::new));
    }

    // Endpoint to update user information
    @PutMapping
    @Transactional
    public ResponseEntity updateUserData (@RequestBody @Valid UserDataUpdate userDataUpdate){
        // Retrieve the user entity by ID for efficient update
        User user = userRepository.getReferenceById(userDataUpdate.id());

        // Update the user with new data
        user.updateData(userDataUpdate);

        // Return the updated user data in the response
        return ResponseEntity.ok(new UserResponseData(user.getId(),user.getName()));
    }
    // Endpoint to logically delete a user
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteUser(@PathVariable Long id){

        // Retrieve the user entity by ID for logical deletion
        User user = userRepository.getReferenceById(id);

        // Perform logical deletion of the user
        user.diactivateUser();

        // Return a 204 No Content response indicating successful deletion
       return ResponseEntity.noContent().build();
    }

    // Endpoint to retrieve user details by ID
    @GetMapping("/{id}")
    public ResponseEntity <UserResponseData> userDataReturn(@PathVariable Long id){
        // Retrieve the user entity by ID to return its details
        User user = userRepository.getReferenceById(id);

        // Construct and return detailed user data in the response
        var dUser = new UserResponseData(user.getId(), user.getName());
        return ResponseEntity.ok(dUser);
    }

}
