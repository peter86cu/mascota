package com.apk.login.repositorio;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.PesoMascota;


@Repository
public interface MascotaPesoRepository extends JpaRepository<PesoMascota, Integer> {

    @Query(value="SELECT * FROM pesos_mascota  WHERE mascotaid=:mascotaid", nativeQuery=true)
	List<PesoMascota> findAllPesoMascotas(@Param("mascotaid") String mascotaId);

}
