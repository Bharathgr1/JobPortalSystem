package com.example.elevateJobPortal.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        boolean isEmployer = false;
        boolean isApplicant = false;

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (auth.getAuthority().equals("ROLE_EMPLOYER")) {
                isEmployer = true;
                break;
            } else if (auth.getAuthority().equals("ROLE_APPLICANT")) {
                isApplicant = true;
                break;
            }
        }

        if (isEmployer) {
            response.sendRedirect("/employer/jobs");
        } else if (isApplicant) {
            System.out.println("Hello23");
            response.sendRedirect("/applicant/my_apps");
        } else {
            response.sendRedirect("/index");
        }
    }
}
