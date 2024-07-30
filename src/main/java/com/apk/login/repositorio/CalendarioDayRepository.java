package com.apk.login.repositorio;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.CalendarioDay;


@Repository
public interface CalendarioDayRepository extends JpaRepository<CalendarioDay, String> {

	 @Query(value="SELECT * FROM calendario_day  WHERE calendarioid=:id", nativeQuery=true)
	    List<CalendarioDay> obtenerCalendarioTrabajo(@Param("id") String id);

}
