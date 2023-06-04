package com.smartmanager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartmanager.entity.Contact;
import com.smartmanager.entity.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	@Query("from Contact as c where c.user.id=:userId")
	public Page<Contact> findContactByusername(@Param("userId") int userId, Pageable pageable);

	public Contact findContactBycId(Integer cId);

	public List<Contact> findByNameContainingAndUser(String name, User username);
}
