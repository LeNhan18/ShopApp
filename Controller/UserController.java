package com.project.shopapp.Controller;


import com.project.shopapp.DTOS.UserDTO;
import com.project.shopapp.DTOS.UserLoginDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping("/register")
    public ResponseEntity<?> register (@Valid @RequestBody UserDTO userDTO
            ,BindingResult result) {
        try{
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                       .map(FieldError:: getDefaultMessage )
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Password does not match");
            }
            return ResponseEntity.ok("Register successfly!");

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody UserLoginDTO userloginDTO){
        //Kiểm tra thông tin đăng nhập và sinh tokens
        //trả token về reponse
        return ResponseEntity.ok("Login successfly!");

    }
//    @GetMapping("")
//    public ResponseEntity<?> GetAllUsers(){
//        List<UserDTO> A = new ArrayList<UserDTO>();
//        for(UserDTO user  : A  ){
//
//        }
//
//    }
}
