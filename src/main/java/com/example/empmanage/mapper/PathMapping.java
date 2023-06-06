package com.example.empmanage.mapper;

import com.example.empmanage.data.EmpData;
import com.example.empmanage.data.EmpDataDTO;
import com.example.empmanage.repo.EmpDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PathMapping {
    @Autowired
    private EmpDataRepo repo;
    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/admin_panel")
    public String adminPanel(Model model){
        //To show the working employee list
        List<EmpDataDTO> list = repo.findAll();
        model.addAttribute("emplist",list);
        //To add a new employee as an admin
        EmpData data = new EmpData();
        model.addAttribute("empdata",data);
        return  "admin_panel";
    }
    @PostMapping("/admin_panel")
    public String addEmpData(Model model, @ModelAttribute("empdata") EmpData data){
        //This will deal with the instance where admin adds new employee
        System.out.println("[*]Name : "+data.getName());
        return "redirect:/admin_panel";
    }
}
