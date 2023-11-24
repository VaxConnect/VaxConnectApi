package com.salesianos.triana.VaxConnectApi.security.jwt;

import com.salesianos.triana.VaxConnectApi.security.errorhandling.JwtTokenException;
import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.Sanitary;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import com.salesianos.triana.VaxConnectApi.user.service.SanitaryService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
@Log
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final PatientService patientService;

    @Autowired
    private final SanitaryService sanitaryService;

    @Autowired
    private final JwtProvider jwtProvider;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getJwtTokenFromRequest(request);

        try {
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                UUID userId = jwtProvider.getUserIdFromJwtToken(token);

                Optional<Patient> result = patientService.findById(userId);


                if (result.isPresent()) {
                    Patient patient = result.get();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    patient,
                                    null,
                                    patient.getAuthorities()
                            );

                    authentication.setDetails(new WebAuthenticationDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else{
                    Optional<Sanitary> resultSanitary = sanitaryService.findById(userId);

                    if(resultSanitary.isPresent()){

                    Sanitary sanitary = resultSanitary.get();
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    sanitary,
                                    null,
                                    sanitary.getAuthorities()
                            );

                    authentication.setDetails(new WebAuthenticationDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    }
                }

            }

            filterChain.doFilter(request, response);

        } catch (JwtTokenException ex) {
            log.info("Authentication error using token JWT: " + ex.getMessage());
            resolver.resolveException(request, response, null, ex);
        }
    }


    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtProvider.TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProvider.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtProvider.TOKEN_PREFIX.length());
        }
        return null;
    }
}
