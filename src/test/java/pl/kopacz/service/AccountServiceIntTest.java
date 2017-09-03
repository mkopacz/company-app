package pl.kopacz.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import pl.kopacz.CompanyApp;
import pl.kopacz.domain.User;
import pl.kopacz.exception.EmailNotRegisteredException;
import pl.kopacz.exception.ValidResetKeyNotFoundException;
import pl.kopacz.repository.UserRepository;
import pl.kopacz.util.TestDataUtil;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CompanyApp.class)
@Transactional
public class AccountServiceIntTest {

    @Inject
    private AccountService accountService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Mock
    private MailService mockMailService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(mockMailService).sendPasswordResetMail(anyObject(), anyString());
        ReflectionTestUtils.setField(accountService, "mailService", mockMailService);
    }

    @Test
    public void assertThatUserMustExistToResetPassword() {
        try {
            accountService.requestPasswordReset("john.doe@localhost");
            fail("User must exist to reset password");
        } catch (EmailNotRegisteredException e) {
        }

        try {
            accountService.requestPasswordReset("admin@localhost");
        } catch (EmailNotRegisteredException e) {
            fail("Existing user should be able to reset password");
        }
    }

    @Test
    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        User user = userService.createUser(TestDataUtil.generateManagedUserVM());
        user.setActivated(false);

        try {
            accountService.requestPasswordReset("john.doe@localhost");
            fail("User must be active to reset password");
        } catch (EmailNotRegisteredException e) {
        }

        userRepository.delete(user);
    }

    @Test
    public void assertThatResetKeyMustNotBeOlderThan24Hours() {
        User user = userService.createUser(TestDataUtil.generateManagedUserVM());
        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(25);
        user.setResetDate(daysAgo);


        try {
            accountService.completePasswordReset("johndoe2", user.getResetKey());
            fail("Reset key must not be older than 24 hours to reset password");
        } catch (ValidResetKeyNotFoundException e) {
        }

        userRepository.delete(user);
    }

    @Test
    public void assertThatResetKeyMustBeValid() {
        User user = userService.createUser(TestDataUtil.generateManagedUserVM());

        try {
            accountService.completePasswordReset("johndoe2", "1234");
            fail("Reset key must be valid to reset password");
        } catch (ValidResetKeyNotFoundException e) {
        }

        userRepository.delete(user);
    }

    @Test
    public void assertThatUserCanResetPassword() {
        User user = userService.createUser(TestDataUtil.generateManagedUserVM());

        try {
            accountService.completePasswordReset("johndoe2", user.getResetKey());
        } catch (ValidResetKeyNotFoundException e) {
            fail("User should be able to reset password");
        }

        userRepository.delete(user);
    }

}
