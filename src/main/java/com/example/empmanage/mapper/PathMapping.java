package com.example.empmanage.mapper;

import com.example.empmanage.data.EmpData;
import com.example.empmanage.data.EmpDataDTO;
import com.example.empmanage.data.MyUsers;
import com.example.empmanage.data.MyUsersDTO;
import com.example.empmanage.repo.EmpDataRepo;
import com.example.empmanage.repo.MyUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PathMapping {
    @Autowired
    private EmpDataRepo repo;
    @Autowired
    private MyUserRepo userRepo;
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
    public String modData(@PathVariable("id") int id,Model model){
        //Add the update operation in here
        EmpDataDTO data = repo.getReferenceById(id);
        model.addAttribute("empdata",data);
        return "modify";
    }
    @PostMapping("/admin_panel/mod/{id}")
    public String setModData(@PathVariable("id") int id,@ModelAttribute("empdata") EmpDataDTO data){
        //Do something that will change the database
        /*  System.out.println("[*]Name : "+data.getName());
        *   System.out.println("[*]Age : "+data.getAge());
        *   System.out.println("[*]Phone number : "+data.getPhoneNumber());
        *   System.out.println("[*]Salary : "+data.getSalary());
        *   System.out.println("[*]Testing complete");
        */
        repo.save(data);
        return "redirect:/admin_panel";
    }
    @GetMapping("/register")
    public String getRegister(Model model){
        /*
        * User registration form should be a bit more long
        * The main aim is to create a page which will save the emp data and user data at the same time
        * The user default would have user as role and there will be an auto generated admin at the start of the program
        * This would allow us to not only have admin as the register but also user as register of their valued information
        * However, the salary should be autogenerated and admin should do all of what should be associated with raises
        * */
        MyUsers user = new MyUsers();
        model.addAttribute("usrdata",user);
        return "register";
    }
    @PostMapping("/register")
    public String register(@ModelAttribute("usrdata") MyUsers user){
        /*
        * System.out.println("[*]Username : "+user.getUsername());
        * System.out.println("[*]Password : "+user.getPassword());
        */
        MyUsersDTO userDTO = new MyUsersDTO();
        userDTO.setName(user.getUsername());
        userDTO.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepo.save(userDTO);
        return "redirect:/success";
    }
    @GetMapping("/success")
    public String getLogin(){
        return "success";
    }
}
