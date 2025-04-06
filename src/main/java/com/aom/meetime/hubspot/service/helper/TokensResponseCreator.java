package com.aom.meetime.hubspot.service.helper;

import com.aom.meetime.hubspot.model.request.TokensResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * This class is responsible for creating a TokensResponse object from a given map of response body data.
 * It extracts specific token information, such as access and refresh tokens, from the provided map and
 * populates a TokensResponse instance with these values.
 */
@Component
public class TokensResponseCreator {

    /**
     * Creates a TokensResponse object from the given response body map.
     * Extracts the "access_token" and "refresh_token" values from the map
     * and sets them in a new TokensResponse instance.
     *
     * @param responseBody a map containing the token information with keys
     *                     "access_token" and "refresh_token". This map can be null.
     * @return a TokensResponse object populated with the extracted tokens, or null
     *         if the response body map is null.
     */
    public TokensResponse create(Map<String, Object> responseBody){

        TokensResponse tokensResponse = null;
        if(Objects.nonNull(responseBody)){
            tokensResponse = new TokensResponse();
            tokensResponse.setAccessToken((String) responseBody.get("access_token"));
            tokensResponse.setRefreshToken((String) responseBody.get("refresh_token"));
        }
        return tokensResponse;
    }
}
