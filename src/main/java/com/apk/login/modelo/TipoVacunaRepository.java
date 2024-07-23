package com.apk.login.modelo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TipoVacunaRepository extends JpaRepository<TipoVacunas, Integer> {

	List<TipoVacunas> findAllBytipoRaza(String tipo);

}
