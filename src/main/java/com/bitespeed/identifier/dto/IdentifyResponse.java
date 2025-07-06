package com.bitespeed.identifier.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentifyResponse {
    private ContactResponse contact;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactResponse {
        private Long primaryContatctId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Long> secondaryContactIds;
    }
}
