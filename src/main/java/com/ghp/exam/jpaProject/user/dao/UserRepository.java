package com.ghp.exam.jpaProject.user.dao;

import com.ghp.exam.jpaProject.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
