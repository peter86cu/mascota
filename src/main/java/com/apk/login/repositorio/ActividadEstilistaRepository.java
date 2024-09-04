package com.apk.login.repositorio;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.ActividadEstilista;


@Repository
public interface ActividadEstilistaRepository extends JpaRepository<ActividadEstilista, String> {

   // List<ActividadEstilista> findAllByUsuario(String id);
    
    @Query(value="SELECT * FROM actividad_estilista  WHERE userid=:userId and status=:status", nativeQuery=true)
    List<ActividadEstilista> obtenerActividadesEstilista(@Param("userId") String userId, @Param("status") String status);
    
    
    @Query(value="SELECT * FROM actividad_estilista  WHERE userid=:userId", nativeQuery=true)
    List<ActividadEstilista> obtenerAllActividadesEstilista(@Param("userId") String userId);
    
    @Query(value="SELECT * FROM actividad_estilista  WHERE mascotaid=:mascotaId and status=:status", nativeQuery=true)
    List<ActividadEstilista> obtenerActividadesMascota(@Param("mascotaId") String mascotaId, @Param("status") String status);

}
