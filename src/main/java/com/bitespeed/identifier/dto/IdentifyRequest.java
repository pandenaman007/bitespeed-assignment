package com.bitespeed.identifier.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class IdentifyRequest {
    private String email;
    private String phoneNumber;

}
