package com.aws.login;

import com.aws.model.ApplicationUser;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class AuthenticationAuthorizationController {
    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/signedUp")
    public void signedUp(){
        return;
    }
    @PostMapping("/signup")
    public ResponseEntity<ApplicationUser> signup(@RequestBody ApplicationUser applicationUser, HttpServletRequest request) {
        applicationUser.setPassword(bCryptPasswordEncoder.encode(applicationUser.getPassword()));
        ApplicationUser save = applicationUserRepository.save(applicationUser);
        if (save != null) {
            HttpSession httpSession=httpSessionFactory.getObject();
            httpSession.setAttribute("userName",applicationUser.getUsername());
            return new ResponseEntity<>(applicationUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(applicationUser, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ApplicationUser> getUser(@RequestParam("username") String username) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser != null) {
            return new ResponseEntity<>(applicationUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
