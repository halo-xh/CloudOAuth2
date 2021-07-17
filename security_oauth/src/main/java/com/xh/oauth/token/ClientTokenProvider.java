package com.xh.oauth.token;

import com.xh.oauth.endpoints.request.FirstAuthorizationRequest;

public interface ClientTokenProvider {

    FirstAuthorizationRequest validateToken(String temTokenKey);

    String storeToken(FirstAuthorizationRequest authorizationRequest);

}
