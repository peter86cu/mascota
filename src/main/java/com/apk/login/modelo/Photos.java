package com.apk.login.modelo;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;


@Entity
@Table(name="photos")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Photos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String photoid;
    private String photo;
    
    @Temporal(TemporalType.DATE)
    @Column(name="created_at")
   	private Date fechaCreado;
    @ManyToOne
    @JoinColumn(name = "albumid", nullable = false) // Clave foránea
    @JsonBackReference
    private MascotaAlbun album;
   	@Column(name="media_type")
    private String mediaType;
	
	public String getPhotoid() {
		return photoid;
	}
	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Date getFechaCreado() {
		return fechaCreado;
	}
	public void setFechaCreado(Date fechaCreado) {
		this.fechaCreado = fechaCreado;
	}
	public MascotaAlbun getAlbum() {
		return album;
	}
	public void setAlbum(MascotaAlbun album) {
		this.album = album;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	
    
}
