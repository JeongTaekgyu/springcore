package com.taek.springcore.service;

import com.taek.springcore.dto.ProductMypriceRequestDto;
import com.taek.springcore.dto.ProductRequestDto;
import com.taek.springcore.model.Product;
import com.taek.springcore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    public static final int MIN_MY_PRICE = 100;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductRequestDto requestDto, Long userId ) {
// 요청받은 DTO 로 DB에 저장할 객체 만들기
        Product product = new Product(requestDto, userId);

        productRepository.save(product);

        return product;
    }

    public Product updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        int myprice = requestDto.getMyprice();
        if (myprice < MIN_MY_PRICE) {
            // 여기 들어왔기 때문에 productRepository.findById(id)를 안타서 에러가 안났다.
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + " 원 이상으로 설정해 주세요.");
        }

        Product product = productRepository.findById(id)    // 내용이 없으면 여기서 에러
                .orElseThrow(() -> new NullPointerException("해당 아이디가 존재하지 않습니다.")); // 결과가 null 이면 여기서 에러

        product.setMyprice(myprice);
        productRepository.save(product);

        return product;
    }

    // 회원 ID 로 등록된 상품 조회
    public List<Product> getProducts(Long userId) {
        return productRepository.findAllByUserId(userId);
    }

    // (관리자용) 상품 전체 조회
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}