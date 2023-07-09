package com.example.empmanage.mapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import com.example.empmanage.data.EmpData;
import com.example.empmanage.data.EmpDataDTO;
import com.example.empmanage.data.MyUsers;
import com.example.empmanage.data.MyUsersDTO;
import com.example.empmanage.repo.EmpDataRepo;
import com.example.empmanage.repo.MyUserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
//Making two different table for different functions were the wrong thing in here
//It makes the system more prone to breaking
//But uniting it might create some other issues
//Make the employee registration from the admin side with password
@Controller
public class PathMapping {
    @Autowired
    private EmpDataRepo repo;
    @Autowired
    private MyUserRepo userRepo;
    @GetMapping("/")
    public String index(Principal principal){
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
        MyUsersDTO tempUserDTO = userRepo.findByName(data.getName());
        EmpDataDTO tempEmpDTO = repo.getReferenceByName(data.getName());
        if(tempEmpDTO!=null||tempUserDTO!=null){
            // Complete the admin control
        }else {
            // Complete the admin control in here
        }
        EmpDataDTO dataDTO = new EmpDataDTO();
        dataDTO.setName(data.getName());
        dataDTO.setAge(data.getAge());
        dataDTO.setPhoneNumber(data.getPhoneNumber());
        dataDTO.setSalary(data.getSalary());
        //Add the user as the admin should have the permission to do that
        MyUsersDTO usersDTO = new MyUsersDTO();
        usersDTO.setName(dataDTO.getName());
        usersDTO.setPassword(new BCryptPasswordEncoder().encode(dataDTO.getName()));
        usersDTO.setRole("user");
        repo.save(dataDTO);
        userRepo.save(usersDTO);
        return "redirect:/admin_panel";
    }
    @GetMapping("/admin_panel/del/{id}")
    public String getDelId(@PathVariable("id") int id){
        /*
        * Make it so that not only the employee database entry is removed but also the entry on authentication list is also removed
        * Need to work around something to make it do that
        */
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
    public String register(@Valid @ModelAttribute("usrdata") MyUsers user, BindingResult result,Model model){
        /*
        * System.out.println("[*]Username : "+user.getUsername());
        * System.out.println("[*]Password : "+user.getPassword());
        */
        if(result.hasErrors()){
            return "redirect:/register";
        }else {
            // Add a checking method to check if the username already exists
            // A model attribute would be user to show the message if username exists
            int defSalary = 30000;
            //Saving the credentials for a user
            MyUsersDTO tempUserDTO = userRepo.findByName(user.getUsername());
            EmpDataDTO tempEmpDTO = repo.getReferenceByName(user.getUsername());
            if(tempEmpDTO!=null||tempUserDTO!=null){
                // Do not save the data if it has a matching username
                String outputData = "Username already exists : "+user.getUsername();
                model.addAttribute("exists",outputData);
                return "register";
            }else {
                // Save the data only if it has unique username
                MyUsersDTO userDTO = new MyUsersDTO();
                userDTO.setName(user.getUsername());
                userDTO.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                userDTO.setRole("user");
                //Saving as employee
                EmpDataDTO empData = new EmpDataDTO();
                empData.setName(user.getUsername());
                empData.setAge(user.getAge());
                empData.setPhoneNumber(user.getPhoneNumber());
                empData.setSalary(defSalary);
                repo.save(empData);
                userRepo.save(userDTO);
                return "redirect:/success";
            }
        }
    }
    @GetMapping("/success")
    public String getLogin(){
        return "success";
    }
    @GetMapping("/user/{userid}")
    public String userStatus(@PathVariable("userid")int id,Model model){
        EmpDataDTO empData = repo.getReferenceById(id);
        model.addAttribute("emp",empData);
        return "user_info";
    }
    @GetMapping("/user/board")
    public String userBoard(Model model){
        /*
        * Add a user board which would allow other users to see each other
        * It will also allow other users to search users
        * Try using spring boot to implement search
        * But it might also be possible to do it with javascript/jquery
        */
        List<EmpDataDTO> list = repo.findAll();
        model.addAttribute("emplist",list);
        return "user_list";
    }
    //There is no need to add special session or cookie as it is already done by spring boot
    //Use Principal to access the current session id
    @GetMapping("/user/{userid}/reset")
    public String setPassword(@PathVariable("userid") int id,Model model,Principal principal){
        String name = principal.getName();
        EmpDataDTO empDataDTO = repo.getReferenceByName(name);
        int uid = empDataDTO.getId();
        if(uid==id){
            model.addAttribute("id",uid);
            return "reset";
        }else {
            return "redirect:/user/"+uid+"reset";
        }
    }
    @PostMapping("/user/{userid}/reset")
    public String resetPassword(@PathVariable("userid") int id, Model model,Principal principal){
        String name = principal.getName();
        EmpDataDTO empDataDTO = repo.getReferenceByName(name);
        int uid = empDataDTO.getId();
        if(id==uid){
            model.addAttribute("id",uid);
            return "reset";
        }else {
            return "redirect:/user/"+uid+"reset";
        }
    }
}
