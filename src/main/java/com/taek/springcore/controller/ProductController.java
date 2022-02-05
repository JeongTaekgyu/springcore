package com.taek.springcore.controller;

import com.taek.springcore.dto.ProductMypriceRequestDto;
import com.taek.springcore.dto.ProductRequestDto;
import com.taek.springcore.model.Product;
import com.taek.springcore.model.UserRoleEnum;
import com.taek.springcore.security.UserDetailsImpl;
import com.taek.springcore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // JSON으로 데이터를 주고받음을 선언합니다.
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 신규 상품 등록
    @PostMapping("/api/products")
    public Product createProduct(@RequestBody ProductRequestDto requestDto,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
// 로그인 되어 있는 회원 테이블의 ID
        Long userId = userDetails.getUser().getId();
        System.out.println("~~~신규 상품 등록 userId : "+userId);
        Product product = productService.createProduct(requestDto, userId);

// 응답 보내기
        return product;
    }

    // 설정 가격 변경
    @PutMapping("/api/products/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {
        Product product = productService.updateProduct(id, requestDto);
        // 어째 업데이트 myprice가 달라서 product 테이블에 똑같은 상품이 중복 가능 하구나

        System.out.println("~~~업데이트된 상품 id : "+product.getId());
// 응답 보내기 (업데이트된 상품 id)
        return product.getId();
    }

    // 로그인한 회원이 등록한 관심 상품 조회
    @GetMapping("/api/products")
    public List<Product> getProducts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인 되어 있는 회원 테이블의 ID
        Long userId = userDetails.getUser().getId();
        System.out.println("~~~userId : "+userId);
        return productService.getProducts(userId);
    }

    // (관리자용) 전체 상품 조회
    /*@Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/api/admin/products")
    public List<Product> getAllProducts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인 되어 있는 회원 테이블의 ID 필요없음
        //Long userId = userDetails.getUser().getId();
        return productService.getAllProducts();
    }*/

    // (관리자용) 등록된 모든 상품 목록 조회
    @Secured(value = UserRoleEnum.Authority.ADMIN)
    @GetMapping("/api/admin/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}