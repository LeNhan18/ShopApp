package com.project.shopapp.MODELS;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "image_url",length = 300)
    private String imageUrl;

}
