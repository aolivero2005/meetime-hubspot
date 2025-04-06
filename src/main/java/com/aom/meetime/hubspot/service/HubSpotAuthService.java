package com.aom.meetime.hubspot.service;

import com.aom.meetime.hubspot.model.request.HubSpotContactRequest;
import com.aom.meetime.hubspot.model.request.HubSpotWebhookEventRequest;
import com.aom.meetime.hubspot.model.request.TokensResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HubSpotAuthService {

    String generateAuthUrl();

    TokensResponse callback(String code);

    ResponseEntity<?> createContact(HubSpotContactRequest contactRequest, String token);

    void webhookReceived(List<HubSpotWebhookEventRequest> events);
}
