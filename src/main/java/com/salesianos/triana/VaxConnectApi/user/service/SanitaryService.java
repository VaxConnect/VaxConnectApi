package com.salesianos.triana.VaxConnectApi.user.service;

import com.salesianos.triana.VaxConnectApi.user.dto.CreateUserRequest;
import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import com.salesianos.triana.VaxConnectApi.user.modal.UserRole;
import com.salesianos.triana.VaxConnectApi.user.repo.SanitaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SanitaryService {
    private final PasswordEncoder passwordEncoder;
    private final SanitaryRepository sanitaryRepository;

    public Sanitary createSanitary (CreateUserRequest createUserRequest, EnumSet<UserRole>roles){
    if (sanitaryRepository.existsByEmailIgnoreCase(createUserRequest.email())){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The email has benn registred");
    }
    Sanitary sanitary = Sanitary.builder()
            .email(createUserRequest.email())
            .name(createUserRequest.name())
            .lastName(createUserRequest.lastName())
            .createdAt(LocalDateTime.now())
            .birthDate(createUserRequest.birthDate())
            .password(passwordEncoder.encode(createUserRequest.password()))
            .roles(roles)
            .build();
    return sanitaryRepository.save(sanitary);


    }
    public List<Sanitary> findAll(){
        return sanitaryRepository.findAll();
    }
    public Sanitary createSanitaryWithRole(CreateUserRequest createUserRequest){
        return createSanitary(createUserRequest,EnumSet.of(UserRole.SANITARY));
    }

    public Optional<Sanitary> findByEmail(String email){
        return sanitaryRepository.findFirstByEmail(email);
    }
}
