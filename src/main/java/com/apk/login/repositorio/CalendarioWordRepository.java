package com.apk.login.repositorio;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.CalendarioWork;


@Repository
public interface CalendarioWordRepository extends JpaRepository<CalendarioWork, String> {

	

    @Query(value="SELECT * FROM calendario_work  WHERE userid=:userId", nativeQuery=true)
    CalendarioWork obtenerCalendario(@Param("userId") String userId);
    
    

}
