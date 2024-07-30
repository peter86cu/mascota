package com.apk.login.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, String> {
	
	 @Query(value="SELECT b.* FROM businesses b join businesses_activity ba ON(b.id=ba.negocioid) WHERE ba.actividad LIKE %?1% ", nativeQuery=true)
	    List<Business> filtrarListaNegocios(String busqueda);
	
}