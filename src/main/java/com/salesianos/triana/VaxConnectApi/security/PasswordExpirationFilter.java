package com.salesianos.triana.VaxConnectApi.security;

import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.service.CustomDetailUserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class PasswordExpirationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (isUrlExcluded(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        System.out.println("PasswordExpirationFilter");

        Patient patient = getLoggedInPatient();

        if (patient != null && patient.isPasswordExpired()) {
            showChangePasswordPage(response, httpRequest, patient);
        } else {
            chain.doFilter(httpRequest, response);
        }

    }

    private boolean isUrlExcluded(HttpServletRequest httpRequest)
            throws IOException, ServletException {
        String url = httpRequest.getRequestURL().toString();

        if (url.endsWith(".css") || url.endsWith(".png") || url.endsWith(".js")
                || url.endsWith("/change_password")) {
            return true;
        }

        return false;
    }

    private Patient getLoggedInPatient() {
        /*Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();
        Object principal = null;

        if (authentication != null) {
            principal = authentication.getPrincipal();
        }

        if (principal != null && principal instanceof CustomDetailUserService) {
            CustomDetailUserService userDetails = (CustomDetailUserService) principal;
            String userEmail = userDetails.getClass().getName();

            return userDetails.loadUserByUsername(); //nose como pasarle a este metodo el email del usuario
        }

        return null;*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Patient) {
            return (Patient) authentication.getPrincipal();
        }

        return null;
    }

    private void showChangePasswordPage(ServletResponse response,
                                        HttpServletRequest httpRequest, Patient patient) throws IOException {
        System.out.println("Customer: " + patient.getName() + " - Password Expired:");
        System.out.println("Last time password changed: " + patient.getPasswordChangedTime());
        System.out.println("Current time: " + new Date());

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String redirectURL = httpRequest.getContextPath() + "/change_password";
        httpResponse.sendRedirect(redirectURL);
    }

}
