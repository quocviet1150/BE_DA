package com.coverstar.service.Impl;

import com.coverstar.component.mail.Mail;
import com.coverstar.component.mail.MailService;
import com.coverstar.constant.RandomUtil;
import com.coverstar.dao.account.AccountDao;
import com.coverstar.dao.verify_account.VerifyAccountDao;
import com.coverstar.dto.AccountCreateDto;
import com.coverstar.dto.VerifyCodeDto;
import com.coverstar.entity.Account;
import com.coverstar.entity.Role;
import com.coverstar.entity.VerifyAccount;
import com.coverstar.service.AccountService;
import com.coverstar.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

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


    @Override
    public Account createMember(AccountCreateDto accountDto) throws MessagingException {

        String email = accountDto.getEmail();
        String username = accountDto.getUsername();
        String password = accountDto.getPassword();
        String firstName = accountDto.getFirstName();
        String lastName = accountDto.getLastName();

        Account account = new Account();
        account.setEmail(email);
        account.setUsername(username);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setPassword(passwordEncoder.encode(password));
        account.setActive(false);

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
        accountDao.update(account);
    }

    public boolean checkPassword(String userNameOrEmail, String oldPassword) {
        try {
            String username = StringUtils.EMPTY;
            String email = StringUtils.EMPTY;
            if (userNameOrEmail.contains("@")) {
                email = userNameOrEmail;
            } else {
                username = userNameOrEmail;
            }
            Account account = accountDao.findByUsernameOrEmail(username, email).orElseThrow(() -> new RuntimeException("User not found"));
            return passwordEncoder.matches(oldPassword, account.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void changePassword(String userNameOrEmail, String newPassword) {
        String username = StringUtils.EMPTY;
        String email = StringUtils.EMPTY;
        if (userNameOrEmail.contains("@")) {
            email = userNameOrEmail;
        } else {
            username = userNameOrEmail;
        }
        Account account = accountDao.findByUsernameOrEmail(username, email).orElseThrow(() -> new RuntimeException("User not found"));
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
            e.printStackTrace();
        }
    }

    @Override
    public void forgotPassword(String usernameOrEmail) {
        try {
            String username = StringUtils.EMPTY;
            String email = StringUtils.EMPTY;
            if (usernameOrEmail.contains("@")) {
                email = usernameOrEmail;
            } else {
                username = usernameOrEmail;
            }
            String password = generateRandomPassword();
            Account account = accountDao.findByUsernameOrEmail(username, email).orElseThrow(() -> new RuntimeException("User not found"));
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
            e.printStackTrace();
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

}