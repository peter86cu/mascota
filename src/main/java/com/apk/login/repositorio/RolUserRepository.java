package com.apk.login.repositorio;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apk.login.modelo.User;
import com.apk.login.modelo.UserRoles;

public interface RolUserRepository extends JpaRepository<UserRoles, Integer>{
	
	
    
}
