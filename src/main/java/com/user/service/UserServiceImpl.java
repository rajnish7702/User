package com.user.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.binding.LoginForm;
import com.user.binding.UnlockAccForm;
import com.user.binding.UserForm;
import com.user.entity.CityMaster;
import com.user.entity.CountryMaster;
import com.user.entity.StateMaster;
import com.user.entity.User;
import com.user.repository.CityRepository;
import com.user.repository.CountryRepository;
import com.user.repository.StateRepository;
import com.user.repository.UserRepository;
import com.user.util.EmailUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private StateRepository stateRepo;

	@Autowired
	private CityRepository cityRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public String checkEmail(String email) {
		User user = userRepo.findByEmail(email);
		if (user == null) {
			return "UNIQUE";
		}
		return "DUBLICATE";
	}

	@Override
	public Map<Integer, String> getCountries() {
		List<CountryMaster> countries = countryRepo.findAll();
		Map<Integer, String> countryMap = new HashMap<>();
		countries.forEach(country -> {
			countryMap.put(country.getCountryId(), country.getCountryName());
		});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		List<StateMaster> states = stateRepo.findByCountryId(countryId);
		Map<Integer, String> stateMap = new HashMap<>();
		states.forEach(state -> {
			stateMap.put(state.getStateId(), state.getStateName());
		});
		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		List<CityMaster> citys = cityRepo.findByStateId(stateId);
		Map<Integer, String> cityMap = new HashMap<>();
		citys.forEach(city -> {
			cityMap.put(city.getCityId(), city.getCityName());
		});
		return cityMap;
	}

	@Override
	public String registerUser(UserForm userform) throws Exception {
		User entity = new User();
		// copy date binding object to entity object
		BeanUtils.copyProperties(userform, entity);

		// generate & set Random Password
		entity.setUserPwd(generateRandomPwd());
		// set account status is lock
		entity.setAccStatus("LOCK");

		userRepo.save(entity);

		// send the email to unlock accout
		String to = userform.getEmail();
		String subject = "Registration Form";
		String body = readEmailBody("REG_EMAIL_BODY.txt", entity);
		emailUtils.sendEmail(to, subject, body);

		return "User Accout is Created";
	}

	@Override
	public String unlockAccount(UnlockAccForm accForm) {
		
		String email = accForm.getEmail();
		
		User user = userRepo.findByEmail(email);
		
		if (user !=null && user.getUserPwd().equals(accForm.getTempPwd())) {
			user.setUserPwd(accForm.getNewPwd());
			user.setAccStatus("UNLOCK");
			userRepo.save(user);
			return "ACCOUNT UNLOCK";
		}
		return "Invalid Temp Password";
	}

	@Override
	public String login(LoginForm loginForm) {
		User user = userRepo.findByEmailAndUserPwd(loginForm.getEmail(), loginForm.getPwd());
		if (user == null) {
			return "Invalid Credintial";
		}
		if (user.getAccStatus().equals("LOCK")) {
			return "Account is Locak";
		}
		return "Sucess";
	}

	@Override
	public String forgotPwd(String email) throws Exception {
		User user = userRepo.findByEmail(email);
		if (user == null) {
			return "No Account Found";
		}
		// TO send email password with email
		String subject = "Recover Password";
		String body = readEmailBody("FORGOT_EMAIL_PWD_BODY.txt", user);
		emailUtils.sendEmail(email, subject, body);
		return "Password send to registration mail";
	}

	public String generateRandomPwd() {

		String text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		int pwdLength = 6;
		for (int i = 1; i <= pwdLength; i++) {
			int index = random.nextInt(text.length());
			sb.append(text.charAt(index));
		}
		return sb.toString();
	}

	private String readEmailBody(String filename, User user) throws Exception {
		StringBuffer sb = new StringBuffer();
		try (Stream<String> lines = Files.lines(Paths.get(filename))) {
			lines.forEach(line -> {
				line = line.replace("${FNAME}", user.getFname());
				line = line.replace("${LNAME}", user.getLname());
				line = line.replace("${TEMP_PWD}", user.getUserPwd());
				line = line.replace("${EMAIL}", user.getEmail());
				line = line.replace("${PWD}", user.getUserPwd());
				sb.append(line);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
