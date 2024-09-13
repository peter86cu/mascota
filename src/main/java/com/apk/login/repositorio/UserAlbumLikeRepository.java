package com.apk.login.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apk.login.modelo.MascotaAlbun;
import com.apk.login.modelo.User;
import com.apk.login.modelo.UserAlbumLike;

public interface UserAlbumLikeRepository extends JpaRepository<UserAlbumLike, String> {
    Optional<UserAlbumLike> findByUserAndAlbum(User user, MascotaAlbun album);
    
    @Query("SELECT a.likeCount FROM MascotaAlbun a WHERE a = :album")
    int findLikeCountByAlbum(MascotaAlbun album);
}
