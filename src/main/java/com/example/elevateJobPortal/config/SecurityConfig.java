package com.example.elevateJobPortal.config;

import com.example.elevateJobPortal.entity.User;
import com.example.elevateJobPortal.repository.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {



        private final CustomLoginSuccessHandler loginSuccessHandler;

        // Constructor injection (recommended)
        public SecurityConfig(CustomLoginSuccessHandler loginSuccessHandler) {
            this.loginSuccessHandler = loginSuccessHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/", "/index", "/auth/**", "/css/**", "/js/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .formLogin(form -> form
                            .loginPage("/auth/login")
                            .loginProcessingUrl("/auth/login")
                            .failureUrl("/auth/login?error=true")
                            .successHandler(loginSuccessHandler) // use the handler here
                            .permitAll()
                    )
                    .logout(logout -> logout
                            .logoutSuccessUrl("/index")
                            .permitAll()
                    );

            return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService(UserRepo userRepo) {
            return username -> {
                User user = userRepo.findByUsername(username);
                if (user == null) {
                    throw new UsernameNotFoundException("User not found: " + username);
                }
                return org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().toUpperCase()) // ROLE_EMPLOYER or ROLE_APPLICANT
                        .build();
            };
        }
    }
