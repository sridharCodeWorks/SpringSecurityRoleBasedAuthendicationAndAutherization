package com.project.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.springsecurity.entity.MyUser;
import com.project.springsecurity.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String home(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails myUser = (UserDetails) authentication.getPrincipal();
			MyUser user = userService.findUserByUserName(myUser.getUsername());
			System.out.println(user);
			model.addAttribute("user", user);
		} else {
			model.addAttribute("user", null);
		}
		return "home";
	}

	@GetMapping("/admin/home")
	public String adminHome() {
		return "adminHome";
	}

	@GetMapping("/user/home")
	public String userHome() {
		return "userHome";
	}

	@GetMapping("/signIn/account")
	public String signInAccount() {
		return "signInUser";
	}

	@PostMapping("/signIn/account")
	public String createNewAccount(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "userEmail") String userEmail,
			@RequestParam(value = "userPassword") String userPassword, MyUser myUser) {
		MyUser storedMyUser = userService.signInAccount(userName, userEmail, passwordEncoder.encode(userPassword),
				myUser);
		return storedMyUser != null ? "redirect:/" : "redirect:/signIn/account";
	}

	@ResponseBody
	@PostMapping("/register/adminUser")
	public MyUser registeMyUser(@RequestBody MyUser myUser) {
		myUser.setUserPassword(passwordEncoder.encode(myUser.getUserPassword()));
		return userService.registerUser(myUser);
	}

	@GetMapping("/login")
	public String login() {
		return "customLogin";
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			System.out.println(userDetails.toString());
			model.addAttribute("userName", userDetails.getUsername());
		}
		return "customLogout";
	}
}
