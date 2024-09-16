package com.apk.login.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.Event;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	 @Query("SELECT e FROM Event e WHERE (e.id > :lastEventId OR e.lastModified < :lastCheckedTime) ORDER BY e.id ASC")
	 List<Event> findNewOrModifiedEvents(@Param("lastEventId") Long lastEventId, @Param("lastCheckedTime") LocalDateTime lastCheckedTime);

    
    @Query(value="SELECT * FROM event e where e.mascota_id = :mascotaId and e.leido=0", nativeQuery=true)
    List<Event> obtenerEventsMascota(String mascotaId);
}