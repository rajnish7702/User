package com.user.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.entity.CountryMaster;
@Repository
public interface CountryRepository extends JpaRepository<CountryMaster,Serializable>{
	
}
