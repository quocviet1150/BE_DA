package com.coverstar.controller;

import com.coverstar.constant.Coverstar;
import com.coverstar.dto.AccountCreateDto;
import com.coverstar.dto.CustomUserDetails;
import com.coverstar.dto.LoginDto;
import com.coverstar.dto.VerifyCodeDto;
import com.coverstar.model.Account;
import com.coverstar.service.AccountService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody AccountCreateDto accountCreateDto) {

        try {
            Account account = accountService.createMember(accountCreateDto);
            accountCreateDto.setId(account.getId());
            return ResponseEntity.ok(accountCreateDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Coverstar.ERROR);
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCodeAction(@Valid @RequestBody VerifyCodeDto verifyCodeDto, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        try {
            accountService.verifyCode(verifyCodeDto);
            return ResponseEntity.ok(Coverstar.VALID_VERIFICATION);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Coverstar.VERIFYING_ERROR);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
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

        return ResponseEntity.ok(response);
    }

}
