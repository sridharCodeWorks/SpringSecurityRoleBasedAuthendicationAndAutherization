package com.project.springsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.springsecurity.entity.MyUser;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {
	
	public Optional<MyUser> findByUserName(String userName);
}
