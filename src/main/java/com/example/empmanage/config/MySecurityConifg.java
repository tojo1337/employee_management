package com.example.empmanage.config;

import com.example.empmanage.data.MyUserDetails;
import com.example.empmanage.service.MyUserDetailsService;
import com.sun.jna.platform.win32.Netapi32Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class MySecurityConifg {
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(httpSecurityCsrfConfigurer -> {}).
                authorizeHttpRequests(requests->requests.
                        requestMatchers("/","/register","/success","/static/**").permitAll().
                        requestMatchers("/admin_panel**").hasAuthority("admin").
                        requestMatchers("/user**").hasAuthority("user").
                        anyRequest().authenticated()).
                formLogin(login->login.permitAll().successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
                        int userId = userDetails.getUserId();
                        int redirectVal = (userId-1);
                        /*  -1 because there is already the admin in the user database but user database and employee database are not the same
                         *  Employee database starts from 1 and the employees are mapped according to their id order by using -1 from user database
                         *  It was the best way for mapping
                         */
                        System.out.println("[*]The user id : "+userId);
                        String role = String.valueOf(userDetails.getAuthorities().stream().iterator().next());
                        System.out.println("[*]\tName : "+userDetails.getUsername()+"\tRole:"+role);
                        if(role.equals("user")){
                            System.out.println("[*]User authority detected");
                            response.sendRedirect("/user/"+redirectVal);
                        }else if(role.equals("admin")){
                            System.out.println("[*]Admin authority detected");
                            response.sendRedirect("/admin_panel");
                        }else {
                            System.out.println("[*]Unable to understand logged in user authority");
                            response.sendRedirect("/");
                        }
                        // response.sendRedirect("/admin_panel");
                    }
                })).
                httpBasic(httpSecurityHttpBasicConfigurer->{}).build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}