package com.aom.meetime.hubspot.service.helper;

import com.aom.meetime.hubspot.model.request.TokensResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class TokensResponseCreator {

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
