package com.coverstar.controller;

import com.coverstar.constant.Constants;
import com.coverstar.dto.*;
import com.coverstar.entity.Account;
import com.coverstar.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody AccountCreateDto accountCreateDto) {
        try {
            Account account = accountService.createMember(accountCreateDto);
            accountCreateDto.setId(account.getId());
            return ResponseEntity.ok(accountCreateDto);
        } catch (Exception e) {
            if (e.getMessage().equals("Email already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
            if (e.getMessage().equals("Username already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCodeAction(@Valid @RequestBody VerifyCodeDto verifyCodeDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            accountService.verifyCode(verifyCodeDto);
            return ResponseEntity.ok(Constants.VALID_VERIFICATION);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.VERIFYING_ERROR);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        try {
            Map<String, String> response = accountService.authenticateUser(loginDto);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            if (e.getMessage().equals("Account is locked")) {
                return ResponseEntity.status(HttpStatus.LOCKED).body("Account is locked");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.VERIFYING_ERROR);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        try {
            if (!changePasswordDto.isPasswordsMatch()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password and confirm password do not match");
            }

            boolean isPasswordCorrect = accountService.checkPassword(changePasswordDto.getUsernameOrEmail(), changePasswordDto.getOldPassword());
            if (!isPasswordCorrect) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
            }

            accountService.changePassword(changePasswordDto.getUsernameOrEmail(), changePasswordDto.getNewPassword());
            return ResponseEntity.ok("Please verify");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing password");
        }
    }

    @PostMapping("/forgot-password/{usernameOrEmail}")
    public ResponseEntity<?> forgotPassword(@PathVariable String usernameOrEmail) {
        try {
            if (StringUtils.isBlank(usernameOrEmail)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usernameOrEmail is required");
            }

            accountService.forgotPassword(usernameOrEmail);
            return ResponseEntity.ok("Please check your usernameOrEmail");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
        }
    }

    @GetMapping("/user")
    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return oAuth2User.getAttributes();
    }

    @PostMapping("/unlock-account/{usernameOrEmail}")
    public ResponseEntity<?> unlockAccount(@PathVariable String usernameOrEmail) {
        try {
            if (StringUtils.isBlank(usernameOrEmail)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usernameOrEmail is required");
            }

            accountService.unlockAccount(usernameOrEmail);
            return ResponseEntity.ok("Account unlocked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error unlocking account");
        }
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        try {
            Account account = accountService.findById(id).orElseThrow(() -> new Exception("Account not found"));
            ModelMapper modelMapper = new ModelMapper();
            AccountUpdateDto accountCreateDto = modelMapper.map(account, AccountUpdateDto.class);
            return ResponseEntity.ok(accountCreateDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
    }

    @GetMapping("/admin/getAll")
    public ResponseEntity<?> getAllAccount() {
        try {
            List<Account> accounts = accountService.getAllAccount();
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting all account");
        }
    }
}
