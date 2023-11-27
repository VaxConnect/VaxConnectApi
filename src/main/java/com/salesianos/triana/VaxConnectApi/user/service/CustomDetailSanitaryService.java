package com.salesianos.triana.VaxConnectApi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("sanitaryDetailService")
@RequiredArgsConstructor
public class CustomDetailSanitaryService implements UserDetailsService {
    private final SanitaryService sanitaryService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return sanitaryService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with this email"+email));
    }
}
