package com.bitespeed.identifier.repository;

import com.bitespeed.identifier.entity.Contact;
import com.bitespeed.identifier.entity.Contact.LinkPrecedence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByEmail(String email);

    List<Contact> findByPhoneNumber(String phoneNumber);

    List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);

    List<Contact> findByLinkedId(Long linkedId);

    List<Contact> findByLinkPrecedence(LinkPrecedence linkPrecedence);
}
