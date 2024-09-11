package com.apk.login.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apk.login.modelo.Activity;
import com.apk.login.modelo.MascotaAlbun;
import com.apk.login.modelo.Photos;

@Repository
public interface PhotoAlbumMascotaRepository extends JpaRepository<Photos, String> {
	
}