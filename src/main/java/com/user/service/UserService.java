package com.user.service;

import java.util.Map;

import com.user.binding.LoginForm;
import com.user.binding.UnlockAccForm;
import com.user.binding.UserForm;

public interface UserService {

	public String checkEmail(String email);

	public Map<Integer, String> getCountries();

	public Map<Integer, String> getStates(Integer countryId);

	public Map<Integer, String> getCities(Integer stateId);

	public String registerUser(UserForm user) throws Exception;

	public String unlockAccount(UnlockAccForm accForm);

	public String login(LoginForm loginForm);

	public String forgotPwd(String email) throws Exception;
}
