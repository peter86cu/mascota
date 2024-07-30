package com.apk.login.repositorio;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.ActividadEstilista;
import com.apk.login.modelo.BusinessActivity;
import com.apk.login.modelo.NegociosActividade;


@Repository
public interface BussinessActivityRepository extends JpaRepository<BusinessActivity, String> {

   // List<ActividadEstilista> findAllByUsuario(String id);
    
    @Query(value="SELECT * FROM businesses_activity  WHERE negocioid=:negocioId and status=:status", nativeQuery=true)
    List<BusinessActivity> obtenerActividadesNegocios(@Param("negocioId") String negocioId, @Param("status") String status);

}
