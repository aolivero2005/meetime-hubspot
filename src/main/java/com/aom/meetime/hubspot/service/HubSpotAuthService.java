package com.aom.meetime.hubspot.service;

import com.aom.meetime.hubspot.model.request.HubSpotContactRequest;
import com.aom.meetime.hubspot.model.request.HubSpotWebhookEventRequest;
import com.aom.meetime.hubspot.model.request.TokensResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HubSpotAuthService {

    /**
     * Generates and returns an authentication URL for initiating the OAuth process
     * with HubSpot. This URL is used to direct the user to HubSpot's authorization
     * page where they can grant necessary permissions.
     *
     * @return a string representation of the authentication URL.
     */
    String generateAuthUrl();

    /**
     * Handles the callback process for the OAuth authorization with HubSpot.
     * Once the user has granted authorization on the HubSpot authorization page,
     * this method exchanges the provided authorization code for access and refresh tokens.
     *
     * @param code the authorization code received from HubSpot after the user authorizes the application.
     * @return a {@code TokensResponse} containing the access token and refresh token for accessing HubSpot APIs.
     */
    TokensResponse callback(String code);

    /**
     * Creates a new contact in HubSpot using the provided contact details and authentication token.
     *
     * @param contactRequest an object containing the details of the contact to be created,
     *                       including email, first name, and last name.
     * @param token          the authentication token used to authorize the request to HubSpot.
     * @return a ResponseEntity containing the response from the HubSpot API, which may include
     *         the created contact details or an error message in case of failure.
     */
    ResponseEntity<?> createContact(HubSpotContactRequest contactRequest, String token);

    /**
     * Handles the processing of incoming webhook events from HubSpot. This method is invoked
     * when a list of webhook events is received, allowing the application to process and handle
     * updates or changes triggered by HubSpot.
     *
     * @param events a list of {@code HubSpotWebhookEventRequest} objects representing the webhook events
     *               received from HubSpot. Each event contains information about the subscription type,
     *               the object that triggered the webhook, and any relevant property changes.
     */
    void webhookReceived(List<HubSpotWebhookEventRequest> events);
}
