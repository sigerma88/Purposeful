package ca.mcgill.purposeful.controller;

import ca.mcgill.purposeful.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** API for authenticating users when they log in */
@RestController
@RequestMapping({"/login", "/login/"})
public class LoginController {

  @Autowired LoginService loginService;

  /**
   * This methods takes in a username and and password of a user, and authenticates them The
   * username and password are passed in the header of the request with the format:
   * "Authorization":"Basic encodeBase64(username:password)" encodeBase64 is a function that encodes
   * the username and password in base64 format
   *
   * @return a JWT token if the user is properly authenticated
   */
  @PostMapping()
  public String getToken() {
    // get the authentication object from the context
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // generate a token from the authentication object for the user
    String token = loginService.generateToken(authentication);
    return token;
  }
}
