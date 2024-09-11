package com.apk.login.repositorio;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.Activity;
import com.apk.login.modelo.Mascota;
import com.apk.login.modelo.MascotaAlbun;

@Repository
public interface AlbumMascotaRepository extends JpaRepository<MascotaAlbun, String> {
	
	List<MascotaAlbun> findByMascota(Mascota mascota);
	
	 @Modifying
	    @Transactional
	    @Query("UPDATE MascotaAlbun a SET a.isSelected = :isSelected WHERE a.id = :albumId")
	    void updateAlbumIsSelected(String albumId, boolean isSelected);
	 
	 @Modifying
	    @Transactional
	    @Query("UPDATE MascotaAlbun a SET a.likeCount = :like, a.isLiked=:isLiked  WHERE a.id = :albumId")
	    void updateAlbumIsLike(String albumId, int like, boolean isLiked );
	 
	    @Query(value="SELECT * FROM mascota_album a where a.selected = :isSelected", nativeQuery=true)
	    List<MascotaAlbun> sharedAlbumIsSelected( boolean isSelected);
}