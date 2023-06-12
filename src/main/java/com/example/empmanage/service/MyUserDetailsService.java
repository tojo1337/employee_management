package com.example.empmanage.service;

import com.example.empmanage.data.MyUserDetails;
import com.example.empmanage.data.MyUsersDTO;
import com.example.empmanage.repo.MyUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private MyUserRepo repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUsersDTO user = repo.findByName(username);
        if(user==null){
            throw new UsernameNotFoundException(username);
        }
        return new MyUserDetails(user);
    }
}
