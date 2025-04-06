package com.aom.meetime.hubspot.controller;

import com.aom.meetime.hubspot.model.request.HubSpotContactRequest;
import com.aom.meetime.hubspot.model.request.HubSpotWebhookEventRequest;
import com.aom.meetime.hubspot.model.request.TokensResponse;
import com.aom.meetime.hubspot.service.HubSpotAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/hubspot")
@RequiredArgsConstructor
public class HubSpotAuthController {

    private final HubSpotAuthService hubSpotAuthService;

    @GetMapping("/authorize-url")
    public String generateAuthUrl() {
        return hubSpotAuthService.generateAuthUrl();
    }

    @GetMapping("/callback")
    public ResponseEntity<TokensResponse>  handleHubSpotCallback(
            @RequestParam("code") String code) {
        return ResponseEntity.ok(hubSpotAuthService.callback(code));
    }

    @PostMapping("/contacts")
    public ResponseEntity<?> createContact(@RequestBody HubSpotContactRequest contactRequest, @RequestParam String token) {
        return hubSpotAuthService.createContact(contactRequest, token);
    }

    @PostMapping("/webhook_received")
    public ResponseEntity<Void> webhookReceived(@RequestBody List<HubSpotWebhookEventRequest> events) {
        hubSpotAuthService.webhookReceived(events);
        return ResponseEntity.noContent().build();
    }

}
