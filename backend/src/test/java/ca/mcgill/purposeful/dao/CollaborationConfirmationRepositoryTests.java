package ca.mcgill.purposeful.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.purposeful.model.*;
import ca.mcgill.purposeful.util.DatabaseUtil;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Class for testing the CollaborationConfirmationRepository and the persistence of
 * CollaborationConfirmation. CollaborationRequestRepository and CollaborationRequest are tested at
 * the same time
 *
 * @author Siger Ma
 */
@SpringBootTest
public class CollaborationConfirmationRepositoryTests {

  @Autowired private AppUserRepository appUserRepository;

  @Autowired private CollaborationConfirmationRepository collaborationConfirmationRepository;

  @Autowired private CollaborationRequestRepository collaborationRequestRepository;

  @Autowired private IdeaRepository ideaRepository;

  @Autowired private RegularUserRepository regularUserRepository;

  @Autowired private URLRepository urlRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  /** Clear the database before all tests */
  @BeforeAll
  public static void clearDatabaseBefore(@Autowired DatabaseUtil util) {
    util.clearDatabase();
  }

  /** Clear the database after each test */
  @AfterEach
  public void clearDatabaseAfter(@Autowired DatabaseUtil util) {
    util.clearDatabase();
  }

  @Test
  public void testPersistAndLoadCollaborationConfirmation() {
    // Create app user 1
    AppUser user1 = new AppUser();
    user1.setEmail("prof@gmail.com");
    user1.setFirstname("Rob");
    user1.setLastname("Sab");
    user1.setPassword(passwordEncoder.encode("password"));

    // Create corresponding regular user
    RegularUser regUser1 = new RegularUser();
    regUser1.setAppUser(user1);
    regUser1.setVerifiedCompany(false);

    // Create app user 2
    AppUser user2 = new AppUser();
    user2.setEmail("ta@gmail.com");
    user2.setFirstname("Neeraj");
    user2.setLastname("Katiyar");
    user2.setPassword(passwordEncoder.encode("password"));

    // Create corresponding regular user
    RegularUser regUser2 = new RegularUser();
    regUser2.setAppUser(user2);
    regUser2.setVerifiedCompany(false);

    // create basic URL
    URL url = new URL();
    url.setURL("www.url.com");

    // Create basic idea
    Idea idea = new Idea();
    idea.setDate(Date.from(Instant.now()));
    idea.setTitle("Brilliant Idea");
    idea.setPurpose("huge learning experience");
    idea.setDescription("It's a good idea");
    idea.setIconUrl(url);
    idea.setUser(regUser1);

    // Create collaboration request
    CollaborationRequest request = new CollaborationRequest();
    request.setStatus(VerificationRequest.Status.Pending);
    request.setAdditionalContact("Chat me on Slack URL");
    request.setMessage("I would like to collaborate with you on this idea");
    request.setIdea(idea);
    request.setRequester(regUser2);

    // Save objects to database
    urlRepository.save(url);
    appUserRepository.save(user1);
    regularUserRepository.save(regUser1);
    appUserRepository.save(user2);
    regularUserRepository.save(regUser2);
    ideaRepository.save(idea);
    collaborationRequestRepository.save(request);

    // Assert that the collaboration request is saved
    request = collaborationRequestRepository.findCollaborationRequestById(request.getId());
    assertNotNull(request);
    assertEquals(VerificationRequest.Status.Pending, request.getStatus());
    assertEquals("Chat me on Slack URL", request.getAdditionalContact());
    assertEquals("I would like to collaborate with you on this idea", request.getMessage());
    assertEquals(idea.getId(), request.getIdea().getId());
    assertEquals(regUser2.getId(), request.getRequester().getId());

    // Create collaboration confirmation
    CollaborationConfirmation confirmation = new CollaborationConfirmation();
    confirmation.setAdditionalContact("I prefer Discord URL");
    confirmation.setMessage("Welcome to the team!");
    collaborationConfirmationRepository.save(confirmation);

    // Update collaboration request
    request.setCollaborationConfirmation(confirmation);
    request.setStatus(VerificationRequest.Status.Approved);
    collaborationRequestRepository.save(request);

    // Assert that the collaboration confirmation is saved
    confirmation =
        collaborationConfirmationRepository.findCollaborationConfirmationById(confirmation.getId());
    assertNotNull(confirmation);
    assertEquals("I prefer Discord URL", confirmation.getAdditionalContact());
    assertEquals("Welcome to the team!", confirmation.getMessage());

    // Assert that the collaboration request is updated
    request = collaborationRequestRepository.findCollaborationRequestById(request.getId());
    assertNotNull(request);
    assertEquals(VerificationRequest.Status.Approved, request.getStatus());
    assertEquals(confirmation.getId(), request.getCollaborationConfirmation().getId());
  }
}
