package com.apk.login.repositorio;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.Vacuna;


@Repository
public interface MascotaVacunaRepository extends JpaRepository<Vacuna, Integer> {

    //List<Mascota> findAllByUsuarioid(String idDueno);

}
