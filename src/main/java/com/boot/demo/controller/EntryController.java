package com.boot.demo.controller;

import com.boot.demo.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class EntryController {
    AuthenticationManager authenticationManager;

    UserService userService;

    public EntryController (UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping()
    public String entry() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String auth() {
        return "register";
    }

    @PostMapping("/register")
    public String auth(@RequestParam String login, @RequestParam String password, @RequestParam String confirmPassword, HttpServletRequest request) {
        if (!password.equals(confirmPassword)) {
            return "redirect:/register?error=true";
        }
        if (userService.findUserByName(login) != null) {
            return "redirect:/register?errorlogin=true";
        }
        userService.addUser(login, password);
        this.authenticateUserAndSetSession(login, password, request);
        return "redirect:/home";
    }

    private void authenticateUserAndSetSession(String username, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }
}
