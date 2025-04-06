package com.aom.meetime.hubspot.model.request;

import lombok.Data;

/**
 * Represents a response containing tokens generated during an authentication process.
 * <p>
 * This class encapsulates both the access token and the refresh token, which are
 * typically used for managing user authentication and session handling in APIs.
 */
@Data
public class TokensResponse {

    private String accessToken;
    private String refreshToken;
}
