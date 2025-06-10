package com.bookstudio.publisher.model;

import com.bookstudio.shared.util.IdFormatter;

public class Publisher {
	private String publisherId;
	private String formattedPublisherId;
	private String name;
	private String nationalityId;
	private String nationalityName;
	private String literaryGenreId;
	private String literaryGenreName;
	private Integer foundationYear;
	private String website;
	private String address;
	private String status;
	private byte[] photo;
	private String photoBase64;

	public String getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
        this.formattedPublisherId = IdFormatter.formatId(publisherId, "ED");
	}
	
	public String getFormattedPublisherId() {
		return formattedPublisherId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(String nationalityId) {
		this.nationalityId = nationalityId;
	}

	public String getNationalityName() {
		return nationalityName;
	}

	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}

	public String getLiteraryGenreId() {
		return literaryGenreId;
	}

	public void setLiteraryGenreId(String literaryGenreId) {
		this.literaryGenreId = literaryGenreId;
	}

	public String getLiteraryGenreName() {
		return literaryGenreName;
	}

	public void setLiteraryGenreName(String literaryGenreName) {
		this.literaryGenreName = literaryGenreName;
	}

	public int getFoundationYear() {
		return foundationYear;
	}

	public void setFoundationYear(int foundationYear) {
		this.foundationYear = foundationYear;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getPhotoBase64() {
		return photoBase64;
	}

	public void setPhotoBase64(String photoBase64) {
		this.photoBase64 = photoBase64;
	}
}
