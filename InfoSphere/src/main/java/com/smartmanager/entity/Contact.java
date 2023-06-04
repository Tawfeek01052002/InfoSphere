package com.smartmanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "Contact")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;

	@NotBlank(message = "Name field can not be empty !!")
	@Size(min = 3, max = 40, message = "Name must be between 3-40 characters")
	private String name;

	@NotBlank(message = "Nickname field can not be empty !!")
	@Size(min = 3, max = 10, message = "Nickname must be between 3-10 characters")
	private String nickName;

	@NotBlank(message = "Work field can not be empty !!")
	@Size(min = 3, max = 30, message = "Name must be between 3-30 characters")
	private String work;

	@Column(unique = true)
	@NotBlank(message = "Email field can not be empty !!")
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Enter the valid Email address")
	private String email;

	@Column(unique = true)
	@NotBlank(message = "Phone number can not be empty !!")
	@Pattern(regexp = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$", message = "Enter the valid Phone number")
	private String phone;

	private String imageUrl;

	@Column(length = 5000)
	@NotBlank(message = "Description field can not be empty !!")
	@Size(min = 10, max = 5000, message = "Description must be between 10-5000 characters")
	private String description;

	@ManyToOne
	@JsonIgnore
	private User user;

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contact(int cId, String name, String nickName, String work, String email, String phone, String imageUrl,
			String description) {
		super();
		this.cId = cId;
		this.name = name;
		this.nickName = nickName;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.imageUrl = imageUrl;
		this.description = description;
	}

	public int getcId() {
		return cId;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

//	@Override
//	public String toString() {
//		return "Contact [cId=" + cId + ", name=" + name + ", nickName=" + nickName + ", work=" + work + ", email="
//				+ email + ", phone=" + phone + ", imageUrl=" + imageUrl + ", description=" + description + ", user="
//				+ user + "]";
//	}

	@Override
	public boolean equals(Object obj) {
		return this.cId == ((Contact) obj).getcId();
	}

}
