package com.apk.login.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {
	
	 @Query(value="SELECT * FROM activity WHERE STATUS='Disponible'", nativeQuery=true)
	    List<Activity> listadoActividadesActivas();
	
}