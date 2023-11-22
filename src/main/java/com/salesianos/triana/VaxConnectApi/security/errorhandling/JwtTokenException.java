package com.salesianos.triana.VaxConnectApi.security.errorhandling;

public class JwtTokenException extends RuntimeException{

    public JwtTokenException(String msg){
        super(msg);
    }

}
