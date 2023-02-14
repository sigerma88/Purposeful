package ca.mcgill.purposeful.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/** The RegularUser class, the model for accounts with regular user authority in the database */
@Entity
public class RegularUser extends Role {

  // ------------------------
  // RegularUser Attributes
  // ------------------------

  @Column(nullable = false)
  private boolean verifiedCompany;

  // ------------------------
  // RegularUser Associations
  // ------------------------

  @OneToOne(optional = true)
  @JoinColumn(name = "verification_request_id", nullable = true, unique = true)
  private VerificationRequest verificationRequest;

  // ------------------------
  // RegularUser Constructor
  // ------------------------

  public RegularUser() {}

  // ------------------------
  // Getter/Setter Methods
  // ------------------------

  public boolean isVerifiedCompany() {
    return verifiedCompany;
  }

  public void setVerifiedCompany(boolean verifiedCompany) {
    this.verifiedCompany = verifiedCompany;
  }

  public VerificationRequest getVerificationRequest() {
    return verificationRequest;
  }

  public void setVerificationRequest(VerificationRequest verificationRequest) {
    this.verificationRequest = verificationRequest;
  }
}
