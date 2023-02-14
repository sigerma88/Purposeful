package ca.mcgill.purposeful.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

/** The abstract Role class, the abstract model for all account roles in the database */
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class Role {

  // ------------------------
  // Role Attributes
  // ------------------------

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  // ------------------------
  // Role Associations
  // ------------------------

  @ManyToOne(optional = false)
  @JoinColumn(name = "app_user_id", nullable = false)
  private AppUser appUser;

  // ------------------------
  // Role Constructor
  // ------------------------

  public Role() {}

  // ------------------------
  // Getter/Setter Methods
  // ------------------------

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AppUser getAppUser() {
    return appUser;
  }

  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }
}
