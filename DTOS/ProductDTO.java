package com.project.shopapp.DTOS;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotEmpty(message = "The Product can not be Empty")
    @NotBlank(message = "Title is required")
    @Size(min=3,max = 200,message = "Title must be between 3 and 200 characters")
    private String productName;
    @Min(value= 0,message = "Price must be greater than or equal to 0")
    @Max(value= 10000000,message = "Price must less than or equal to 10,000,000")
    private Float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private String category;

}