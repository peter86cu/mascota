package com.apk.login.modelo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MascotaVacunaRepository extends JpaRepository<Vacuna, Integer> {

    //List<Mascota> findAllByUsuarioid(String idDueno);

}