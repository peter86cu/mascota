package com.apk.login.repositorio;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apk.login.modelo.User;

public interface UserRepository extends JpaRepository<User, String>{
	
	User findByUsernameAndPassword(String username, String password);	
	
    //User findByUsername(String username);
    
    Optional<User> findByUsername(String username);


    @Transactional
    @Modifying
    @Query(value="UPDATE user SET state = 2 WHERE id =:id", nativeQuery=true)
    int confirmarPedido(@Param("id") String id);
    
    @Transactional
    @Modifying
    @Query(value="UPDATE user SET password =:pass WHERE id =:id", nativeQuery=true)
    int confirmarNewPassword(@Param("id") String id, @Param("pass") String password);
    
}
