package com.apk.login.modelo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MascotaPesoRepository extends JpaRepository<PesoMascota, Integer> {

    //List<Mascota> findAllByUsuarioid(String idDueno);

}
