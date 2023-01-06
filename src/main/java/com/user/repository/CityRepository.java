package com.user.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.entity.CityMaster;
@Repository
public interface CityRepository extends JpaRepository<CityMaster,Serializable>{
	public List<CityMaster> findByStateId(Integer stateId);
}
