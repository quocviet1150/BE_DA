package com.coverstar.config.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2Account implements OAuth2User {

    private OAuth2User oauth2User;

    public CustomOAuth2Account(OAuth2User oAuth2User) {
        this.oauth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getFullName(){
        return oauth2User.getAttribute("name");
    }

    public String getEmail(){
        return oauth2User.getAttribute("email");
    }

    public String getPassword(){
        return oauth2User.getAttribute("password");
    }
}
