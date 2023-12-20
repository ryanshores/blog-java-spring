package com.ryanshores.blog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var optionalAccount = accountService.findByEmail(email);

        if (optionalAccount.isEmpty()) {
            throw new UsernameNotFoundException("Account not found");
        }

        var account = optionalAccount.get();

        var grantedAuthorities = account.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .toList();

        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), grantedAuthorities);
    }
}
