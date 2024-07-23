package com.apk.login.modelo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PerfilMascotaRepository extends JpaRepository<Mascota, Integer> {

    //List<Mascota> findAllByUsuarioid(String idDueno);

}
