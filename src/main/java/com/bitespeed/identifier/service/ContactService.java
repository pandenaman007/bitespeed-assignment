package com.bitespeed.identifier.service;

import com.bitespeed.identifier.entity.Contact;
import com.bitespeed.identifier.entity.Contact.LinkPrecedence;
import com.bitespeed.identifier.repository.ContactRepository;
import com.bitespeed.identifier.dto.IdentifyRequest;
import com.bitespeed.identifier.dto.IdentifyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public IdentifyResponse identify(IdentifyRequest request) {
        String email = request.getEmail();
        String phone = request.getPhoneNumber();

        List<Contact> matchedContacts = contactRepository.findByEmailOrPhoneNumber(email, phone);

        if (matchedContacts.isEmpty()) {
                // No contact exists — create a new primary contact
                Contact newContact = Contact.builder()
                    .email(email)
                    .phoneNumber(phone)
                    .linkPrecedence(LinkPrecedence.PRIMARY)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            contactRepository.save(newContact);

            return buildResponse(newContact, Collections.emptyList());
        }

        // Find all unique contact IDs related to these contacts
        Set<Long> allContactIds = new HashSet<>();
        for (Contact c : matchedContacts) {
            allContactIds.add(c.getId());
            if (c.getLinkedId() != null) {
                allContactIds.add(c.getLinkedId());
            }
        }

        // Fetch all related contacts again (in case we missed some indirectly linked)
        List<Contact> allRelatedContacts = contactRepository.findAllById(allContactIds);

        // Determine the oldest contact — this becomes primary
        Contact primary = allRelatedContacts.stream()
                .min(Comparator.comparing(Contact::getCreatedAt))
                .orElseThrow();

        // Update all others to be secondary and link to the primary
        List<Contact> updatedSecondaries = new ArrayList<>();
        for (Contact contact : allRelatedContacts) {
            if (!contact.getId().equals(primary.getId())) {
                if (contact.getLinkPrecedence() != LinkPrecedence.SECONDARY || !Objects.equals(contact.getLinkedId(), primary.getId())) {
                    contact.setLinkPrecedence(LinkPrecedence.SECONDARY);
                    contact.setLinkedId(primary.getId());
                    contact.setUpdatedAt(LocalDateTime.now());
                    updatedSecondaries.add(contact);
                }
            }
        }
        contactRepository.saveAll(updatedSecondaries);

        // If incoming info is new, insert it as a secondary linked to primary
        boolean isNew = allRelatedContacts.stream()
                .noneMatch(c -> Objects.equals(c.getEmail(), email) && Objects.equals(c.getPhoneNumber(), phone));

        if (isNew) {
            Contact newSecondary = Contact.builder()
                    .email(email)
                    .phoneNumber(phone)
                    .linkPrecedence(LinkPrecedence.SECONDARY)
                    .linkedId(primary.getId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            contactRepository.save(newSecondary);
            allRelatedContacts.add(newSecondary);
        }

        return buildResponse(primary, allRelatedContacts);
    }

    private IdentifyResponse buildResponse(Contact primary, List<Contact> contacts) {
        Set<String> emails = new LinkedHashSet<>();
        Set<String> phones = new LinkedHashSet<>();
        List<Long> secondaryIds = new ArrayList<>();

        emails.add(primary.getEmail());
        phones.add(primary.getPhoneNumber());

        for (Contact c : contacts) {
            if (!c.getId().equals(primary.getId())) {
                if (c.getEmail() != null) emails.add(c.getEmail());
                if (c.getPhoneNumber() != null) phones.add(c.getPhoneNumber());
                secondaryIds.add(c.getId());
            }
        }

        IdentifyResponse.ContactResponse contactResponse = IdentifyResponse.ContactResponse.builder()
                .primaryContatctId(primary.getId())
                .emails(new ArrayList<>(emails))
                .phoneNumbers(new ArrayList<>(phones))
                .secondaryContactIds(secondaryIds)
                .build();

        return IdentifyResponse.builder()
                .contact(contactResponse)
                .build();
    }
}
