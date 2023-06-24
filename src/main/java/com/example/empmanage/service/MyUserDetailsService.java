package com.example.empmanage.service;

import com.example.empmanage.data.MyUserDetails;
import com.example.empmanage.data.MyUsersDTO;
import com.example.empmanage.repo.MyUserRepo;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private MyUserRepo repo;
    @PostConstruct
    public void inti(){
        //This method would have been better since it also admin registration ability but better
        //It can also be used to initialize roles
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUsersDTO user = repo.findByName(username);
        if(user==null){
            throw new UsernameNotFoundException(username);
        }
        return new MyUserDetails(user);
    }
}
