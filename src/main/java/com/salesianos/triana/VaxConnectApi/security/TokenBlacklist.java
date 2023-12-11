package com.salesianos.triana.VaxConnectApi.security;

public interface TokenBlacklist {
    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
}
