package com.project.springsecurity.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.springsecurity.entity.MyUser;
import com.project.springsecurity.repository.UserRepository;
import com.project.springsecurity.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<MyUser> myUser = userRepository.findByUserName(username);
		if (myUser.isPresent()) {
			System.out.println(myUser);
			MyUser user = myUser.get();
			return User.builder().username(user.getUserName()).password(user.getUserPassword())
					.roles(user.getUserRole()).build();
		} else {
			throw new UsernameNotFoundException(username);
		}
	}

	@Override
	public MyUser registerUser(MyUser myUser) {
		return userRepository.save(myUser);
	}

	@Override
	public MyUser signInAccount(String userName, String userEmail, String userPassword, MyUser myUser) {
		myUser.setUserName(userName);
		myUser.setUserEmail(userEmail);
		myUser.setUserPassword(userPassword);
		myUser.setUserRole("USER");
		return userRepository.save(myUser);
	}

	@Override
	public MyUser findUserByUserName(String username) {
		return userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(username));
	}
}
