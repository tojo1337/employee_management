package com.example.empmanage.repo;

import com.example.empmanage.data.EmpDataDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpDataRepo extends JpaRepository<EmpDataDTO,Integer> {
    EmpDataDTO getReferenceById(int id);
    EmpDataDTO getReferenceByName(String name);
}
