package com.apk.login.modelo;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import net.minidev.json.annotate.JsonIgnore;

@Entity
@Table(name="businesses")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String rut;
    private String name;
    private String address;
    private String phone;
    private String longitud;
    private String latitud;
    private String logoUrl;
    private float averageRating;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "userid") 
    private User usuario;
    
    @OneToMany(mappedBy = "negocio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BusinessActivity> activity;
    
    private int reviewCount;
    
//    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comment;
      
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public float getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
    @JsonIgnore
	public User getUsuario() {
		return usuario;
	}
	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public List<BusinessActivity> getActivity() {
		return activity;
	}
	public void setActivity(List<BusinessActivity> activity) {
		this.activity = activity;
	}
//	public List<Comment> getComment() {
//		return comment;
//	}
//	public void setComment(List<Comment> comment) {
//		this.comment = comment;
//	}
//	
	public int getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
	
	
}
