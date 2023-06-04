package com.smartmanager.controllers;

import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartmanager.dao.OTPRepositry;
import com.smartmanager.dao.UserRepository;
import com.smartmanager.entity.OTP;
import com.smartmanager.entity.User;
import com.smartmanager.helper.Message;
import com.smartmanager.helper.SendEmail;

@Controller
public class homeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@Autowired
	OTPRepositry otpRepositry;

	@Autowired
	private SendEmail sendEmail;

	private String forgetEmail = null;

	@GetMapping("/")
	public String getHome(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String getAbout(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String getSignUp(Model model) {
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/registered")
	public String getRegister(@Valid @ModelAttribute("user") User user, BindingResult bresult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try {
			if (!agreement) {
				System.out.println("You have not agreed the term and conditions");
				throw new Exception("You have not agreed the term and conditions");
			}
			User isUserPresent = userRepository.getUserByUsername(user.getEmail());

			if (isUserPresent != null) {
				session.setAttribute("message", new Message("User already exist", "alert-danger"));
				return "signup";
			}

			if (bresult.hasErrors()) {
				System.out.println("Error" + bresult.toString());
				model.addAttribute("user", user);
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			// Saving Data into DataBase
			userRepository.save(user);

			model.addAttribute("user", new User());
			return "Register-Done";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !! " + e.getMessage(), "alert-danger"));
			return "signup";
		}
	}

	@GetMapping("/signin")
	public String getLogin(Model model) {
		model.addAttribute("title", "Login - Smart Contact Manager");
		return "signin";
	}

	@GetMapping("/forgetPassword")
	public String forgetPassword(Model model) {
		model.addAttribute("title", "Forget Password");
		return "forgetPassword";
	}

	@PostMapping("/forgetPassword-process")
	public String forgetPasswordProcess(@RequestParam("email") String email, Model model, HttpSession session) {
		model.addAttribute("title", "Forget Password");
		try {
			Integer sendOtp = null;
			Random ran = new Random();
			sendOtp = ran.nextInt(999999);

			// System.out.println("Email = " + email);
			User user = userRepository.getUserByUsername(email);

			OTP storedOtp = otpRepositry.findOTPByEmail(email);
			if (storedOtp != null) {
				storedOtp.setOTP(sendOtp);
				otpRepositry.save(storedOtp);
				forgetEmail = email;
			} else {
				otpRepositry.save(new OTP(email, sendOtp));
			}

			if (user != null) {
				sendEmail.SendingEmail("InfoSphere Reset Password OTP - " + sendOtp, email,
						"Reset Password - InfoSphere");
				model.addAttribute("isMailSent", true);
				model.addAttribute("send", "Email has been send to your registered email address");
				model.addAttribute("email", email);
				return "forgetPassword";
			} else {
				session.setAttribute("message", new Message("The Email address is not registered !!", "alert-danger"));
				return "forgetPassword";
			}

		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong !! ", "alert-danger"));
			return "forgetPassword";
		}
	}

	@PostMapping("/validate-otp")
	public String processOTP(@RequestParam("otp") Integer otp, Model model, HttpSession session) {
		model.addAttribute("title", "Forget Password");
		model.addAttribute("email", forgetEmail);
		try {
			OTP storedOtp = otpRepositry.findOTPByEmail(forgetEmail);

			System.out.println(storedOtp.getOTP() + " === " + otp);
			if (storedOtp != null && otp != null && storedOtp.getOTP().equals(otp)) {
				model.addAttribute("isMailSent", null);
				model.addAttribute("verified", true);
				session.setAttribute("message",
						new Message("OTP verified successfully !! Enter new Password", "alert-success"));
				return "forgetPassword";
			} else {
				model.addAttribute("isMailSent", true);
				session.setAttribute("message", new Message("OTP not matched ", "alert-danger"));
				return "forgetPassword";
			}
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong !! ", "alert-danger"));
			return "forgetPassword";
		}

	}

	@PostMapping("/confirm-password")
	public String forgetConfirmPassword(@RequestParam("newPassword") String newPassoword,
			@RequestParam("confirmPassword") String confirmPassword, Model model, HttpSession session) {
		model.addAttribute("title", "Forget Password");
		try {
			if (newPassoword != null && confirmPassword != null && newPassoword.equals(confirmPassword)) {
				User user = userRepository.getUserByUsername(forgetEmail);
				user.setPassword(this.passwordEncoder.encode(newPassoword));
				userRepository.save(user);
				otpRepositry.delete(otpRepositry.findOTPByEmail(forgetEmail));
				session.setAttribute("message", new Message("Password changed successfully", "alert-success"));
				return "forgetPassword";

			} else {
				model.addAttribute("email", forgetEmail);
				session.setAttribute("message", new Message("password not matched", "alert-danger"));
				return "forgetPassword";
			}

		} catch (Exception e) {
			model.addAttribute("email", forgetEmail);
			session.setAttribute("message", new Message("Something went wrong !! ", "alert-danger"));
			return "forgetPassword";
		}
	}

}
