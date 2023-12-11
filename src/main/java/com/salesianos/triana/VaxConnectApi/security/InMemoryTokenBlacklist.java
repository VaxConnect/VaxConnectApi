package com.salesianos.triana.VaxConnectApi.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Service
public class InMemoryTokenBlacklist implements TokenBlacklist {
    private Set<String> blacklist = new HashSet<>();

    @Override
    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }



}