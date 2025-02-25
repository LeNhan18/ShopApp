package com.project.shopapp.Controller;


import com.project.shopapp.DTOS.ProductDTO;
import com.project.shopapp.DTOS.ProductImageDTO;
import com.project.shopapp.MODELS.Product;
import com.project.shopapp.MODELS.ProductImage;
import com.project.shopapp.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("")
    public ResponseEntity<?> getAllProducts(@RequestParam String limit , @RequestParam int page) {
        return ResponseEntity.ok("limit: "+limit+" page: "+page);
    }
//    @PostMapping( value ="",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?>
@PostMapping("/1")
public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
BindingResult bindingResult) {
    try {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        Product newProduct = productService.CreateProduct(productDTO);

                //Lưu vào đối tượng product trong DB =>làm sau
                //Lưu vào bảng products image


        productService.CreateProduct(productDTO);
        return ResponseEntity.ok("Product insert successfully");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return ResponseEntity.ok("Product insert successfully");
}
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<?> createImage(@PathVariable("id") Long id, @RequestParam("files") List<MultipartFile> files) {
        try {
            Product existingProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size()> ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("You can only Upload maximum 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                if (file != null) {
                    //Kiem tra kich thuoc cua file va dinh dang
                    if (file.getSize() > 10 * 1024 * 1024) {
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                                .body("File is too large Maximum size is 1000mb");
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .body("File must be an image");
                    }
                    String fileName = storeFile(file);
                    //Thay thế hàm này với code của bạn để lưu file
                    ProductImage productImage = productService.createProductImage(existingProduct.getId(), ProductImageDTO.builder()
                            .imageUrl(fileName)
                            .build());
                    productImages.add(productImage);
                    //Lưu vào đối tượng product trong DB =>làm sau
                    //Lưu vào bảng products image
                }

            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private String storeFile(MultipartFile file) throws IOException {
        if(!isImageFile(file) && file.getOriginalFilename()==null){
            throw new IOException("Invalid Image format");

        }

        // Lấy tên file gốc và làm sạch
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Kiểm tra nếu tên file trống
        if (originalFileName.isBlank()) {
            throw new IOException("File name is empty!");
        }

        // Lấy phần mở rộng của file (jpg, png, ...)
        String fileExtension = "";
        int lastDot = originalFileName.lastIndexOf(".");
        if (lastDot != -1) {
            fileExtension = originalFileName.substring(lastDot); // Ví dụ: ".jpg"
        }

        // Tạo tên file mới với UUID + đuôi file
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Đường dẫn đến thư mục Uploads
        Path uploadDir = Paths.get("Uploads");

        // Tạo thư mục nếu chưa tồn tại
        Files.createDirectories(uploadDir);

        // Đường dẫn đầy đủ của file
        Path destination = uploadDir.resolve(uniqueFileName);

        // Lưu file vào thư mục Uploads
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }
    private boolean isImageFile(MultipartFile file)
    {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getIdProduct(@PathVariable("id") String ProductId) {

        return ResponseEntity.ok("id : "+ProductId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeleteProduct(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body("Product Delete successly");
    }
}
