package pl.kopacz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kopacz.config.JHipsterProperties;
import pl.kopacz.domain.Authority;
import pl.kopacz.domain.User;
import pl.kopacz.exception.EmailAlreadyExistsException;
import pl.kopacz.exception.EmailNotRegisteredException;
import pl.kopacz.exception.LoginAlreadyExistsException;
import pl.kopacz.repository.AuthorityRepository;
import pl.kopacz.repository.UserRepository;
import pl.kopacz.security.AuthoritiesConstants;
import pl.kopacz.service.util.RandomUtil;
import pl.kopacz.web.rest.vm.ManagedUserVM;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private MailService mailService;

    public void registerAccount(ManagedUserVM managedUserVM) throws LoginAlreadyExistsException, EmailAlreadyExistsException {
        if (checkIfLoginExists(managedUserVM)) {
            throw new LoginAlreadyExistsException();
        } else if (checkIfEmailExists(managedUserVM)) {
            throw new EmailAlreadyExistsException();
        } else {
            User newUser = createUser(managedUserVM);
            sendActivationEmails(newUser);
        }
    }

    public void requestPasswordReset(String mail) throws EmailNotRegisteredException {
        User user = userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .orElseThrow(() -> new EmailNotRegisteredException());

        initResetKey(user);
        sendPasswordResetMail(user);
    }

    private boolean checkIfLoginExists(ManagedUserVM managedUserVM) {
        return userRepository.findOneByLogin(managedUserVM.getLogin()).isPresent();
    }

    private boolean checkIfEmailExists(ManagedUserVM managedUserVM) {
        return userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent();
    }

    private User createUser(ManagedUserVM managedUserVM) {
        User newUser = new User();

        Set<Authority> authorities = new HashSet<>();
        Authority userAuthority = authorityRepository.findOne(AuthoritiesConstants.USER);
        authorities.add(userAuthority);

        String password = managedUserVM.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);
        String activationKey = RandomUtil.generateActivationKey();

        newUser.setLogin(managedUserVM.getLogin());
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(managedUserVM.getEmail());
        newUser.setLangKey(managedUserVM.getLangKey());

        newUser.setActivated(false);
        newUser.setActivationKey(activationKey);
        newUser.setAuthorities(authorities);

        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private void sendActivationEmails(User newUser) {
        String baseUrl = jHipsterProperties.getMail().getBaseUrl();
        Authority adminAuthority = authorityRepository.findOne(AuthoritiesConstants.ADMIN);
        userRepository.findAllByAuthority(adminAuthority).forEach(
            admin -> mailService.sendActivationEmail(admin, newUser, baseUrl)
        );
    }

    private void initResetKey(User user) {
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        userRepository.save(user);
    }

    private void sendPasswordResetMail(User user) {
        String baseUrl = jHipsterProperties.getMail().getBaseUrl();
        mailService.sendPasswordResetMail(user, baseUrl);
    }

}
