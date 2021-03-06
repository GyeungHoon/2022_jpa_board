package com.ghp.exam.jpaProject.user.controller;

import com.ghp.exam.jpaProject.user.dao.UserRepository;
import com.ghp.exam.jpaProject.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/usr/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;


    @RequestMapping("doLogout")
    @ResponseBody
    public String doLogout(HttpSession session){
        boolean isLogined = false;
        long loginedUserId = 0;

        if (session.getAttribute("loginedUserId") != null){
            isLogined = true;
        }
        if (isLogined == false){
            return "이미 로그아웃 되었습니다.";
        }
        session.removeAttribute("loginedUserId");
        return "로그아웃 되었습니다.";

    }


    @RequestMapping("doLogin")
    @ResponseBody
    public String doLogin(String email, String password, HttpServletRequest req, HttpServletResponse resp) {
        if (email == null || email.trim().length() == 0) {
            return "이메일을 입력해주세요.";
        }

        email = email.trim();


//      User user = userRepository.findByEmail(email).orElseGet(() -> null);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return "일치하는 회원이 존재하지 않습니다.";
        }

        if (password == null || password.trim().length() == 0) {
            return "비밀번호를 입력해주세요.";
        }

        password = password.trim();

        System.out.println("user.getPassword() : " + user.get().getPassword());
        System.out.println("password : " + password);

        if (user.get().getPassword().equals(password) == false) {
            return "비밀번호가 일치하지 않습니다.";
        }

        HttpSession session =  req.getSession();
        session.setAttribute("loginedUserId", user.get().getId());

        return "%s님 환영합니다.".formatted(user.get().getName());
    }

    @RequestMapping("doJoin")
    @ResponseBody
    public String doJoin(String name, String email, String password) {
        if (name == null || name.trim().length() == 0) {
            return "이름을 입력해주세요.";
        }

        name = name.trim();

        if (email == null || email.trim().length() == 0) {
            return "이메일을 입력해주세요.";
        }

        email = email.trim();

        boolean existsByEmail = userRepository.existsByEmail(email);

        if (existsByEmail) {
            return "입력하신 이메일(%s)은 이미 사용중입니다.".formatted(email);
        }

        if (password == null || password.trim().length() == 0) {
            return "비밀번호를 입력해주세요.";
        }

        password = password.trim();

        User user = new User();
        user.setRegDate(LocalDateTime.now());
        user.setUpdateDate(LocalDateTime.now());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);

        return "%d번 회원이 생성되었습니다.".formatted(user.getId());
    }


    @RequestMapping("me")
    @ResponseBody
    public User showMe(HttpSession session) {
        boolean isLogined = false;
        long loginedUserId = 0;

        if(session.getAttribute("loginedUserId") != null){
            isLogined = true;
            loginedUserId = (long) session.getAttribute("loginedUserId");
        }


        if(isLogined == false){
            return null;
        }
        Optional<User> user = userRepository.findById(loginedUserId);

        if(user.isEmpty()){
            return null;
        }

        return user.get();
    }

}