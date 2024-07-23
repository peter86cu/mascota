package com.apk.login.modelo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TipoRazaRepository extends JpaRepository<TipoRaza, Integer> {

	List<TipoRaza> findAllByTipo(String tipo);

}
