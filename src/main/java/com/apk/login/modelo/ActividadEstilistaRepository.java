package com.apk.login.modelo;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ActividadEstilistaRepository extends JpaRepository<ActividadEstilista, Integer> {

   // List<ActividadEstilista> findAllByUsuario(String id);
    
    @Query(value="SELECT * FROM actividad_estilista  WHERE userid=:userId", nativeQuery=true)
    List<ActividadEstilista> obtenerActividadesEstilista(@Param("userId") String userId);

}
