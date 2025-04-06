package com.aom.meetime.hubspot.model.request;

import lombok.Data;

/**
 * Represents a request received from a HubSpot webhook event.
 * <p>
 * This class encapsulates the details of an event triggered by
 * specific HubSpot webhook subscriptions. It includes information
 * about the type of subscription, the associated object, and any
 * relevant property changes.
 */
@Data
public class HubSpotWebhookEventRequest {

    private String subscriptionType;
    private Long objectId;
    private String propertyName;
    private String propertyValue;
}
