package com.example.empmanage.repo;

import com.example.empmanage.data.MyUsersDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRepo extends JpaRepository<MyUsersDTO, Integer> {
    MyUsersDTO findByName(String username);
}
