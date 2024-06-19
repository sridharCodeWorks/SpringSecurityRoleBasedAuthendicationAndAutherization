package com.project.springsecurity.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.project.springsecurity.entity.MyUser;

@Service
public interface UserService extends UserDetailsService {

	public MyUser registerUser(MyUser myUser);

	public MyUser signInAccount(String userName, String userEmail, String userPassword, MyUser myUser);

	public MyUser findUserByUserName(String username);

}