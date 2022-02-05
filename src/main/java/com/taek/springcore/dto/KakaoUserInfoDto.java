package com.taek.springcore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor // 아래 생성자를 생성해준다
@Getter
public class KakaoUserInfoDto {
    private Long id;
    private String nickname;
    private String email;

    // @AllArgsConstructor를 적으면 아래 생성자를 생성해 주는 역할을 한다.
    /*public KakaoUserInfoDto(Long id, String nickname, String email){
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }*/
}