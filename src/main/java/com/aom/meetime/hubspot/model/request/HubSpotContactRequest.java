package com.aom.meetime.hubspot.model.request;

import lombok.Data;

@Data
public class HubSpotContactRequest {

    private String email;
    private String firstName;
    private String lastName;
}
