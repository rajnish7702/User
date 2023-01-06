package com.user.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.entity.StateMaster;
@Repository 
public interface StateRepository extends JpaRepository<StateMaster, Serializable>{
	public List<StateMaster> findByCountryId(Integer countryId);
}
