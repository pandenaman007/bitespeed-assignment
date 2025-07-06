package com.bitespeed.identifier.controller;

import com.bitespeed.identifier.dto.IdentifyRequest;
import com.bitespeed.identifier.dto.IdentifyResponse;
import com.bitespeed.identifier.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identify")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<IdentifyResponse> identifyContact(@RequestBody IdentifyRequest request) {
        IdentifyResponse response = contactService.identify(request);
        return ResponseEntity.ok(response);
    }
}
