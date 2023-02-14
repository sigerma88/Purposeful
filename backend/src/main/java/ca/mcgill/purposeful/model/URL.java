package ca.mcgill.purposeful.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

/** The URL class, the model for all URLs in the database */
@Entity
public class URL {

  // ------------------------
  // URL Attributes
  // ------------------------

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  @Column(nullable = false, unique = false)
  private String URL;

  @Column(nullable = false)
  private boolean presetIcon;

  // ------------------------
  // URL Constructor
  // ------------------------

  public URL() {}

  // ------------------------
  // Getter/Setter Methods
  // ------------------------

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getURL() {
    return URL;
  }

  public void setURL(String URL) {
    this.URL = URL;
  }

  public boolean isPresetIcon() {
    return presetIcon;
  }

  public void setPresetIcon(boolean presetIcon) {
    this.presetIcon = presetIcon;
  }
}
