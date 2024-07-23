package com.apk.login.modelo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CalendarioDayRepository extends JpaRepository<CalendarioDay, Integer> {

    //List<Mascota> findAllByUsuarioid(String idDueno);

}
