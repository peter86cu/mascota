package com.apk.login.repositorio;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.Activity;
import com.apk.login.modelo.Event;
import com.apk.login.modelo.Mascota;
import com.apk.login.modelo.MascotaAlbun;

@Repository
public interface EventsRepository extends JpaRepository<Event, Long> {
	
	
	 
	    @Query(value="SELECT * FROM event e where e.mascota_id = :mascotaId and e.leido=0", nativeQuery=true)
	    List<Event> obtenerEventsMascota(String mascotaId);
}