package com.coverstar.service.Impl;

import com.coverstar.component.mail.Mail;
import com.coverstar.component.mail.MailService;
import com.coverstar.constant.RandomUtil;
import com.coverstar.dao.account.AccountDao;
import com.coverstar.dao.verify_account.VerifyAccountDao;
import com.coverstar.dto.*;
import com.coverstar.entity.Account;
import com.coverstar.entity.Role;
import com.coverstar.entity.VerifyAccount;
import com.coverstar.repository.AccountRepository;
import com.coverstar.service.AccountService;
import com.coverstar.service.RoleService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MailService mailService;

    @Autowired
    private VerifyAccountDao verifyAccountDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Map<String, String> authenticateUser(LoginDto loginDto) {
        try {

            Account account = getEmailOrUser(loginDto.getUsernameOrEmail());

            if (account.isLocked()) {
                throw new BadCredentialsException("Account is locked");
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsernameOrEmail(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            String role = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
            SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            String token = Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                    .claim("role", role)
                    .signWith(SignatureAlgorithm.HS256, KEY).compact();
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", userDetails.getUsername());
            response.put("role", role);
            response.put("firstName", userDetails.getFirstName());
            response.put("lastName", userDetails.getLastName());
            return response;
        } catch (Exception e) {
            try {
                Account account = getEmailOrUser(loginDto.getUsernameOrEmail());
                if (account.getCountLock() == null) {
                    account.setCountLock(0);
                }
                int newCountLock = account.getCountLock() + 1;

                if (newCountLock >= 5) {
                    account.setLocked(true);
                }
                account.setCountLock(newCountLock);
                accountDao.update(account);
            } catch (Exception exception) {
                exception.fillInStackTrace();
            }
            throw e;
        }
    }

    @Override
    public Account createMember(AccountCreateDto accountDto) throws Exception {

        String email = accountDto.getEmail();
        String username = accountDto.getUsername();
        String password = accountDto.getPassword();
        String firstName = accountDto.getFirstName();
        String lastName = accountDto.getLastName();

        if (accountDao.findByEmail(email).isPresent()) {
            throw new Exception("Email already exists");
        }

        if (accountDao.findByUsername(username).isPresent()) {
            throw new Exception("Username already exists");
        }

        Account account = new Account();
        account.setEmail(email);
        account.setUsername(username);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setPassword(passwordEncoder.encode(password));
        account.setActive(false);
        account.setLocked(false);
        account.setCreatedDate(new Date());
        account.setUpdatedDate(new Date());

        if (roleService.findById(2l).isPresent()) {
            Role role = roleService.findById(2l).get();
            account.addRole(role);
        }
        sendEmail(account);
        return accountDao.create(account);
    }

    @Override
    public Account createAdmin(AccountCreateDto accountDto) {
        String email = accountDto.getEmail();
        String username = accountDto.getUsername();
        String password = accountDto.getPassword();

        Account account = new Account();
        account.setEmail(email);
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));

        if (roleService.findById(2l).isPresent()) {
            Role role = roleService.findById(2l).get();
            account.addRole(role);
        }

        return accountDao.create(account);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountDao.findByEmail(email);
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        return accountDao.findByUsername(username);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountDao.findById(id);
    }

    public void verifyCode(VerifyCodeDto verifyCodeDto) {

        String token = verifyCodeDto.getToken();
        VerifyAccount verifyAccount = verifyAccountDao.findByToken(token).get();
        Account account = verifyAccount.getAccount();
        account.setActive(true);
        account.setCountLock(0);
        account.setLocked(false);
        accountDao.update(account);
    }

    public boolean checkPassword(String userNameOrEmail, String oldPassword) {
        try {
            Account account = getEmailOrUser(userNameOrEmail);
            return passwordEncoder.matches(oldPassword, account.getPassword());
        } catch (Exception e) {
            e.fillInStackTrace();
            return false;
        }
    }

    public void changePassword(String userNameOrEmail, String newPassword) {
        Account account = getEmailOrUser(userNameOrEmail);
        account.setPassword(passwordEncoder.encode(newPassword));
        account.setActive(false);
        sendEmail(account);
        accountDao.update(account);
    }

    private void sendEmail(Account account) {
        try {
            String token = RandomUtil.generateRandomStringNumber(6).toUpperCase();

            VerifyAccount verifyAccount = new VerifyAccount();
            verifyAccount.setAccount(account);
            verifyAccount.setCreatedDate(LocalDateTime.now());
            verifyAccount.setExpiredDataToken(5);
            verifyAccount.setToken(token);
            verifyAccountDao.create(verifyAccount);

            Map<String, Object> maps = new HashMap<>();
            maps.put("account", account);
            maps.put("token", token);

            Mail mail = new Mail();
            mail.setFrom("postmaster@mg.iteacode.com");
            mail.setSubject("Registration");
            mail.setTo(account.getEmail());
            mail.setModel(maps);
            mailService.sendEmail(mail);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void forgotPassword(String usernameOrEmail) {
        try {
            Account account = getEmailOrUser(usernameOrEmail);
            String password = generateRandomPassword();
            account.setPassword(passwordEncoder.encode(password));
            accountDao.update(account);
            VerifyAccount verifyAccount = new VerifyAccount();
            verifyAccount.setAccount(account);
            verifyAccount.setCreatedDate(LocalDateTime.now());
            verifyAccount.setExpiredDataToken(5);
            verifyAccount.setToken(password);
            verifyAccountDao.create(verifyAccount);

            Map<String, Object> maps = new HashMap<>();
            maps.put("account", account);
            maps.put("token", password);

            Mail mail = new Mail();
            mail.setFrom("postmaster@mg.iteacode.com");
            mail.setSubject("Registration");
            mail.setTo(account.getEmail());
            mail.setModel(maps);
            mailService.sendEmail(mail);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private String generateRandomPassword() {
        int length = 12;
        String upperCaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseChars = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_=+<>?";

        String allChars = upperCaseChars + lowerCaseChars + digits + specialChars;
        Random random = new Random();

        StringBuilder password = new StringBuilder();

        password.append(upperCaseChars.charAt(random.nextInt(upperCaseChars.length())));
        password.append(lowerCaseChars.charAt(random.nextInt(lowerCaseChars.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        for (int i = password.length(); i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        Collections.shuffle(Arrays.asList(password.toString().split("")));

        return password.toString();
    }

    private Account getEmailOrUser(String usernameOrEmail) {
        try {
            EmailOrUser emailOrUser = new EmailOrUser();
            if (usernameOrEmail.contains("@")) {
                emailOrUser.setEmail(usernameOrEmail);
            } else {
                emailOrUser.setUsername(usernameOrEmail);
            }
            Account account = accountDao.findByUsernameOrEmail(emailOrUser.getUsername(),
                    emailOrUser.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
            return account;
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public void unlockAccount(String usernameOrEmail) {
        try {
            Account account = getEmailOrUser(usernameOrEmail);
            account.setActive(false);
            sendEmail(account);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }
}