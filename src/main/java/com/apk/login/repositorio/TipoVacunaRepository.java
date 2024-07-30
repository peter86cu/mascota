package com.apk.login.repositorio;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.TipoVacunas;


@Repository
public interface TipoVacunaRepository extends JpaRepository<TipoVacunas, Integer> {

	List<TipoVacunas> findAllBytipoRaza(String tipo);

}
