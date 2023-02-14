package ca.mcgill.purposeful.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/** The VerificationRequest class, the model for all verification requests in the database */
@Entity
public class VerificationRequest {

  // ------------------------
  // Enumerations
  // ------------------------

  public enum Status {
    Pending,
    Approved,
    Refused
  }

  // ------------------------
  // VerificationRequest Attributes
  // ------------------------

  @Id private String companyOuiNumber;

  @Column(nullable = false)
  private String companyName;

  @Column(nullable = false)
  private String explanation;

  @Column(nullable = false)
  private String supportingDocumentUrl;

  @Column(nullable = false)
  private Status status;

  // ------------------------
  // VerificationRequest Constructor
  // ------------------------

  public VerificationRequest() {}

  // ------------------------
  // Getter/Setter Methods
  // ------------------------

  public String getCompanyOuiNumber() {
    return companyOuiNumber;
  }

  public void setCompanyOuiNumber(String companyOuiNumber) {
    this.companyOuiNumber = companyOuiNumber;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getExplanation() {
    return explanation;
  }

  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }

  public String getSupportingDocumentUrl() {
    return supportingDocumentUrl;
  }

  public void setSupportingDocumentUrl(String supportingDocumentUrl) {
    this.supportingDocumentUrl = supportingDocumentUrl;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
