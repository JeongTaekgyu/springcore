package com.taek.springcore.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 편의상 CSRF protection 을 비활성화 
        http.csrf().disable();  // 이렇게 한번에 안해주면 페이지별로 csrf를 무시하는 처리를 해줘야함

        /* 회원 관리 처리 API (POST /user/**) 에 대해 CSRF 무시
         (아래 명령어 때문에 get user/signup은 문제가 없었는데 ), post 요청 /user/signup (회원 가입을 실제로 처리하는 그 요청을 할 때)
         .antMatchers("/user/**").permitAll() 이것만으로는 허용이 안된다. 즉, csrf 라는 것 때문에 post 요청이 오면 스프링 시큐리티가 자동적으로
         csrf 토큰을 확인하는 작업이 있는데 그 csrf를 무시하는 처리를 해준다 (스프링 프레임워크를 사용하면 프레임워크의 룰을 따라야하기 때문에 받아 들이자 )
         */
//        http.csrf()
//                .ignoringAntMatchers("/user/**");

        http.authorizeRequests()
            // image 폴더를 login 없이 허용
                .antMatchers("/images/**").permitAll()
            // css 폴더를 login 없이 허용
                .antMatchers("/css/**").permitAll()
            // 회원 관리 처리 API 전부를 login 없이 허용
                .antMatchers("/user/**").permitAll()
            // 그 외 어떤 요청이든 '인증'
                .anyRequest().authenticated()
                .and()
                // 로그인 기능 허용
                    .formLogin()
                // 로그인 View 제공 (GET /user/login)
                    .loginPage("/user/login")   // 로그인 페이지 경로 이동 (컨트롤러가 받는다 )
                // 로그인 처리 (POST /user/login)
                    .loginProcessingUrl("/user/login")
                // 로그인 성공 시 url ( '/(루트)'위치로 이동 )
                    .defaultSuccessUrl("/")
                // 로그인 처리 후 실패 시 URL
                    .failureUrl("/user/login?error")
                    .permitAll()    // 로그인에 관련된 기능에 대해서 허용을 해줘라
                .and()
                // 로그아웃 기능 허용
                    .logout()
                // 로그아웃 처리 URL
                    .logoutUrl("/user/logout")
                    .permitAll()
                .and()
                .exceptionHandling()
                // "접근 불가" 페이지 URL 설정
                .accessDeniedPage("/forbidden.html");
    }
}