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
            if (e.getMessage().equals(Constants.DUPLICATE_EMAIL)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Constants.DUPLICATE_EMAIL);
            }
            if (e.getMessage().equals(Constants.DUPLICATE_USERNAME)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Constants.DUPLICATE_USERNAME);
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
            if (e.getMessage().equals(Constants.LOCK_ACCOUNT)) {
                return ResponseEntity.status(HttpStatus.LOCKED).body(Constants.LOCK_ACCOUNT);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Constants.INVALID_USERNAME);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.VERIFYING_ERROR);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        try {
            if (!changePasswordDto.isPasswordsMatch()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.INVALID_PASSWORD);
            }

            boolean isPasswordCorrect = accountService.checkPassword(changePasswordDto.getUsernameOrEmail(), changePasswordDto.getOldPassword());
            if (!isPasswordCorrect) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.INVALID_OLD_PASSWORD);
            }

            accountService.changePassword(changePasswordDto.getUsernameOrEmail(), changePasswordDto.getNewPassword());
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR_PASSWORD);
        }
    }

    @PostMapping("/forgot-password/{usernameOrEmail}")
    public ResponseEntity<?> forgotPassword(@PathVariable String usernameOrEmail) {
        try {
            if (StringUtils.isBlank(usernameOrEmail)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.DUPLICATE_USERNAME_EMAIL);
            }

            accountService.forgotPassword(usernameOrEmail);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR_EMAIL);
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.DUPLICATE_USERNAME_EMAIL);
            }

            accountService.unlockAccount(usernameOrEmail);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR_UNLOCK);
        }
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        try {
            Account account = accountService.findById(id).orElseThrow(() -> new Exception(Constants.ACCOUNT_NOTFOUND));
            ModelMapper modelMapper = new ModelMapper();
            AccountUpdateDto accountCreateDto = modelMapper.map(account, AccountUpdateDto.class);
            return ResponseEntity.ok(accountCreateDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ACCOUNT_NOTFOUND);
        }
    }

    @GetMapping("/admin/getAll")
    public ResponseEntity<?> getAllAccount() {
        try {
            List<Account> accounts = accountService.getAllAccount();
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR_GET_ALL_ACCOUNT);
        }
    }

    @PostMapping("/admin/lock-account/{usernameOrEmail}")
    public ResponseEntity<?> lockAccount(@PathVariable String usernameOrEmail) {
        try {
            if (StringUtils.isBlank(usernameOrEmail)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.DUPLICATE_USERNAME_EMAIL);
            }

            accountService.lockAccount(usernameOrEmail);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR_LOCK);
        }
    }
}
