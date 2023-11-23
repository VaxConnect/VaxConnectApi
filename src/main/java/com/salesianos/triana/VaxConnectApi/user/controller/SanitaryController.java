package com.salesianos.triana.VaxConnectApi.user.controller;

import com.salesianos.triana.VaxConnectApi.security.jwt.JwtProvider;
import com.salesianos.triana.VaxConnectApi.user.dto.*;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import com.salesianos.triana.VaxConnectApi.user.service.SanitaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.CacheRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class SanitaryController {


    private final SanitaryService sanitaryService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @PostMapping("/auth/register/sanitary")
    public ResponseEntity<UserResponse> createSanitary(@RequestBody CreateUserRequest createUserRequest){
        Sanitary sanitary = sanitaryService.createSanitaryWithRole(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromSanitary(sanitary));
    }
    @PostMapping("/auth/login/sanitary")
    public ResponseEntity<JwtUserResponse> loginSanitary(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.mail(),
                        loginRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        Sanitary sanitary1 = (Sanitary) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JwtUserResponse.ofSanitary(sanitary1,token));
    }
    @GetMapping("/patients/young")
    public ResponseEntity<List<GetListYoungestPatients>> listYoungestPatients(@AuthenticationPrincipal Sanitary sanitary){
    List<GetListYoungestPatients> youngest = sanitaryService.listYoungestPatients();
    return ResponseEntity.ok(youngest);
    }

}
