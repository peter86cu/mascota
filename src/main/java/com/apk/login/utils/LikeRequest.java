package com.apk.login.utils;

import com.apk.login.modelo.User;

public class LikeRequest {

	 private String albumId;
	    private boolean isLiked;
	    private User user;
	    private int likeCount;

	    // Constructor vacío
	    public LikeRequest() {
	    }

	    // Constructor con parámetros
	    public LikeRequest(String albumId, boolean isLiked) {
	        this.albumId = albumId;
	        this.isLiked = isLiked;
	    }

	    // Getters y setters
	    public String getAlbumId() {
	        return albumId;
	    }

	    public void setAlbumId(String albumId) {
	        this.albumId = albumId;
	    }

	    public boolean isLiked() {
	        return isLiked;
	    }

	    public void setLiked(boolean isLiked) {
	        this.isLiked = isLiked;
	    }

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public int getLikeCount() {
			return likeCount;
		}

		public void setLikeCount(int likeCount) {
			this.likeCount = likeCount;
		}
	}