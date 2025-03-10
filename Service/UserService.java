package com.project.shopapp.Service;

import com.project.shopapp.DTOS.UserDTO;
import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.MODELS.Role;
import com.project.shopapp.MODELS.User;
import com.project.shopapp.Respository.RoleRespository;
import com.project.shopapp.Respository.UserRespository;
import com.project.shopapp.Service.IMP.IMPUserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service //Xác định lớp này la lớp dịch vuj trong spring
@RequiredArgsConstructor // Auto create các Contructor với các final field
public class UserService implements IMPUserService { //Lớp trien khai interface IMPUserService
    private final RoleRespository roleRespository; //Kho lưu truu role duoc tiem qua constructor
    private UserRespository userRespository; //
    @SneakyThrows // ẩn việc xử ly ngoại lệ auto throw neeus có
    @Override
    public User createUser(UserDTO userDTO) {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRespository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        User newUser = User.builder()
                .fullname(userDTO.getFullname())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(new java.sql.Date(userDTO.getDateOfBirth().getTime()))
                .facebookAccount(userDTO.getFacebookAccountId())
                .googleAccount(userDTO.getGoogleAccountId())
                .build();
        Role role = roleRespository.findById(userDTO.getRoleId())
                .orElseThrow(()->new DataNotFoundException("Role not found"));
        newUser.setRole(role);
        if(userDTO.getFacebookAccountId() ==0 && userDTO.getGoogleAccountId() ==0){
            String password = userDTO.getPassword();
        }
        return userRespository.save(newUser);
    }

    @Override
    public User Login(String username, String password) {
        return null;
    }
}
