package com.user.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Serializable>{
	// select * from usert_master where email=?
	public User findByEmail(String email);
	
	// select * from user_master where email=? and pwd = ?
	public User findByEmailAndUserPwd(String email, String pwd);
}
