package com.smartmanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Otps")
public class OTP {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(unique = true)
	private String email;
	private Integer OTP;

	public OTP() {
		super();
	}

	public OTP(String email, Integer oTP) {
		super();
		this.email = email;
		OTP = oTP;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getOTP() {
		return OTP;
	}

	public void setOTP(Integer oTP) {
		OTP = oTP;
	}

	@Override
	public String toString() {
		return "OTP [" + "email=" + email + ", OTP=" + OTP + "]";
	}

}
