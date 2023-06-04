package com.smartmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.smartmanager.entity.OTP;

public interface OTPRepositry extends JpaRepository<OTP, String> {

	public OTP findOTPByEmail(@Param("findemail") String findemail);
}
