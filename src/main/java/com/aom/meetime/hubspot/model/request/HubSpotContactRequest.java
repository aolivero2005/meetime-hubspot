package com.aom.meetime.hubspot.model.request;

import lombok.Data;

/**
 * Represents a request to create or update a contact in HubSpot.
 * <p>
 * This class contains basic contact information including email,
 * first name, and last name, which are typically required by
 * HubSpot when interacting with contact-related APIs.
 */
@Data
public class HubSpotContactRequest {

    private String email;
    private String firstName;
    private String lastName;
}
