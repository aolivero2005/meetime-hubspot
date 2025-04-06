package com.aom.meetime.hubspot.model.request;

import lombok.Data;

@Data
public class HubSpotWebhookEventRequest {

    private String subscriptionType;
    private Long objectId;
    private String propertyName;
    private String propertyValue;
}
