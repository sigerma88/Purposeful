package ca.mcgill.purposeful.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;

import ca.mcgill.purposeful.configuration.Authority;
import ca.mcgill.purposeful.dao.AppUserRepository;
import ca.mcgill.purposeful.dao.ModeratorRepository;
import ca.mcgill.purposeful.dao.RegularUserRepository;
import ca.mcgill.purposeful.model.AppUser;
import ca.mcgill.purposeful.model.Moderator;
import ca.mcgill.purposeful.model.RegularUser;

/**
 * This class tests the AppUser service
 */
@ExtendWith(MockitoExtension.class)
public class TestAppUserService {

  @Mock
  private AppUserRepository appUserRepository;

  @Mock
  private RegularUserRepository regularUserRepository;

  @Mock
  private ModeratorRepository moderatorRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AppUserService appUserService;

  private static final String VALID_REGULARUSER_EMAIL_ONE = "regular.user.one@email.com";
  private static final String VALID_REGULARUSER_EMAIL_TWO = "regular.user.two@email.com";
  private static final String VALID_REGULARUSER_FIRSTNAME_ONE = "Rob";
  private static final String VALID_REGULARUSER_FIRSTNAME_TWO = "Marwan";
  private static final String VALID_REGULARUSER_LASTNAME_ONE = "Sab";
  private static final String VALID_REGULARUSER_LASTNAME_TWO = "Kanaan";

  private static final String VALID_MODERATOR_EMAIL_ONE = "moderator.user.one@email.com";
  private static final String VALID_MODERATOR_EMAIL_TWO = "moderator.user.two@email.com";
  private static final String VALID_MODERATOR_FIRSTNAME_ONE = "Rob";
  private static final String VALID_MODERATOR_FIRSTNAME_TWO = "Marwan";
  private static final String VALID_MODERATOR_LASTNAME_ONE = "Kanaan";
  private static final String VALID_MODERATOR_LASTNAME_TWO = "Sab";

  private static final String VALID_PASSWORD = "Password1";
  private static final String VALID_PASSWORD_ENCODED = "Password1Encoded";

  private static final String INVALID_EMAIL = "invalid.email";
  private static final String INVALID_PASSWORD_ONE = "invalid";
  private static final String INVALID_PASSWORD_TWO = "invalidPassword";
  private static final String INVALID_PASSWORD_THREE = "invalidpassword1";
  private static final String INVALID_PASSWORD_FOUR = "INVALIDPASSWORD1";

  /**
   * Mocking the repositories
   *
   * @author Siger Ma
   */
  @BeforeEach
  public void setMockOutput() {
    Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
      return invocation.getArgument(0);
    };

