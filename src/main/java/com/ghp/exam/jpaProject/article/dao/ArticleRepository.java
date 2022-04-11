package com.ghp.exam.jpaProject.article.dao;

import com.ghp.exam.jpaProject.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ArticleRepository extends JpaRepository<Article, Long> {
}