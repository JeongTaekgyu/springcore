package com.taek.springcore.model;

import com.taek.springcore.dto.ProductRequestDto;
import com.taek.springcore.validator.ProductValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Product {

    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    // 반드시 값을 가지도록 합니다.
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private int lprice;

    @Column(nullable = false)
    private int myprice;

    @Column(nullable = false)
    private Long userId;// 회원의 아이디( test1 같은 username 말고 테이블이 가지고 있는 고유 id)

    // 관심 상품 생성 시 이용합니다.
    public Product(ProductRequestDto requestDto, Long userId) {
        // 입력값 Validation
        ProductValidator.validateProductInput(requestDto, userId);
        // 참고로 ProductValidator 클래스에서  validateProductInput 메서드를
        // static으로 선언해서 new 안하고 이렇게 사용이 가능하다

        // 관심상품을 등록한 회원 테이블의 Id 저장(userId는 username 말고 테이블이 가지고 있는 고유 id 이다)
        this.userId = userId;
        this.title = requestDto.getTitle();
        this.image = requestDto.getImage();
        this.link = requestDto.getLink();
        this.lprice = requestDto.getLprice();
        this.myprice = 0;
    }
}