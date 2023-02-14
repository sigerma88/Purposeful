package ca.mcgill.purposeful.model;

import ca.mcgill.purposeful.configuration.Authority;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Secure User class that Spring can read, which wraps the AppUser class Makes AppUser compatible
 * with Spring Security
 */
public class SecurityUser implements UserDetails {

  // contains the AppUser object
  private final AppUser appUser;

  public SecurityUser(AppUser appUser) {
    this.appUser = appUser;
  }

  /**
   * Method that converts the authorities of the AppUser into an arraylist of
   * SimpleGrantedAuthoritys SimpleGrantedAuthorities is an class that Implements the
   * GrantedAuthority interface
   *
   * @return the authorities the user has as an array list of strings
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // array list of simple granted authorities
    ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
    // add the authorities
    for (Authority authority : appUser.getAuthorities()) {
      authorities.add(new SimpleGrantedAuthority(authority.toString()));
    }
    return authorities;
  }

  @Override
  public String getPassword() {

    return appUser.getPassword();
  }

  @Override
  public String getUsername() {

    return appUser.getUsername();
  }

  // Spring security methods that are required to be true for a user to be
  // authenticated
  // For now we can leave them blank

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
