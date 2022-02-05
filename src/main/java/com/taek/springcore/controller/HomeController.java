package com.taek.springcore.controller;

import com.taek.springcore.model.UserRoleEnum;
import com.taek.springcore.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")            // @AuthenticationPrincipal이 있으면 로그인한 사용자의 정보가 넘어온다.
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("username", userDetails.getUsername());

        if(userDetails.getUser().getRole() == UserRoleEnum.ADMIN){
            model.addAttribute("admin_role",true);
        }

        return "index";
    }
}
