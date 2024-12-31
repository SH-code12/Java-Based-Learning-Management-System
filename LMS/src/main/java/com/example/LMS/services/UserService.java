package com.example.LMS.services;

import com.example.LMS.models.AuthentictationResponse;
import com.example.LMS.models.Profile;
import com.example.LMS.models.StudentModel;
import com.example.LMS.models.User;
import com.example.LMS.repositories.StudentRepository;
import com.example.LMS.repositories.UserRepository;
import com.example.LMS.SpringSecurity.jwtService;
import com.example.LMS.repositories.profileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final com.example.LMS.repositories.profileRepository profileRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final jwtService jwtService;

    // Register a new user
    public AuthentictationResponse register(User user) {


        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }


        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }


        System.out.println("user role: "+user.getRole());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(Objects.equals(user.getRole(), "ROLE_STUDENT")){
            StudentModel student = new StudentModel(user.getName(),  user.getRole(), user.getPassword(),user.getEmail());
            Profile profile = new Profile(student);
            studentRepository.save(student);
            profileRepository.save(profile);
        }
        else
        {
            Profile profile = new Profile(user);
            userRepository.save(user);
            profileRepository.save(profile);

        }

        String jwtToken = jwtService.generateToken(user);
        return new AuthentictationResponse(jwtToken);
    }


    public AuthentictationResponse login(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isEmpty() || !passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String jwtToken = jwtService.generateToken(existingUser.get());
        return new AuthentictationResponse(jwtToken);
    }


    public ResponseEntity<Object> getAllUsers() {
        // Fetching all users from the database
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.status(404).body("No users found");
        }

        return ResponseEntity.ok(users);
    }

    @PostMapping("/editprofile")
    public ResponseEntity<Object> editProfile(@RequestBody Profile userProfile) {

        Profile existingProfile = profileRepository.getProfileById(userProfile.getUser().getId());

        if (existingProfile != null) {
            User user = existingProfile.getUser();

            boolean isNameChanged = !user.getName().equals(userProfile.getUser().getName());
            boolean isEmailChanged = !user.getEmail().equals(userProfile.getUser().getEmail());

            if (isNameChanged || isEmailChanged) {
                Optional<User> existingUser = userRepository.findByNameAndEmail(userProfile.getUser().getName(), userProfile.getUser().getEmail());

                if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("The name '" + userProfile.getUser().getName() + "' and email '" + userProfile.getUser().getEmail() + "' are already in use by another user. Please choose a different name or email.");
                }

                user.setName(userProfile.getUser().getName());
                user.setEmail(userProfile.getUser().getEmail());
            }


            existingProfile.setAddress(userProfile.getAddress());
            existingProfile.setPhoneNumber(userProfile.getPhoneNumber());
            user.setRole(userProfile.getUser().getRole());
            user.setPassword(userProfile.getUser().getPassword());
            userRepository.save(user);
            profileRepository.save(existingProfile);

            return ResponseEntity.ok(existingProfile);
        } else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Profile not found for user ID: " + userProfile.getUser().getId());
        }
    }



    @GetMapping("/profiles/{userId}")
    public ResponseEntity<Object> ViewProfile(@PathVariable Integer userId) {

        Profile profile = profileRepository.getProfileById(userId);


        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Profile not found for user ID: " + userId);
        }
    }

}
