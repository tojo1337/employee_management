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
        EmpDataDTO dataDTO = new EmpDataDTO();
        dataDTO.setName(data.getName());
        dataDTO.setAge(data.getAge());
        dataDTO.setPhoneNumber(data.getPhoneNumber());
        dataDTO.setSalary(data.getSalary());
        repo.save(dataDTO);
        return "redirect:/admin_panel";
    }
    @GetMapping("/admin_panel/del/{id}")
    public String getDelId(@PathVariable("id") int id){
        repo.deleteById(id);
        return "redirect:/admin_panel";
    }
    @GetMapping("/admin_panel/mod/{id}")
    public String modData(@PathVariable("id") int id){
        //Add the update operation in here
        EmpDataDTO data = repo.getReferenceById(id);
        System.out.println("[*]Name : "+data.getName());
        System.out.println("[*]Phone number : "+data.getPhoneNumber());
        return "redirect:/admin_panel";
    }
    @PostMapping("/admin_panel/mod/{id}")
    public String setModData(@PathVariable("id") int id){
        //Do something that will change the database
        return "redirect:/admin_panel";
    }
}
