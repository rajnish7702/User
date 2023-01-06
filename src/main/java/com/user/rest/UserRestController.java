package com.user.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.binding.LoginForm;
import com.user.binding.UnlockAccForm;
import com.user.binding.UserForm;
import com.user.service.UserService;


@RestController
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginForm loginForm){
		String status =  userService.login(loginForm);
		return new ResponseEntity<> (status,HttpStatus.OK);
	}
	@GetMapping("/country")
	public Map<Integer,String> loadCountry(){
		return userService.getCountries();
	}
	@GetMapping("/state/{countruId}")
	public Map<Integer,String> loadState(@PathVariable Integer countryId){
		return userService.getStates(countryId);
	}
	@GetMapping("/city/{stateId}")
	public Map<Integer,String> loadCity(@PathVariable Integer stateId){
		return userService.getCities(stateId);
	}
	@GetMapping("/email/{email}")
	public String checkEmail(@PathVariable String email) {
		return userService.checkEmail(email);
	}
	@PostMapping("/user")
	public ResponseEntity<String> userRegistration(@RequestBody UserForm userForm) throws Exception{
		String status = userService.registerUser(userForm);
		return new ResponseEntity<>(status,HttpStatus.CREATED);
	}
	@PostMapping("/unlock")
	public ResponseEntity<String> unlockAccount(UnlockAccForm unlockAccForm){
		String status = userService.unlockAccount(unlockAccForm);
		return new ResponseEntity<> (status,HttpStatus.OK);
	}
	@GetMapping("/forget/{email}")
	public ResponseEntity<String> forgotPwd(@PathVariable String email) throws Exception{
		String status = userService.forgotPwd(email);
		return new ResponseEntity<>(status,HttpStatus.OK);
	}
	
}
