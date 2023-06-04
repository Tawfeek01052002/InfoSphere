package com.smartmanager.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartmanager.dao.ContactRepository;
import com.smartmanager.dao.UserRepository;
import com.smartmanager.entity.Contact;
import com.smartmanager.entity.User;
import com.smartmanager.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

	private Integer currentPage = 0;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String username = principal.getName();
		System.out.println("USERNAME = " + username);

		User user = userRepository.getUserByUsername(username);
		System.out.println("USER = " + user);

		model.addAttribute("user", user);
	}

	@GetMapping("/dashboard/home")
	public String getHome(Model model) {
		model.addAttribute("title", "Home");
		return "normal/dashboard/home";
	}

	// Show Contact Handler
	// per page= 5[n]
	// current page= 0[page]
	@GetMapping("/dashboard/viewContact/{page}")
	public String getviewContact(@PathVariable("page") Integer page, Model model, Principal principal) {

		String username = principal.getName();
		User user = userRepository.getUserByUsername(username);

		// current page -page
		// Contact per page -5
		Pageable pageable = PageRequest.of(page, 9);

		Page<Contact> contacts = contactRepository.findContactByusername(user.getId(), pageable);

		if (contacts.getTotalPages() <= page && page != 0) {
			return "normal/dashboard/InternalServerError";
		}
		model.addAttribute("title", "View Contacts");
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		this.currentPage = page;
		model.addAttribute("totalContacts", contacts.getTotalElements());
		model.addAttribute("totalPages", contacts.getTotalPages());
		return "normal/dashboard/viewContact";
	}

	// Showing Contact Info Handler
	@GetMapping("/contact/{cId}")
	public String getContactInfo(@PathVariable("cId") Integer cId, Model model, Principal principal) {

		model.addAttribute("title", "Contact Info");
		String username = principal.getName();
		User user = userRepository.getUserByUsername(username);
		Contact contact = contactRepository.findContactBycId(cId);
		System.err.println(contact);
		if (contact != null && contact.getUser().getId() == user.getId()) {
			model.addAttribute("contact", contact);
		}
		model.addAttribute("currentPage", this.currentPage);
		return "normal/dashboard/contactInfo";
	}

	// Deleting Contact handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model, Principal principal,
			HttpSession session) {

		String username = principal.getName();
		User user = userRepository.getUserByUsername(username);

		Contact contact = contactRepository.findContactBycId(cId);

		if (contact == null || contact.getUser().getId() != user.getId()) {
			return "normal/dashboard/InternalServerError";
		}

		user.getContact().remove(contact);
		this.userRepository.save(user);

		return "redirect:/user/dashboard/viewContact/" + currentPage;
	}

	// Update Contact Handler
	@GetMapping("/update/{cId}")
	public String getUpdateContact(@PathVariable("cId") Integer cid, Model model, Principal principal) {

		String username = principal.getName();
		User user = userRepository.getUserByUsername(username);

		Contact contact = contactRepository.findContactBycId(cid);

		if (contact == null || contact.getUser().getId() != user.getId()) {
			return "normal/dashboard/InternalServerError";
		}
		model.addAttribute("title", "Update Contact");
		model.addAttribute("contact", contact);
		model.addAttribute("currentPage", this.currentPage);
		return "normal/dashboard/updateContact";
	}

	@PostMapping("/process-UpdateContact/{cId}")
	public String updateContact(@PathVariable("cId") Integer id, @Valid @ModelAttribute Contact updatedContact,
			BindingResult result, @RequestParam("ProfileImage") MultipartFile file, Principal principal, Model model,
			HttpSession session) {

		model.addAttribute("title", "Update Contact");
		model.addAttribute("currentPage", this.currentPage);

		try {
			// Getting User by username
			String username = principal.getName();
			User user = userRepository.getUserByUsername(username);

			Contact contactDb = contactRepository.findContactBycId(id);

			System.out.println(contactDb.getcId());

			// checking that user have permission to update specific contact
			if (contactDb == null || contactDb.getUser().getId() != user.getId()) {
				return "normal/dashboard/InternalServerError";
			}

			if (result.hasErrors()) {
				model.addAttribute("contact", updatedContact);
				return "normal/dashboard/updateContact";
			} else {
				if (file.isEmpty()) {
					System.out.println("File is not Uploaded");
				} else if (!file.getContentType().equals("image/jpeg")) {
					model.addAttribute("contact", updatedContact);
					session.setAttribute("message", new Message("File should be of type jpg", "alert-danger"));
					return "normal/dashboard/updateContact";
				} else {
					contactDb.setImageUrl(file.getOriginalFilename());

					File saveFile = new ClassPathResource("static/images").getFile();

					String targetPath = saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename();
					InputStream is = file.getInputStream();
					byte data[] = new byte[is.available()];
					is.read(data);

					// writing the data
					FileOutputStream fos = new FileOutputStream(targetPath);
					fos.write(data);
					fos.close();

					file.transferTo(new File(targetPath));

				}
				contactDb.setName(updatedContact.getName());
				contactDb.setNickName(updatedContact.getNickName());
				contactDb.setEmail(updatedContact.getEmail());
				contactDb.setDescription(updatedContact.getDescription());
				contactDb.setPhone(updatedContact.getPhone());
				contactDb.setWork(updatedContact.getWork());

				contactRepository.save(contactDb);
				model.addAttribute("currentPage", this.currentPage);
				session.setAttribute("message", new Message("Contact Updated Successfully", "alert-success"));
				return "normal/dashboard/updateContact";
			}

		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
			model.addAttribute("contact", updatedContact);
			session.setAttribute("message", new Message("Something went wrong", "alert-danger"));
			return "normal/dashboard/updateContact";
		}
	}

	// Update Contact Error Handler if the request is get Request - (Showing
	// Internal Server error)
	@GetMapping("/process-UpdateContact/{cId}")
	public String updateContact(@PathVariable("cId") Integer id, Model model) {
		return "normal/dashboard/InternalServerError";
	}

	// Contact Adding Handler
	@GetMapping("/dashboard/addContact")
	public String getaddContact(Model model) {

		model.addAttribute("title", "Add Contacts");
		model.addAttribute("contact", new Contact());
		return "normal/dashboard/addContact";
	}

	// Form -Submitting Handler
	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute Contact contact, BindingResult result,
			@RequestParam("ProfileImage") MultipartFile file, Principal principal, Model model, HttpSession session) {

		model.addAttribute("title", "Add Contact");
		try {
			if (result.hasErrors()) {
				model.addAttribute("contact", contact);
				return "normal/dashboard/addContact";
			} else {
				String username = principal.getName();
				User user = userRepository.getUserByUsername(username);

				if (file.isEmpty()) {
					System.out.println("File is not Uploaded");
					contact.setImageUrl("defaultProfile.png");
				} else if (!file.getContentType().equals("image/jpeg")) {
					model.addAttribute("contact", contact);
					session.setAttribute("message", new Message("File should be of type jpg", "alert-danger"));
					return "normal/dashboard/addContact";
				} else {
					contact.setImageUrl(file.getOriginalFilename());

					File saveFile = new ClassPathResource("static/images").getFile();

					String targetPath = saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename();
					InputStream is = file.getInputStream();
					byte data[] = new byte[is.available()];
					is.read(data);

					// writing the data
					FileOutputStream fos = new FileOutputStream(targetPath);
					fos.write(data);
					fos.close();

					file.transferTo(new File(targetPath));

				}
				user.getContact().add(contact);
				contact.setUser(user);
				userRepository.save(user);
				session.setAttribute("message", new Message("Contact Added Successfully", "alert-success"));
				model.addAttribute("contact", new Contact());
				return "normal/dashboard/addContact";
			}

		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
			model.addAttribute("contact", contact);
			session.setAttribute("message", new Message("Something went wrong", "alert-danger"));
			return "normal/dashboard/addContact";
		}
	}

	@GetMapping("/dashboard/profile")
	public String getProfile(Model model) {
		model.addAttribute("title", "Profile");
		return "normal/dashboard/profile";
	}

	@GetMapping("/dashboard/setting")
	public String getSetting(Model model) {
		model.addAttribute("title", "Setting");
		return "normal/dashboard/setting";
	}

	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, @RequestParam("reNewPassword") String reNewPassword,
			Principal principal, HttpSession session) {

		User user = userRepository.getUserByUsername(principal.getName());

		System.out.println(newPassword);
		System.out.println(reNewPassword);

		if (!newPassword.equals(reNewPassword)) {
			session.setAttribute("message", new Message("Re-Entered Password is not matched", "alert-warning"));
			return "normal/dashboard/setting";
		} else if (this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			userRepository.save(user);
			session.setAttribute("message", new Message("Password Changed Successfully", "alert-success"));
			return "normal/dashboard/setting";
		} else {
			session.setAttribute("message", new Message("Entered Password is not matched", "alert-danger"));
			return "normal/dashboard/setting";
		}
	}
}
