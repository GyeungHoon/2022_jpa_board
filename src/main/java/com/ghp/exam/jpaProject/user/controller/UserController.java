package com.ghp.exam.jpaProject.user.controller;

import com.ghp.exam.jpaProject.article.dao.ArticleRepository;
import com.ghp.exam.jpaProject.article.domain.Article;
import com.ghp.exam.jpaProject.user.dao.UserRepository;
import com.ghp.exam.jpaProject.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usr/user")
public class UserController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("list")
    @ResponseBody
    public List<Article> showList() {
        return articleRepository.findAll();
    }

    @RequestMapping("detail")
    @ResponseBody
    public Article showDetail(long id) {
        Optional<Article> article = articleRepository.findById(id);
        return article.get();
    }

    @RequestMapping("doModify")
    @ResponseBody
    public Article doModify(long id, String title, String body) {
        Article article = articleRepository.findById(id).get();

        if ( title != null ) {
            article.setTitle(title);
        }

        if ( body != null ) {
            article.setBody(body);
        }

        article.setUpdateDate(LocalDateTime.now());

        articleRepository.save(article);

        return article;
    }

    @RequestMapping("doDelete")
    @ResponseBody
    public String doDelete(long id) {
        if ( articleRepository.existsById(id) == false ) {
            return "%d번 게시물은 이미 삭제되었거나 존재하지 않습니다.".formatted(id);
        }

        articleRepository.deleteById(id);
        return "%d번 게시물이 삭제되었습니다.".formatted(id);
    }

    @RequestMapping("doJoin")
    @ResponseBody
    public String doJoin(String name, String password, String email) {
        if ( name == null || name.trim().length() == 0 ) {
            return "이름을 입력해주세요.";
        }

        name = name.trim();

        if ( password == null || password.trim().length() == 0 ) {
            return "비밀번호을 입력해주세요.";
        }

        password = password.trim();


        if ( email == null || email.trim().length() == 0 ) {
            return "이메일을 입력해주세요.";
        }

        email = email.trim();


        User user = new User();
        user.setRegDate(LocalDateTime.now());
        user.setUpdateDate(LocalDateTime.now());
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);

        userRepository.save(user);

        return "%d번 회원이 생성되었습니다.".formatted(user.getId());
    }
}