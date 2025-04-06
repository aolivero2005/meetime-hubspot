package com.aom.meetime.hubspot.service.impl;

import com.aom.meetime.hubspot.Exception.MissingOrUnknownAuthCodeException;
import com.aom.meetime.hubspot.Exception.TokenExchangeError;
import com.aom.meetime.hubspot.model.request.HubSpotContactRequest;
import com.aom.meetime.hubspot.model.request.HubSpotWebhookEventRequest;
import com.aom.meetime.hubspot.model.request.TokensResponse;
import com.aom.meetime.hubspot.service.HubSpotAuthService;
import com.aom.meetime.hubspot.service.helper.TokensResponseCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the HubSpotAuthService interface, providing methods to handle
 * HubSpot authentication, contact creation, and webhook events.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HubSpotAuthServiceImpl implements HubSpotAuthService {

    @Value("${hubspot.client.id}")
    private String clientId;


    @Value("${hubspot.secret_client.id}")
    private String secretClientId;

    @Value("${hubspot.redirect.uri}")
    private String redirectUri;

    @Value("${hubspot.scopes}")
    private String scopes;

    @Value("${hubspot.optional.scopes}")
    private String optionalScopes;

    private final RestTemplate restTemplate;

    private final TokensResponseCreator tokensResponseCreator;


    /**
     * Generates an authorization URL to initiate the OAuth 2.0 authorization flow.
     *
     * @return The fully constructed authorization URL as a String.
     */
    public String generateAuthUrl() {
        String baseUrl = "https://app.hubspot.com/oauth/authorize";
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        String encodedScopes = URLEncoder.encode(scopes, StandardCharsets.UTF_8);
        String encodedOptionalScopes = URLEncoder.encode(optionalScopes, StandardCharsets.UTF_8);

        return baseUrl +
                "?client_id=" + clientId +
                "&redirect_uri=" + encodedRedirectUri +
                "&scope=" + encodedScopes +
                "&optional_scope=" + encodedOptionalScopes +
                "&response_type=code";
    }

    /**
     * Handles the OAuth callback by exchanging an authorization code for tokens from the API.
     * Communicates with the authorization server to fetch the tokens necessary for subsequent API interactions.
     *
     * @param code the authorization code provided by the authorization server after user consent
     * @return an instance of TokensResponse containing the tokens and associated data
     * @throws TokenExchangeError if an error occurs during the token exchange process
     * @throws MissingOrUnknownAuthCodeException if the provided authorization code is missing or invalid
     */
    @Override
    public TokensResponse callback(String code) {

        String tokenUrl = "https://api.hubapi.com/oauth/v1/token";

        // Preparamos los parámetros del formulario
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        form.add("client_secret", secretClientId);
        form.add("redirect_uri", redirectUri);
        form.add("code", code);

        // Establecemos los headers (application/x-www-form-urlencoded)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        try {
            // Realizamos la petición POST a la API de HubSpot
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                // Procesamos la respuesta con el token
                return tokensResponseCreator.create(response.getBody());
            } else {
                throw  new TokenExchangeError("Error en el intercambio de token");
            }
        } catch (RuntimeException e) {
            throw  new MissingOrUnknownAuthCodeException("missing or unknown auth code");
        }
    }

    /**
     * Creates a new contact in HubSpot using the provided contact details and authentication token.
     *
     * @param contactRequest the request object containing contact details such as email, first name, and last name
     * @param token the authentication token used for authorization when interacting with the HubSpot API
     * @return a ResponseEntity containing the response from the HubSpot API; may include the created contact information or an error message
     */
    @Override
    public ResponseEntity<?> createContact(HubSpotContactRequest contactRequest, String token) {

        String HUBSPOT_API_URL = "https://api.hubapi.com/crm/v3/objects/contacts";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> contact = Map.of(
                "properties", Map.of(
                        "email", contactRequest.getEmail(),
                        "firstname", contactRequest.getFirstName(),
                        "lastname", contactRequest.getLastName()
                )
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(contact, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(HUBSPOT_API_URL, request, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException.TooManyRequests e) {
            // Respeitando rate limit
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit excedido. Tente novamente mais tarde.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    /**
     * Handles incoming webhook events received from HubSpot.
     * Processes the list of webhook event requests and logs information for specific event types.
     *
     * @param events a list of HubSpotWebhookEventRequest objects representing the webhook events
     *               received from HubSpot
     */
    @Override
    public void webhookReceived(List<HubSpotWebhookEventRequest> events) {
        for (HubSpotWebhookEventRequest event : events) {
            if ("contact.creation".equals(event.getSubscriptionType())) {
                log.info("Novo contato criado com ID: {}", event.getObjectId());
            }
        }
    }

}
