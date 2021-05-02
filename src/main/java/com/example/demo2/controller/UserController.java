package com.example.demo2.controller;

import com.example.demo2.model.ERole;
import com.example.demo2.model.Role;
import com.example.demo2.model.User;
import com.example.demo2.payload.request.LoginRequest;
import com.example.demo2.payload.request.SignupRequest;
import com.example.demo2.payload.response.JwtResponse;
import com.example.demo2.payload.response.MessageResponse;
import com.example.demo2.repository.RoleRepository;
import com.example.demo2.repository.UserRepository;
import com.example.demo2.security.jwt.JwtUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/user")

public class UserController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private long CheckUserConnected() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> UserData = userRepository.findByUsername(userDetails.getUsername());
        return UserData.get().getId();
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllShopsIndex(@RequestParam(required = false) String title)
    {
        try {
            List<User> shops = new ArrayList<>();
            if (title == null)
                userRepository.findAll().forEach(shops::add);

//            else userRepository.findByNameContaining(title).forEach(shops::add);

            if (shops.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(shops, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllShops(@RequestParam(required = false) String title)
    {
        try {
            List<User> shops = new ArrayList<>();
            if (title == null)
                userRepository.findAll().forEach(shops::add);

//            else userRepository.findByNameContaining(title).forEach(shops::add);

            if (shops.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(shops, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/profile")
    public ResponseEntity<User> profile()
    {
        try {
            User user = userRepository.findById(CheckUserConnected()).get();

            User current_user = new User();

            current_user.setUsername(user.getUsername());
            current_user.setEmail(user.getEmail());
            current_user.setRoles(user.getRoles());

            current_user.setActivation_date(user.getActivation_date());
            current_user.setActive(user.getActive());
            current_user.setActived_by(user.getActived_by());

            current_user.setAdresse(user.getAdresse());
            current_user.setBanned(user.getBanned());
            current_user.setCreated_date(user.getCreated_date());
            current_user.setPhone_number(user.getPhone_number());

            current_user.setShops(user.getShops());

            return new ResponseEntity<>(current_user, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/BannedUsers")
    public ResponseEntity<List<String>> GetBannedUsers(@RequestParam(required = false) String title) {
        try {
//            List<User> BannedUsers = new ArrayList<User>();
            List<String> BannedUsers = new ArrayList<>();
            for (User user : userRepository.findAll()) {
                if (user.getBanned()) {

                    BannedUsers.add(user.getEmail());

                }
            }
            if (BannedUsers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(BannedUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/InactiveUsers")
    public ResponseEntity<List<User>> GetInactiveUsers(@RequestParam(required = false) String title) {
        try {
            List<User> InactiveUsers = new ArrayList<User>();
            for (User user : userRepository.findAll()) {
                if (!user.getActive()) {
                    InactiveUsers.add(user);
                }
            }
            if (InactiveUsers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(InactiveUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") long id) {
        Optional<User> UserData = userRepository.findById(id);

        if (UserData.isPresent()) {
            return new ResponseEntity<>(UserData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/Adduser")
    public ResponseEntity<?> Adduser( @Valid @RequestBody SignupRequest signUpRequest) {

        Optional<User> UserData = userRepository.findById(CheckUserConnected());

        System.out.println("Roles : ");
        for (Role name : UserData.get().getRoles()) {
            System.out.println(name.getName());
        }


        if (signUpRequest.getUsername().equals("") || signUpRequest.getEmail().equals("") || signUpRequest.getPassword().equals("")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: not null"));
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email is already in use!"));
        }



        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                true,
                new Date(),
                signUpRequest.getAdresse(),
                signUpRequest.getPhone_number(),
                false,
                UserData.get().getUsername(),
                new Date()
        );


        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> DeleteUser(@PathVariable("id") long id) {
        try {
            Optional<User> UserToDelete = userRepository.findById(CheckUserConnected());

            if (CheckUserConnected() == id){
                userRepository.deleteById(id);

                return new ResponseEntity<>("USER deleted",HttpStatus.OK);
            }

            for (Role name : UserToDelete.get().getRoles()) {
                if(name.getName().equals(ERole.ROLE_ADMIN)){
                    userRepository.deleteById(id);
                    return new ResponseEntity<>("USER deleted by ADMIN ",HttpStatus.OK);
                }
            }

            return new ResponseEntity<>("USER can't be deleted",HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> ActivateUser(@PathVariable("id") long id) {
        try {
            Optional<User> UserAdmin = userRepository.findById(CheckUserConnected());
            Optional<User> UserToActivate = userRepository.findById(id);

            for (Role name : UserAdmin.get().getRoles()) {
                if(name.getName().equals(ERole.ROLE_ADMIN)){
                    User user = UserToActivate.get();
                    user.setActive(true);
                    user.setActivation_date(new Date());
                    user.setActived_by(UserAdmin.get().getUsername());
                    userRepository.save(user);
                    return new ResponseEntity<>("USER Activated by ADMIN : "+UserAdmin.get().getUsername(),HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("USER can't be Activated",HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/bann/{id}")
    public ResponseEntity<String> BannUser(@PathVariable("id") long id) {
        try {
            Optional<User> UserAdmin = userRepository.findById(CheckUserConnected());
            Optional<User> UserToActivate = userRepository.findById(id);

            for (Role name : UserAdmin.get().getRoles()) {
                if(name.getName().equals(ERole.ROLE_ADMIN)){
                    User user = UserToActivate.get();
                    user.setBanned(true);
                    userRepository.save(user);
                    return new ResponseEntity<>("USER Banned by ADMIN : "+UserAdmin.get().getUsername(),HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("USER can't be Banned",HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> UpdateProfile(
            @PathVariable("id") long id,
            @RequestBody User userr) {
        try {
            Optional<User> UserToUpdate = userRepository.findById(id);

            if (CheckUserConnected() == UserToUpdate.get().getId()){

                if(UserToUpdate.get().getBanned()){
                    return new ResponseEntity<>("Your account is banned",HttpStatus.NOT_FOUND);
                }

                if(!UserToUpdate.get().getActive()){
                    return new ResponseEntity<>("not activated yet",HttpStatus.NOT_FOUND);
                }

                if ( userr.getEmail().equals("") || userr.getPhone_number().equals("")|| userr.getAdresse().equals("")) {
                    return new ResponseEntity<>("All champs must be completed",HttpStatus.NOT_FOUND);
                }


                if (userRepository.existsByEmail(userr.getEmail()) && !UserToUpdate.get().getEmail().equals(userr.getEmail())) {
                        return new ResponseEntity<>("Email is already in use!",HttpStatus.NOT_FOUND);
                }


                User user = UserToUpdate.get();
                user.setEmail(userr.getEmail());
                user.setAdresse(userr.getAdresse());
                user.setPhone_number(userr.getPhone_number());

//                Authentication authentication = authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                String jwt = jwtUtils.generateJwtToken(authentication);


                JSONObject obj = new JSONObject(); obj.put("status", 200);


                userRepository.save(user);

                return new ResponseEntity<>("User updated successfully",HttpStatus.OK);
            }
            return new ResponseEntity<>("USER can't be updated",HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/editPassword/{id}")
    public ResponseEntity<String> EditPassword(
            @PathVariable("id") long id,
            @RequestBody User userr,
            @RequestBody String newPass) {
        try {
            BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            Optional<User> UserToUpdate = userRepository.findById(id);

            if (CheckUserConnected() == UserToUpdate.get().getId()){

                if(UserToUpdate.get().getBanned()){
                    return new ResponseEntity<>("Your account is banned",HttpStatus.NOT_FOUND);
                }

                if(!UserToUpdate.get().getActive()){
                    return new ResponseEntity<>("not activated yet",HttpStatus.NOT_FOUND);
                }

                User user = UserToUpdate.get();

                System.out.println("OLD :" + user.getPassword() );
                System.out.println("NEW :" + newPass );
                System.out.println("NEW ENCODE :" + encoder.encode(newPass));
                System.out.println("BCRYPT :" + b.matches(user.getPassword(),userr.getPassword()));


//                if (b.matches(user.getPassword(),userr.getPassword())){
//                    user.setPassword(encoder.encode(newPass));
//                    userRepository.save(user);
//                    return new ResponseEntity<>("Password updated ",HttpStatus.OK);
//                }else{
//                    return new ResponseEntity<>("Password can't be updated",HttpStatus.NOT_FOUND);
//
//                }

            }
            return new ResponseEntity<>("USER can't be updated",HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
