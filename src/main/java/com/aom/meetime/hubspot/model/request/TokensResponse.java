package com.aom.meetime.hubspot.model.request;

import lombok.Data;

@Data
public class TokensResponse {

    private String accessToken;
    private String refreshToken;
}
