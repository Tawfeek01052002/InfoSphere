package com.smartmanager.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartmanager.dao.ContactRepository;
import com.smartmanager.dao.UserRepository;
import com.smartmanager.entity.Contact;
import com.smartmanager.entity.User;

@RestController
public class SearchController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@GetMapping("/search/{query}")
	public ResponseEntity<?> getContacts(@PathVariable("query") String query, Principal principal) {

		User user = userRepository.getUserByUsername(principal.getName());

		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);

		return ResponseEntity.ok(contacts);
	}
}
