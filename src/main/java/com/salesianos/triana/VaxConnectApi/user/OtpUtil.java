package com.salesianos.triana.VaxConnectApi.user;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtil {
    public String generateOpt(){
        Random random = new Random();
        int randomNumber = random.nextInt(99999);
        String output = Integer.toString(randomNumber);
        while (output.length()<6){
            output = "0" + output;
        }
        return output;
    }
}
