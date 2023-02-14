package ca.mcgill.purposeful.controller;

import ca.mcgill.purposeful.exception.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** API for demonstrating how permissions work for access to endpoints */
@RestController
@RequestMapping({"/demo", "/demo/"})
public class DemoController {

  /**
   * Method to demonstrate how to restrict access to an endpoint
   *
   * @return a demo string
   */
  @GetMapping
  @PreAuthorize("hasAuthority('Moderator')")
  public String createIdea() {
    // Concept #1 - controllers don't need to take usernames as parameters - you get
    // them with the .getName() method of the token
    // all endpoints are secured by default - someone only gets to an endpoint if
    // they are logged in. Get the user like this
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();

    // Concept #2 - every user can have authorities. This is just a string in the
    // context associated with user - it is inside the authenication object
    // authentication.getAuthorities() - gets the list of authorities. A role is an
    // authority with the prefix _.
    // to restrict an endpoint for a particular authority, at the top of the method
    // you add @PreAuthorize to decide if a user has a right to connect to a method
    // ( returns 403 forbidden )
    throw new GlobalException(HttpStatus.I_AM_A_TEAPOT, "You are a teapot");
  }
}
