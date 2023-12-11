package com.salesianos.triana.VaxConnectApi.user.controller;

import com.salesianos.triana.VaxConnectApi.user.modal.Patient;
import com.salesianos.triana.VaxConnectApi.user.modal.User;
import com.salesianos.triana.VaxConnectApi.user.service.CustomDetailUserService;
import com.salesianos.triana.VaxConnectApi.user.service.PatientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/change_password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("pageTitle", "Change Expired Password");
        return "change_password";
    }

    @PostMapping("/change_password")
    public String processChangePassword(HttpServletRequest request, HttpServletResponse response,
                                        Model model, RedirectAttributes ra,
                                        @AuthenticationPrincipal Authentication authentication) throws ServletException {
        CustomDetailUserService userDetails = (CustomDetailUserService) authentication.getPrincipal();
        Patient patient = userDetails.loadUserByUsername();//nose como pasarle a este metodo el email del usuario

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");

        model.addAttribute("pageTitle", "Change Expired Password");

        if (oldPassword.equals(newPassword)) {
            model.addAttribute("message", "Your new password must be different than the old one.");

            return "change_password";
        }

        if (!passwordEncoder.matches(oldPassword, patient.getPassword())) {
            model.addAttribute("message", "Your old password is incorrect.");
            return "change_password";

        } else {
            patientService.changePassword(patient, newPassword);
            request.logout();
            ra.addFlashAttribute("message", "You have changed your password successfully. "
                    + "Please login again.");

            return "redirect:/login";
        }

    }
}
