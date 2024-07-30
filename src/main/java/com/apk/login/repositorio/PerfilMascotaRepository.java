package com.apk.login.repositorio;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.Mascota;


@Repository
public interface PerfilMascotaRepository extends JpaRepository<Mascota, Integer> {

    //List<Mascota> findAllByUsuarioid(String idDueno);

}
