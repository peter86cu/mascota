package com.apk.login.repositorio;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.TipoRaza;


@Repository
public interface TipoRazaRepository extends JpaRepository<TipoRaza, Integer> {

	List<TipoRaza> findAllByTipo(String tipo);

}
