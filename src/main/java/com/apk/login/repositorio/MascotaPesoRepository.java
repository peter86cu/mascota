package com.apk.login.repositorio;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.PesoMascota;


@Repository
public interface MascotaPesoRepository extends JpaRepository<PesoMascota, Integer> {

    //List<Mascota> findAllByUsuarioid(String idDueno);

}
