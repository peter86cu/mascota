package com.apk.login.repositorio;
import java.util.List;
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
	
    @Query(value="SELECT * FROM user u WHERE u.username=:username ", nativeQuery=true)
    User findUsername(String username);
    
    Optional<User> findByUsernameAndPlataforma(String username, String plataforma);
    
    @Query(value="SELECT ur.rolid, r.descripcion,u.username, u.phone FROM user u , user_roles ur, roles r WHERE u.userid=ur.userid AND r.id=ur.rolid AND (u.username=:username or u.phone=:phone)", nativeQuery=true)
    List<Object> validarExisteUserPhone(@Param("username") String username, @Param("phone") String phone);


    @Transactional
    @Modifying
    @Query(value="UPDATE user SET state = 2 WHERE id =:id", nativeQuery=true)
    int confirmarPedido(@Param("id") String id);
    
    @Transactional
    @Modifying
    @Query(value="UPDATE user SET password =:pass WHERE id =:id", nativeQuery=true)
    int confirmarNewPassword(@Param("id") String id, @Param("pass") String password);
    
}
