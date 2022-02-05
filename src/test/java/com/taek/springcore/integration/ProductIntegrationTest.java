package com.taek.springcore.integration;


import com.taek.springcore.dto.ProductMypriceRequestDto;
import com.taek.springcore.dto.ProductRequestDto;
import com.taek.springcore.model.Product;
import com.taek.springcore.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductIntegrationTest {
    @Autowired  // 단위 테스트에 는 안된다, 스프링 Bean을 DI 한다., ProductService가 이쪽으로 주입된다.
    ProductService productService;

    Long userId = 100L;
    Product createdProduct = null;
    int updatedMyPrice = -1;

    @Test
    @Order(1)
    @DisplayName("신규 관심상품 등록")
    void test1() {
// given
        String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
        String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
        int lPrice = 77000;
        ProductRequestDto requestDto = new ProductRequestDto(
                title,
                imageUrl,
                linkUrl,
                lPrice
        );

// when - 이 코드가 실행된 결과를 써서 -> then 처럼 되야한다.
        Product product = productService.createProduct(requestDto, userId);
        // 샘플이 DB까지 저장되고 그 결과를 받아온다
        // 아래 assertEquals로 db에 가져온 데이터와 일치하는지 확인한다.

// then
        assertNotNull(product.getId());
        assertEquals(userId, product.getUserId());
        assertEquals(title, product.getTitle());
        assertEquals(imageUrl, product.getImage());
        assertEquals(linkUrl, product.getLink());
        assertEquals(lPrice, product.getLprice());
        assertEquals(0, product.getMyprice());
        createdProduct = product;   // this.createdProduct = product;
        // 생성된 product 정보를 createdProduct에 저장해서 아래 코드에서 사용한다.
    }

    @Test
    @Order(2)
    @DisplayName("신규 등록된 관심상품의 희망 최저가 변경")
    void test2() {
// given
        Long productId = this.createdProduct.getId();
        int myPrice = 70000;
        ProductMypriceRequestDto requestDto = new ProductMypriceRequestDto(myPrice);

// when - 이 코드가 실행된 결과를 써서 -> then 처럼 되야한다.
        Product product = productService.updateProduct(productId, requestDto);

// then
        assertNotNull(product.getId());
        assertEquals(userId, product.getUserId());
        assertEquals(this.createdProduct.getTitle(), product.getTitle());
        assertEquals(this.createdProduct.getImage(), product.getImage());
        assertEquals(this.createdProduct.getLink(), product.getLink());
        assertEquals(this.createdProduct.getLprice(), product.getLprice());
        assertEquals(myPrice, product.getMyprice());
        this.updatedMyPrice = myPrice;  // 다음 테스트에 사용하기위한 용도
    }

    @Test
    @Order(3)
    @DisplayName("회원이 등록한 모든 관심상품 조회")
    void test3() {
// given

// when
        List<Product> productList = productService.getProducts(userId); // 모든 상품을 다 가져온다.

// then
// 1. 전체 상품에서 테스트에 의해 생성된 상품 찾아오기 (상품의 id 로 찾음)
        Long createdProductId = this.createdProduct.getId();
        // productList 전체를 돌면서 product에서 id가 createdProductId(생성된 프로젝트 id)와
        // 동일한 상품을 찾아오는 코드
        Product foundProduct = productList.stream()
                .filter(product -> product.getId().equals(createdProductId))
                .findFirst()
                .orElse(null);

    // 2. Order(1) 테스트에 의해 생성된 상품과 일치하는지 검증
        assertNotNull(foundProduct);    // NotNULL 이어야 하는데 NULL 이면 Exception에러가 발생함
        // 기댓값과 실제값이 일치하는지 검증
        assertEquals(userId, foundProduct.getUserId());
        assertEquals(this.createdProduct.getId(), foundProduct.getId());
        assertEquals(this.createdProduct.getTitle(), foundProduct.getTitle());
        assertEquals(this.createdProduct.getImage(), foundProduct.getImage());
        assertEquals(this.createdProduct.getLink(), foundProduct.getLink());
        assertEquals(this.createdProduct.getLprice(), foundProduct.getLprice());

    // 3. Order(2) 테스트에 의해 myPrice 가격이 정상적으로 업데이트되었는지 검증
        assertEquals(this.updatedMyPrice, foundProduct.getMyprice());
    }
}
