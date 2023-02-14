package ca.mcgill.purposeful.model;

import ca.mcgill.purposeful.configuration.Authority;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.GenericGenerator;

/** The AppUser class, the model for all accounts in the database */
@Entity
public class AppUser {

  // ------------------------
  // AppUser Attributes
  // ------------------------

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  // ------------------------
  // AppUser Associations
  // ------------------------

  // Every AppUser has a set of Authorities that they can be granted
  @ElementCollection(targetClass = Authority.class)
  @CollectionTable(name = "app_user_authority", joinColumns = @JoinColumn(name = "app_user_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "authorities", nullable = false)
  private Set<Authority> authorities = new HashSet<Authority>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "appUser")
  private List<Role> roles;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "app_user_domain",
      joinColumns = @JoinColumn(name = "app_user_id"),
      inverseJoinColumns = @JoinColumn(name = "domain_id"))
  private Set<Domain> domains;

  // Interests are minimum 2 (2..*). This is enforced in the controller
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "app_user_topic",
      joinColumns = @JoinColumn(name = "app_user_id"),
      inverseJoinColumns = @JoinColumn(name = "topic_id"))
  private Set<Topic> interests;

  // ------------------------
  // AppUser Constructor
  // ------------------------

  public AppUser() {}

  // ------------------------
  // Getter/Setter Methods
  // ------------------------

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  public List<Role> getRole() {
    return roles;
  }

  public void setRole(List<Role> roles) {
    this.roles = roles;
  }

  public Set<Domain> getDomains() {
    return domains;
  }

  public void setDomains(Set<Domain> domains) {
    this.domains = domains;
  }

  public Set<Topic> getInterests() {
    return interests;
  }

  public void setInterests(Set<Topic> interests) {
    this.interests = interests;
  }
}
