package com.project.shopapp.Controller;

import com.project.shopapp.DTOS.OrderDTO;
import com.project.shopapp.Respones.OrderResponse;
import com.project.shopapp.Respository.OrderRespository;
import com.project.shopapp.Service.IMP.IMPOrderService;
import com.project.shopapp.Service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IMPOrderService orderService;
     @PostMapping("")
    public ResponseEntity<?> createOrder (@Valid @RequestBody OrderDTO orderDTO , BindingResult result) {
         try {
             if (result.hasErrors()) {
                 List<String> errorsMessage = result.getFieldErrors()
                         .stream()
                         .map(FieldError::getDefaultMessage)
                         .toList();
                 return ResponseEntity.badRequest().body(result.getAllErrors());
             }
             OrderResponse orderResponse = orderService.createOrder(orderDTO);
             return ResponseEntity.ok("Order created successfuly");
         }catch (Exception e){
             return ResponseEntity.badRequest().body(e.getMessage());
         }
       }
       @GetMapping("/{userId}")
       public ResponseEntity<?> getOrdersByUserId (@Valid @PathVariable("user_id") Long userId){
           try {
              return ResponseEntity.ok("get list user in user");
           }catch (Exception e){
               return ResponseEntity.badRequest().body(e.getMessage());
           }
       }
      // Cong viec cua admin
       @PutMapping("/{id}")
       public ResponseEntity<?> updateOrder (@Valid @PathVariable("id") Long id, @RequestBody OrderDTO orderDTO){
         return ResponseEntity.ok("update order");
       }
       //Xóa mềm => cập nhật trường active ==false
       @DeleteMapping("/{id}")
       public ResponseEntity<?> deleteOrder (@Valid @PathVariable("id") Long id){
           return ResponseEntity.ok("delete order");
       }
    }