    lenient().when(appUserRepository.findAppUserByEmail(anyString()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(VALID_REGULARUSER_EMAIL_TWO)) {
            AppUser appUser = new AppUser();
            appUser.setEmail(VALID_REGULARUSER_EMAIL_TWO);
            appUser.setFirstname(VALID_REGULARUSER_FIRSTNAME_TWO);
            appUser.setLastname(VALID_REGULARUSER_LASTNAME_TWO);
            appUser.setPassword(VALID_PASSWORD);
            appUser.getAuthorities().add(Authority.User);
            return appUser;
          } else if (invocation.getArgument(0).equals(VALID_MODERATOR_EMAIL_TWO)) {
            AppUser appUser = new AppUser();
            appUser.setEmail(VALID_MODERATOR_EMAIL_TWO);
            appUser.setFirstname(VALID_MODERATOR_FIRSTNAME_TWO);
            appUser.setLastname(VALID_MODERATOR_LASTNAME_TWO);
            appUser.setPassword(VALID_PASSWORD);
            appUser.getAuthorities().add(Authority.Moderator);
            return appUser;
          } else {
            return null;
          }
        });

    lenient().when(passwordEncoder.encode(anyString()))
        .thenAnswer((InvocationOnMock invocation) -> {
          return invocation.getArgument(0) + "Encoded";
        });

    lenient().when(appUserRepository.save(any(AppUser.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(regularUserRepository.save(any(RegularUser.class)))
        .thenAnswer(returnParameterAsAnswer);
    lenient().when(moderatorRepository.save(any(Moderator.class)))
        .thenAnswer(returnParameterAsAnswer);
  }

  /**
   * Test the method that creates a new regular user
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateRegularUser() {
    // Create the regular user
    AppUser appUser = null;
    try {
      appUser = appUserService.registerRegularUser(VALID_REGULARUSER_EMAIL_ONE,
          VALID_PASSWORD, VALID_REGULARUSER_FIRSTNAME_ONE, VALID_REGULARUSER_LASTNAME_ONE);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    // Check the regular user
    assertNotNull(appUser);
    assertEquals(VALID_REGULARUSER_EMAIL_ONE, appUser.getEmail());
    assertEquals(VALID_REGULARUSER_FIRSTNAME_ONE, appUser.getFirstname());
    assertEquals(VALID_REGULARUSER_LASTNAME_ONE, appUser.getLastname());
    assertEquals(VALID_PASSWORD_ENCODED, appUser.getPassword());
  }

  /**
   * Test the method that creates a new regular user with an empty email
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateRegularUserWithEmptyEmail() {
    // Create the regular user
    AppUser appUser = null;
    try {
      appUser = appUserService.registerRegularUser("",
          VALID_PASSWORD, VALID_REGULARUSER_FIRSTNAME_ONE, VALID_REGULARUSER_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("Please enter a valid email. Email cannot be left empty", e.getMessage());
    }
  }

  /**
   * Test the method that creates a new regular user with an empty first name
   *
   * @author Sasha Denouvilliez-Pech
   */
  @Test
  public void testCreateRegularUserWithEmptyFirstname() {
    // Create the regular user
    AppUser appUser = null;
    try {
      appUser = appUserService.registerRegularUser(VALID_REGULARUSER_EMAIL_ONE, VALID_PASSWORD, "",
          VALID_REGULARUSER_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("Please enter a valid first name. First name cannot be left empty",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new regular user with an empty last name
   *
   * @author Sasha Denouvilliez-Pech
   */
  @Test
  public void testCreateRegularUserWithEmptyLastname() {
    // Create the regular user
    AppUser appUser = null;
    try {
      appUser = appUserService.registerRegularUser(VALID_REGULARUSER_EMAIL_ONE, VALID_PASSWORD,
          VALID_REGULARUSER_LASTNAME_ONE, "");
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("Please enter a valid last name. Last name cannot be left empty",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new regular user with an empty password
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateRegularUserWithEmptyPassword() {
    // Create the regular user
    AppUser appUser = null;
    try {
      appUser = appUserService.registerRegularUser(VALID_REGULARUSER_EMAIL_ONE, "",
          VALID_REGULARUSER_FIRSTNAME_ONE, VALID_REGULARUSER_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("Please enter a valid password. Password cannot be left empty", e.getMessage());
    }
  }

  /**
   * Test the method that creates a new regular user with an invalid email
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateRegularUserWithInvalidEmail() {
    // Create the regular user
    AppUser appUser = null;
    try {
      appUser = appUserService.registerRegularUser(INVALID_EMAIL, VALID_PASSWORD,
          VALID_REGULARUSER_FIRSTNAME_ONE, VALID_REGULARUSER_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("Please enter a valid email. The email address you entered is not valid",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new regular user with an invalid password too short
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateRegularUserWithInvalidPasswordTooShort() {
    // Create the regular user
    AppUser appUser = null;
    try {
      appUser = appUserService.registerRegularUser(VALID_REGULARUSER_EMAIL_ONE,
          INVALID_PASSWORD_ONE,
          VALID_REGULARUSER_FIRSTNAME_ONE, VALID_REGULARUSER_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals(
          "Please enter a valid password. Passwords must be at least 8 characters long and contain at least one number, one lowercase character and one uppercase character",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new regular user with an invalid password no number
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateRegularUserWithInvalidPasswordNoNumber() {
    // Create the regular user
    AppUser appUser = null;
    try {
      appUser = appUserService.registerRegularUser(VALID_REGULARUSER_EMAIL_ONE,
          INVALID_PASSWORD_TWO,
          VALID_REGULARUSER_FIRSTNAME_ONE, VALID_REGULARUSER_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals(
          "Please enter a valid password. Passwords must be at least 8 characters long and contain at least one number, one lowercase character and one uppercase character",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new regular user with an invalid password no uppercase
   * character
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateRegularUserWithInvalidPasswordNoUppercase() {
    // Create the regular user
    AppUser appUser = null;
    try {
      appUser = appUserService.registerRegularUser(VALID_REGULARUSER_EMAIL_ONE,
          INVALID_PASSWORD_THREE,
          VALID_REGULARUSER_FIRSTNAME_ONE, VALID_REGULARUSER_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals(
          "Please enter a valid password. Passwords must be at least 8 characters long and contain at least one number, one lowercase character and one uppercase character",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new regular user with an invalid password no lowercase
   * character
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateRegularUserWithInvalidPasswordNoLowercase() {
    // Create the regular user
    AppUser appUser = null;
    try {
      appUser = appUserService.registerRegularUser(VALID_REGULARUSER_EMAIL_ONE,
          INVALID_PASSWORD_FOUR,
          VALID_REGULARUSER_FIRSTNAME_ONE, VALID_REGULARUSER_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals(
          "Please enter a valid password. Passwords must be at least 8 characters long and contain at least one number, one lowercase character and one uppercase character",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new regular user with an invalid email that already exists
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateRegularUserWithInvalidEmailAlreadyExists() {
    // Create the regular user
    AppUser appUser = null;
    try {
      appUser = appUserService.registerRegularUser(VALID_REGULARUSER_EMAIL_TWO, VALID_PASSWORD,
          VALID_REGULARUSER_FIRSTNAME_ONE, VALID_REGULARUSER_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("An account with this email address already exists", e.getMessage());
    }
  }

  /**
   * Test the method that creates a new moderator
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateModerator() {
    // Create the moderator
    AppUser appUser = null;
    try {
      appUser = appUserService.registerModerator(VALID_MODERATOR_EMAIL_ONE,
          VALID_PASSWORD, VALID_MODERATOR_FIRSTNAME_ONE, VALID_MODERATOR_LASTNAME_ONE);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    // Check the moderator
    assertNotNull(appUser);
    assertEquals(VALID_MODERATOR_EMAIL_ONE, appUser.getEmail());
    assertEquals(VALID_MODERATOR_FIRSTNAME_ONE, appUser.getFirstname());
    assertEquals(VALID_MODERATOR_LASTNAME_ONE, appUser.getLastname());
    assertEquals(VALID_PASSWORD_ENCODED, appUser.getPassword());
  }

  /**
   * Test the method that creates a new moderator with an empty email
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateModeratorWithEmptyEmail() {
    // Create the moderator
    AppUser appUser = null;
    try {
      appUser = appUserService.registerModerator("", VALID_PASSWORD, VALID_MODERATOR_FIRSTNAME_ONE,
          VALID_MODERATOR_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("Please enter a valid email. Email cannot be left empty", e.getMessage());
    }
  }

  /**
   * Test the method that creates a new moderator with an empty first name
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateModeratorWithEmptyFirstname() {
    // Create the moderator
    AppUser appUser = null;
    try {
      appUser = appUserService.registerModerator(VALID_MODERATOR_EMAIL_ONE, VALID_PASSWORD, "",
          VALID_MODERATOR_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("Please enter a valid first name. First name cannot be left empty",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new moderator with an empty last name
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateModeratorWithEmptyLastname() {
    // Create the moderator
    AppUser appUser = null;
    try {
      appUser = appUserService.registerModerator(VALID_MODERATOR_EMAIL_ONE, VALID_PASSWORD,
          VALID_MODERATOR_LASTNAME_ONE, "");
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("Please enter a valid last name. Last name cannot be left empty",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new moderator with an empty password
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateModeratorWithEmptyPassword() {
    // Create the moderator
    AppUser appUser = null;
    try {
      appUser = appUserService.registerModerator(VALID_MODERATOR_EMAIL_ONE, "",
          VALID_MODERATOR_FIRSTNAME_ONE, VALID_MODERATOR_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("Please enter a valid password. Password cannot be left empty", e.getMessage());
    }
  }

  /**
   * Test the method that creates a new moderator with an invalid email
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateModeratorWithInvalidEmail() {
    // Create the moderator
    AppUser appUser = null;
    try {
      appUser = appUserService.registerModerator(INVALID_EMAIL, VALID_PASSWORD,
          VALID_MODERATOR_FIRSTNAME_ONE, VALID_MODERATOR_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("Please enter a valid email. The email address you entered is not valid",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new moderator with an invalid password too short
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateModeratorWithInvalidPasswordTooShort() {
    // Create the moderator
    AppUser appUser = null;
    try {
      appUser = appUserService.registerModerator(VALID_MODERATOR_EMAIL_ONE,
          INVALID_PASSWORD_ONE, VALID_MODERATOR_FIRSTNAME_ONE, VALID_MODERATOR_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals(
          "Please enter a valid password. Passwords must be at least 8 characters long and contain at least one number, one lowercase character and one uppercase character",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new moderator with an invalid password no number
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateModeratorWithInvalidPasswordNoNumber() {
    // Create the moderator
    AppUser appUser = null;
    try {
      appUser = appUserService.registerModerator(VALID_MODERATOR_EMAIL_ONE, INVALID_PASSWORD_TWO,
          VALID_MODERATOR_FIRSTNAME_ONE, VALID_MODERATOR_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals(
          "Please enter a valid password. Passwords must be at least 8 characters long and contain at least one number, one lowercase character and one uppercase character",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new moderator with an invalid password no uppercase character
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateModeratorWithInvalidPasswordNoUppercase() {
    // Create the moderator
    AppUser appUser = null;
    try {
      appUser = appUserService.registerModerator(VALID_MODERATOR_EMAIL_ONE, INVALID_PASSWORD_THREE,
          VALID_MODERATOR_FIRSTNAME_ONE, VALID_MODERATOR_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals(
          "Please enter a valid password. Passwords must be at least 8 characters long and contain at least one number, one lowercase character and one uppercase character",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new moderator with an invalid password no lowercase character
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateModeratorWithInvalidPasswordNoLowercase() {
    // Create the moderator
    AppUser appUser = null;
    try {
      appUser = appUserService.registerModerator(VALID_MODERATOR_EMAIL_ONE, INVALID_PASSWORD_FOUR,
          VALID_MODERATOR_FIRSTNAME_ONE, VALID_MODERATOR_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals(
          "Please enter a valid password. Passwords must be at least 8 characters long and contain at least one number, one lowercase character and one uppercase character",
          e.getMessage());
    }
  }

  /**
   * Test the method that creates a new moderator with an invalid email that already exists
   *
   * @author Siger Ma
   */
  @Test
  public void testCreateModeratorWithInvalidEmailAlreadyExists() {
    // Create the moderator
    AppUser appUser = null;
    try {
      appUser = appUserService.registerModerator(VALID_MODERATOR_EMAIL_TWO, VALID_PASSWORD,
          VALID_MODERATOR_FIRSTNAME_ONE, VALID_MODERATOR_LASTNAME_ONE);
    } catch (Exception e) {
      assertNull(appUser);
      assertEquals("An account with this email address already exists", e.getMessage());
    }
  }

}
