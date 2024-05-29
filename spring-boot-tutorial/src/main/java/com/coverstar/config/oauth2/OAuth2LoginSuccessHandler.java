package com.coverstar.config.oauth2;

import com.coverstar.component.AuthenticationProvider;
import com.coverstar.model.Account;
import com.coverstar.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private AccountService accountService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2Account oAuth2Account = (CustomOAuth2Account) authentication.getPrincipal();
        String email = oAuth2Account.getEmail();

        Account account = accountService.getAccountByEmail(email);
        String name = oAuth2Account.getName();
        String password = oAuth2Account.getPassword();

        if (account == null){
            accountService.createNewAccountAfterOAuthLoginSuccess(email,name,password, AuthenticationProvider.GOOGLE);
        } else {
            accountService.updateAccountAfterOAuthLoginSuccess(email,name, account,AuthenticationProvider.GOOGLE);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
