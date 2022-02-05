package com.taek.springcore.service;



import com.taek.springcore.dto.ProductMypriceRequestDto;
import com.taek.springcore.dto.ProductRequestDto;
import com.taek.springcore.model.Product;
import com.taek.springcore.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.taek.springcore.service.ProductService.MIN_MY_PRICE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("관심 상품 희망가 - 최저가 이상으로 변경")
    void updateProduct_Normal() {
// given
        Long productId = 100L;
        int myprice = MIN_MY_PRICE + 1000;

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto(
                myprice
        );

        Long userId = 777L;
        ProductRequestDto requestProductDto = new ProductRequestDto(
                "오리온 꼬북칩 초코츄러스맛 160g",
                "https://shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg",
                "https://search.shopping.naver.com/gate.nhn?id=24161228524",
                2350
        );

        Product product = new Product(requestProductDto, userId);

        ProductService productService = new ProductService(productRepository);
        // 전 코드와 바뀐건 여기와 위에 Long userId = 777L; 부터 requestProductDto 객체까지
        when(productRepository.findById(productId)) // Mock 객체에 findById 함수가 호출 됐을 때
                .thenReturn(Optional.of(product));  //thenReturn(결과)를 내줘라, 그 결과과 Optional 형태
        // 즉, 이것은 가짜객체인데 productRepository에 가서
        // 원래는 db를 갔다와서 db에 있는 진짜 내용을 가지고 오든가 아니면 우리가 만들어 준 것처럼 가짜 객체에서
        // list product 해서 메모리에 변수를 만들었다. 그런식으로 가짜로 만들어서 그것에서 가지고 오든가 했어야했는데

        //지금은 안쪽은 상관없고 이런 input 이 있을 때 이런 output이 나와야돼 라고 하는것
        // 그래서 productRepository.findById 해주면 thenReturn 으로 결과
        // MockProductService 만든것 처럼 Mockito가 알아서 해준다.
        // 즉 우리는 repository에서 신경을 안써도된다.
// when
        Product result = productService.updateProduct(productId, requestMyPriceDto);

// then
        assertEquals(myprice, result.getMyprice());
    }

    @Test
    @DisplayName("관심 상품 희망가 - 최저가 미만으로 변경")
    void updateProduct_Failed() {
// given
        Long productId = 100L;
        int myprice = MIN_MY_PRICE - 50;

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto(
                myprice
        );

        ProductService productService = new ProductService(productRepository);

// when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(productId, requestMyPriceDto);
        });

// then
        assertEquals(
                "유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + " 원 이상으로 설정해 주세요.",
                exception.getMessage()
        );
    }
}